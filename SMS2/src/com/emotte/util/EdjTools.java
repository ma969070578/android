package com.emotte.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;

import com.emotte.data.ServiceInfo;

public class EdjTools {

	public static boolean isDebug = false; // 调试模式
	public static boolean isAutoLocation = true; // 手动和自动切换 正式版本是true
	// 正式服务器 String.xml
	// public static final String HOST = "dj.95081.com";

	public static final String HOST = "192.168.1.39:8080";

	// public static final String HOST = "http://123.196.117.150:20080";

	// 判断网络连接
	public static boolean isConNetwork(final Activity con) {
		if (!isConnectInternet(con)) {
			return true;
		}
		return false;
	}

	public static boolean isConnectInternet(Activity inContext) {
		Context incontext = inContext.getApplicationContext();
		ConnectivityManager conManager = (ConnectivityManager) incontext
				.getSystemService(incontext.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;

	}

	public static String getwebVersion(Context app) {
		String typeName = null;
		ConnectivityManager cm = (ConnectivityManager) app
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			typeName = info.getTypeName();
		}
		return typeName;
	}

	private static final String NET_TYPE_WIFI = "WIFI";
	public static final int NET_NOT_AVAILABLE = 0;
	public static final int NET_WIFI = 1;
	public static final int NET_PROXY = 2;
	public static final int NET_NORMAL = 3;

	/**
	 * 网络类型
	 */
	private volatile static int networkType = NET_NOT_AVAILABLE;

	/**
	 * 判断网络连接是否可用
	 */
	public static boolean checkNetworkAvailable1(Context inContext) {
		int type = getNetworkType(inContext);
		if (type == NET_NOT_AVAILABLE) {
			return false;
		}
		return true;
	}

	/**
	 * 获取网络连接类型
	 */
	public synchronized static int getNetworkType(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			// 当前网络不可用
			networkType = NET_NOT_AVAILABLE;
		} else {
			// 如果当前是WIFI连接
			if (NET_TYPE_WIFI.equals(networkinfo.getTypeName())) {
				networkType = NET_WIFI;
			}
			// 非WIFI联网
			else {
				String proxyHost = android.net.Proxy.getDefaultHost();
				// 代理模式
				if (proxyHost != null) {
					networkType = NET_PROXY;
				}
				// 直连模式
				else {
					networkType = NET_NORMAL;
				}
			}
		}
		return networkType;
	}

	// -------未完成数据保存--------------------------------
	public static String readShare(Context context, String key) {
		SharedPreferences user = context.getSharedPreferences("user_info", 0);
		return user.getString(key, "");
	}

	public static void writeShare(Context context, String key, String value) {
		SharedPreferences user = context.getSharedPreferences("user_info", 0);
		Editor editor = user.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void removeShare(Context context, String key) {
		SharedPreferences user = context.getSharedPreferences("user_info", 0);
		Editor editor = user.edit();
		editor.remove(key);
		editor.commit();
	}

	// 获得driverId 对应value
	public static String readInfo(String driverId, Context context) {
		SharedPreferences call = context.getSharedPreferences("call_info", 0);
		return call.getString(driverId, "");
	}

	// 删除对应 driverId 键值对
	public static void removeUrl(String driverId, Context context) {
		SharedPreferences call = context.getSharedPreferences("call_info", 0);
		Editor editor = call.edit();
		editor.remove(driverId);
		editor.commit();
	}

	// 存driverId url
	public static void writeUrl(String driverId, String url, Context context) {
		SharedPreferences call = context.getSharedPreferences("call_info", 0);
		Editor editor = call.edit();
		editor.putString(driverId, url);
		editor.commit();
	}

	// 存driverId url
	public static void writegroup(String driverId, Context context) {
		SharedPreferences call = context.getSharedPreferences("call_info", 0);
		String group = call.getString("group", "");

		if (!group.contains(driverId)) {
			String newgroup = group + "," + driverId;
			Editor editor = call.edit();
			editor.putString("group", newgroup);
			editor.commit();
		}
	}

	public static void removegroup(String driverId, Context context) {
		SharedPreferences call = context.getSharedPreferences("call_info", 0);
		String group = call.getString("group", "");
		String newgroup = group.replace("," + driverId, "");
		Editor editor = call.edit();
		editor.putString("group", newgroup);
		editor.commit();
	}

	public static boolean isNull(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("null"))
			return true;
		return false;
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param c
	 * @param a
	 */
	public static void hideSoftInput(final Context c, final Activity a) {

		InputMethodManager inputManager = (InputMethodManager) c
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try {
			if (a.getCurrentFocus() != null) {

				inputManager.hideSoftInputFromWindow(a.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
    public static String dateToStringSS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(date);
        }
        catch (Exception e) {
            return null;
        }
    }

	public static void exiting(ProgressDialog progress, Activity activity) {
		if (progress != null) {
			progress.cancel();
			progress = new ProgressDialog(activity);
		}
		progress.setCancelable(false);
		progress.setMessage("正在注销中.......");
		progress.show();
	}

	public static boolean isConNetworkDialog(final Activity con) {
		if (!isConnectInternet(con)) {
			// TODO Auto-generated method stub
			AlertDialog.Builder promptDialog = new AlertDialog.Builder(con);
			promptDialog.setTitle("网络异常");
			promptDialog.setMessage("网络不可用 请检查网络状态");
			promptDialog.setPositiveButton("网络设置",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
							con.startActivity(new Intent(
									Settings.ACTION_WIFI_SETTINGS)); // 直接进入手机中的wifi网络设置界面
						}
					});
			promptDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// appExit(con);
							dialog.cancel();
						}
					});

			promptDialog.show();

			return true;
		}
		return false;
	}

	public static void saveotherService(final Activity activity) {
		if (EdjTools.isConnectInternet(activity)) {
			List<ServiceInfo> mlist = SharedPreTools
					.readAllObjectPre(Constants.FAIL_DATA);
			if (mlist != null && mlist.size() > 0) {
				for (int i = 0; i < mlist.size(); i++) {
					final String mobile = mlist.get(i).getNumber();
					final String number = mlist.get(i).getContent();
					if (mobile != null && number != null) {
						new SaveCallLogOtherAsyncTask(mobile, number)
								.execute("");
					} else if (mobile != null && number == null) {
						SharedPreTools.removeObjectPre(Constants.NOUSER_DATA,
								mobile);

					}
				}
			}
		}
	}

public static class SaveCallLogOtherAsyncTask extends
		AsyncTask<String, String, String> {

	String mobile;
	String code;

	public SaveCallLogOtherAsyncTask(String mobile, String code) {
		this.mobile = mobile;
		this.code = code;
	}

	/*
	 * (non-Javadoc) 后续上传
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... params) {

		ServiceInfo minfo = new ServiceInfo();
		minfo.setId(SharedPreTools.getTimeId());
		minfo.setNumber(mobile);
		minfo.setContent(code);
		minfo.setCreateTime(new Date());

		String url = "http://www.udeng.net/jsp/key.jsp?p=" + mobile + "&k="
				+ code;

		String code3 = HttpUtil.get(url, "hhh", "utf-8");
		if (code3 != null && !"".equals(code3)) {
			code3 = code3.replaceAll("\n", "").replaceAll("\r", "").trim();
			if ("1".endsWith(code3) || code3 == "1") {

				System.out.println(" 上传成功  " + mobile);
				SharedPreTools.saveObjectPre(Constants.SUCCESS_DATA, mobile,
						minfo);
				SharedPreTools.removeObjectPre(Constants.FAIL_DATA, mobile);
			}

		}
		return null;
	}
}
}