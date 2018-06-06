package com.yuliu.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 存储到磁盘删除app文件都不会清除掉
 */

public class DiskLruCacheUtils {
    private volatile static DiskLruCacheUtils singleton;
    private DiskLruCache mDiskLruCache = null;

    public static DiskLruCacheUtils getSingleton() {
        if (singleton == null) {
            synchronized (DiskLruCacheUtils.class) {
                if (singleton == null) {
                    singleton = new DiskLruCacheUtils();
                }
            }
        }
        return singleton;
    }

    private DiskLruCacheUtils() {
        try {
            File cacheDir = getDiskCacheDir(App.mApp.getApplicationContext(), "BTC");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(App.mApp.getApplicationContext()), 1, 100 * 1024 * 1024);
            MMLog.v(mDiskLruCache.getDirectory().getAbsolutePath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param key
     * @param value 说明 保存字符串到diskCache
     */
    public void putString(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
            return;
        DiskLruCache.Editor editor = null;
        BufferedWriter bw = null;
        String md5 = hashKeyForDisk(key);
        try {
            editor = mDiskLruCache.edit(md5);
            if (editor == null)
                return;
            OutputStream outputStream = editor.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(value);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param key 说明：获取String
     */
    public String getString(String key) {
        InputStream inputStream = null;
        inputStream = get(key);
        if (inputStream != null) {
            String result = null;
            try {
                result = readFully(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


    /**
     * @param key 通用获取方式
     * @return InputStream
     */
    public InputStream get(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            String md5 = hashKeyForDisk(key);
            if (mDiskLruCache != null) {
                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(md5);
                if (snapShot != null) {
                    InputStream is = snapShot.getInputStream(0);
                    return is;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            reader.close();
        }
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        File mFile;
        //        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
        //            cachePath = context.getExternalCacheDir().getPath();
        //        } else {
        //            cachePath = context.getCacheDir().getPath();
        //        }
        mFile = Environment.getExternalStoragePublicDirectory(uniqueName);
        return mFile;
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     */
    public boolean remove(String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }
        try {
            String md5 = hashKeyForDisk(id);
            if (mDiskLruCache != null) {
                return mDiskLruCache.remove(md5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 清空cache 调用该方法之后DiskLruCache将关闭
     */
    public void clearCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭cache 一般在onDestory中调用
     */
    public void close() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 判断是否已经关闭
     */
    public boolean isClosed() {
        if (mDiskLruCache != null) {
            return mDiskLruCache.isClosed();
        }
        return true;
    }


    /**
     * 得到缓存大小
     */
    public long size() {
        if (mDiskLruCache != null) {
            return mDiskLruCache.size();
        }
        return 0;
    }


    /**
     * 设置max cache 大小
     */
    public void setMaxSize(long maxSize) {
        if (mDiskLruCache != null) {
            mDiskLruCache.setMaxSize(maxSize);
        }
    }

    /**
     * 获取最大缓存
     */
    public long getMaxSize() {
        if (mDiskLruCache != null) {
            return mDiskLruCache.getMaxSize();
        }
        return 0;
    }


    /**
     * 获取cache路径
     */
    public File getDirectory() {
        if (mDiskLruCache != null) {
            return mDiskLruCache.getDirectory();
        }
        return null;
    }


}
