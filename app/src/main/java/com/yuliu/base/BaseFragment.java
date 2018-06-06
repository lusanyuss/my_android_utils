package com.yuliu.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends RxFragment {

    public Activity       mActivity;
    public Context        mContext;
    public Resources      mResources;
    public LayoutInflater mInflater;
    public App            mApp;
    public Unbinder       unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
        mContext = getActivity().getApplicationContext();
        mApp = (App) getActivity().getApplication();
        mInflater = LayoutInflater.from(getActivity().getApplicationContext());
        mResources = getResources();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(getLayoudId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        initData(getArguments());
        initView();
        return mRootView;
    }

    public abstract int getLayoudId();

    /**
     * 初始化数据
     */
    public abstract void initData(Bundle arguments);

    /**
     * 初始化View
     */
    public abstract void initView();


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public View findViewById(int resId) {
        return mActivity.findViewById(resId);
    }


}
