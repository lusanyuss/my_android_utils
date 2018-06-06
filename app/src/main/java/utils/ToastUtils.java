package utils;

import android.content.Context;
import android.widget.Toast;

import com.yuliu.base.App;

public class ToastUtils {

    protected static Toast toast = null;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context mContext, String s, int duration) {
        if (StringUtils.isKong(s)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(mContext, s, duration);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > duration) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getString(resId), duration);
    }

    public static void showToast(Context mContext, String msg) {
        showToast(mContext, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context mContext, int resId) {
        showToast(mContext, mContext.getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(String msg) {
        showToast(App.mApp.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

}
