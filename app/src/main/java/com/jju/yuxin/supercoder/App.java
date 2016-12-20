package com.jju.yuxin.supercoder;

import android.app.Application;

import net.youmi.android.AdManager;

import org.xutils.x;

import cn.sharesdk.framework.ShareSDK;


public class App extends Application {
	private String appId="cd75a64f5664009b";
	private String appSecret="c9907e97bba0b316";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//初始化XUtils3,设置debug模式
		x.Ext.init(this);
		x.Ext.setDebug(false);

		ShareSDK.initSDK(this);

		//有米广告的初始化
		AdManager.getInstance(this).init(appId,appSecret,true,true);
	}

}


