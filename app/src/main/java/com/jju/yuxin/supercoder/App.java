package com.jju.yuxin.supercoder;

import android.app.Application;

import org.xutils.x;

import cn.sharesdk.framework.ShareSDK;


public class App extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//初始化XUtils3,设置debug模式
		x.Ext.init(this);
		x.Ext.setDebug(false);

		ShareSDK.initSDK(this);
	}

}


