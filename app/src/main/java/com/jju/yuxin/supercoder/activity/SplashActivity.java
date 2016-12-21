package com.jju.yuxin.supercoder.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.utils.NetUtils;
import com.jju.yuxin.supercoder.utils.PermissionHelper;

import net.youmi.android.AdManager;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

import java.util.Timer;
import java.util.TimerTask;

import static android.util.Log.e;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.activity
 * Created by yuxin.
 * Created time 2016/12/20 0020 下午 3:10.
 * Version   1.0;
 * Describe : 启动页面
 * History:
 * ==============================================================================
 */

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private Context mContext;

    private PermissionHelper mPermissionHelper;

    private LinearLayout show_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 判断是否已经获得联网权限
         */
        isGetPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 是否已经获取到联网权限
     */
    private void isGetPermission() {
        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");

                isGetConnected();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");

            isGetConnected();
        } else {
            // 权限全部申请，那就直接应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");

                isGetConnected();
            } else {
                // 还有权限未申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }

    /**
     *已经获取到了联网权限,判断是否已经联网
     */
    private void isGetConnected() {
        //检查联网状态
        if (NetUtils.isConnected(SplashActivity.this) == NetUtils.NO_CONNECTED) {
            e(TAG, "onResume" + "联网失败");

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //运行在UI线程的内容
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //提示用户是否跳转到网络设置界面
                            show_message = (LinearLayout) findViewById(R.id.show_message);
                            //设置信息提示界面可见
                            show_message.setVisibility(View.VISIBLE);
                            //属性动画
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                                ObjectAnimator animator = ObjectAnimator.ofFloat(show_message, "translationY", -150, 0);
                                animator.setDuration(500);
                                animator.setInterpolator(new BounceInterpolator());
                                animator.start();
                            }

                            //确定按钮
                            Button bt_left = (Button) findViewById(R.id.bt_left);
                            bt_left.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到无线和网络界面
                                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            });
                            //取消按钮
                            Button bt_right = (Button) findViewById(R.id.bt_right);
                            bt_right.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //退出程序
                                    finish();
                                    System.exit(0);
                                }
                            });

                        }
                    });
                }
            }, 0);
        } else {
            //网络连接正常
            //展示开屏广告
            setupSplashAd();

        }
    }
    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {
        // 创建开屏容器
        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //广告控件位于分割线上面
        params.addRule(RelativeLayout.ABOVE, R.id.view_divider);

        // 对开屏进行设置
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        // 设置跳转的窗口类
        splashViewSettings.setTargetClass(MainActiity.class);
        // 设置开屏的容器
        splashViewSettings.setSplashViewContainer(splashLayout);

        // 展示开屏广告
        SpotManager.getInstance(mContext)
                .showSplash(mContext, splashViewSettings, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.d(TAG, "开屏展示成功");
                        splashLayout.setVisibility(View.VISIBLE);
                        splashLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_splash_enter));
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        Log.d(TAG, "开屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                e(TAG, "无网络");
                                break;
                            case ErrorCode.NON_AD:
                                e(TAG, "无广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                e(TAG, "资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                e(TAG, "展示间隔限制");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                e(TAG, "控件处在不可见状态");
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.d(TAG, "开屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        Log.d(TAG, "开屏被点击");
                        Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
                    }
                });
    }
}
