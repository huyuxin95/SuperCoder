package com.jju.yuxin.supercoder;

import android.app.Application;
import net.youmi.android.AdManager;
import org.xutils.x;
import cn.sharesdk.framework.ShareSDK;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName App
 * Created by yuxin.
 * Created time 20-12-2016 22:03.
 * Describe :APP继承Application,
 *             这里主要是做一些库的初始化操作,确保在第一个启动类中初始化完成
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
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
		//sharedSDK初始化
		ShareSDK.initSDK(this);

		//有米广告的初始化
		AdManager.getInstance(this).init(appId,appSecret,false,true);
	}

}


