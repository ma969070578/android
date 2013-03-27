package com.aline.app;

import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class App extends Application {

	public static String TAG = "app";

	public SharedPreferences prefs = null;
	public Editor editor;
	public static String charSet = "utf-8";
	public String city;// 城市 带有市，县等
	public String street = "";
	public static int lat;// 纬度
	public static int lon;// 经度

	public String userKey;// 用户标识
	public String telephone;// 用户电话
	public String userName;
	public String imei;
	public String userid;

	public static App mApp;

	@Override
	public void onCreate() {

		mApp = this;

		// prefs = getSharedPreferences(Constants.MEME_PREFS,
		// Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		// editor = prefs.edit();
		// city = prefs.getString(Constants.DEFVALUE_CITY, "");
		 Log.d(TAG, "onCreate");
         super.onCreate();
         JPushInterface.setDebugMode(true); 	//设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
	}

	public static App getInstance() {
		return mApp;
	}
	
	
	
	public static Stack<Activity> stackActivity;

	public static void addActivity(Activity activity) {
		if (stackActivity == null)
			stackActivity = new Stack<Activity>();
		stackActivity.add(activity);
	}

	public static void popActivityRemove(Activity activity) {
		if (stackActivity != null && stackActivity.contains(activity)) {
			stackActivity.remove(activity);
		}
	}

	public static void finishAll() {
		if (stackActivity != null) {
			for (Activity a : App.stackActivity) {
				a.finish();
			}
			App.stackActivity.clear();
		}
	}

	public static void finishOne(String clazzName) {
		if (stackActivity != null) {
			for (Activity activity : stackActivity) {
				if (activity.getComponentName().getClassName().equals(clazzName)) {
					activity.finish();
				}
			}
		}
	}
}
