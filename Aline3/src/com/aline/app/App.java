package com.aline.app;

import java.util.Stack;

import cn.jpush.android.api.JPushInterface;
import cn.waps.AppConnect;

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
	public boolean isAutoUpate = false;
	public boolean isLocation=false;
	public boolean isStartCheckUpdata=false;
	
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
         
         
         
         AppConnect.getInstance(this);	//必须确保AndroidManifest文件内配置了WAPS_ID
//         开发者可通过代码开启错误报告功能，并通过管理后台的“错误报告”功能随时查看收集到的错误报告，以便及时进行修正。
         AppConnect.getInstance(this).setCrashReport(false);//默认值true开启，设置false关闭

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
