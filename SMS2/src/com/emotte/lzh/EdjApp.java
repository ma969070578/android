package com.emotte.lzh;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class EdjApp extends Application {
	

	
	public static String TAG = "emotte.djb";
	
	public SharedPreferences prefs = null;
	public Editor editor;
	public static String charSet = "utf-8";
	public String city;// 城市 带有市，县等
	public String street="";
	public int lat;// 纬度
	public int lon;// 经度
	
	public String userKey;// 用户标识
	public String telephone;// 用户电话

	public String userName;
	public String imei;
	public String userid;


	public static EdjApp mEdjApp;

	
	public boolean iscon=false;
	@Override
	public void onCreate() {
		
		mEdjApp = this;
		
		super.onCreate();
	}

	public static EdjApp getInstance() {
		return mEdjApp;
	}


	public SharedPreferences getPrefs(){
		return prefs;
	}

}
