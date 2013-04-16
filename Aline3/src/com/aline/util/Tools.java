package com.aline.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import cn.waps.AppConnect;

import com.aline.activity.R;
import com.aline.app.App;
import com.google.gson.internal.bind.MapTypeAdapterFactory;

public class Tools {

	public static boolean isDebug = true; // 调试模式
	public static Long driverid = 1l;
	public static String driverpwd = "111";
	public static final String HOST = "dj.95081.com";

	public static final String pushusername = "emotte";
	public static final String pushpasswd = "123456";
	public static final String AppKey = "a59219778be8585b98c730b8";

	public static ExecutorService executorService = Executors
			.newCachedThreadPool();

	public static void getDevInfo(Context context) {
		App.getInstance().networkType = Tools.getwebVersion(context);
		App.getInstance().clientVersion = context.getResources().getString(
				R.string.revision);
		App.getInstance().sysVersion = android.os.Build.VERSION.RELEASE;
		App.getInstance().mobileModel = android.os.Build.MODEL;
		App.getInstance().appkey = Tools.getAppKey(context);
	}

	public static void recomment(final Activity a, final App app) {

		new Thread(new Runnable() {
			public void run() {
				String ss = a.getResources().getString(R.string.sms);

				PackageManager pm = a.getPackageManager();
				Intent it = new Intent(Intent.ACTION_SEND);
				it.setType("text/plain");
				it.putExtra(Intent.EXTRA_SUBJECT, "欢迎使用一线牵"); // 分享的主题
				List<ResolveInfo> resInfo = pm.queryIntentActivities(it, 0);
				if (!resInfo.isEmpty()) {
					List<Intent> targetedShareIntents = new ArrayList<Intent>();
					for (ResolveInfo info : resInfo) {
						Intent targeted = new Intent(Intent.ACTION_SEND);
						targeted.setType("text/plain");
						it.putExtra(Intent.EXTRA_SUBJECT, "欢迎使用一线牵"); // 分享的主题
						ActivityInfo activityInfo = info.activityInfo;

						if (activityInfo.packageName.contains("bluetooth")
								|| activityInfo.name.contains("bluetooth")
								|| activityInfo.packageName
										.contains("UCMobile")
								|| activityInfo.name.contains("UCMobile")
								|| activityInfo.packageName.contains("renren")
								|| activityInfo.name.contains("renren")
								|| activityInfo.packageName.contains("tencent")
								|| activityInfo.name.contains("tencent")) {
							continue;
						}

						targeted.putExtra(Intent.EXTRA_TEXT, ss);
						targeted.setPackage(activityInfo.packageName);
						targetedShareIntents.add(targeted);
					}

					Intent chooserIntent = Intent.createChooser(
							targetedShareIntents.remove(0), "分享");

					if (chooserIntent == null) {
						return;
					}

					chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
							targetedShareIntents.toArray(new Parcelable[] {}));

					try {
						a.startActivity(chooserIntent);
					} catch (android.content.ActivityNotFoundException ex) {
						// Toast.makeText(this,
						// "Can't find share component to share",
						// Toast.LENGTH_SHORT).show();
					}
				}

			}
		}).start();

	}

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

	public static String getPhone(String jobPhone, String phone) {
		String defaultPhone = "95081";
		if (jobPhone != null && jobPhone.contains("null")) {
			jobPhone = "";
		}
		if (phone != null && phone.contains("null")) {
			phone = "";
		}
		if (jobPhone != null && !jobPhone.equals("")
				&& !jobPhone.equals("null")) {
			return jobPhone;
		}

		if (phone != null && !phone.equals("") && !phone.equals("null")) {
			return phone;
		}
		return defaultPhone;

	}

	public static String readFlag(Context context) {
		SharedPreferences user = context.getSharedPreferences("user_flag", 0);
		return user.getString("flag", "");
	}

	public static void writeFlag(String flag, Context context) {
		SharedPreferences user = context.getSharedPreferences("user_flag", 0);
		Editor editor = user.edit();
		editor.putString("flag", flag);
		editor.commit();
	}

	// public static String readUserKey(Context context) {
	// SharedPreferences user = context.getSharedPreferences("user_info", 0);
	// return user.getString("userKey", "");
	// }
	//
	// private static void writeUserKey(String userKey, Context context) {
	// SharedPreferences user = context.getSharedPreferences("user_info", 0);
	// Editor editor = user.edit();
	// editor.putString("userKey", userKey);
	// editor.commit();
	// }

	// -------------推送appkey--------------------
	public static String readAppKey(Context context) {
		SharedPreferences user = context.getSharedPreferences("appkey", 0);
		return user.getString("appkey", "");
	}

	public static void writeAppKey(String userKey, Context context) {
		SharedPreferences user = context.getSharedPreferences("appkey", 0);
		Editor editor = user.edit();
		editor.putString("appkey", userKey);
		editor.commit();
	}

	public static String getAppKey(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String appkey = "";
		String imei = tm.getDeviceId();
		if (imei != null && !"".equals(imei)) {
			appkey = imei;
		} else {
			appkey = "" + (Math.random() * 1000000 * 1000000 * 1000);
		}
		writeAppKey(appkey, context);
		return appkey;
	}

	// ------------------------------------------
	public static String readTelephone(Context context) {
		SharedPreferences user = context.getSharedPreferences("user_info", 0);
		return user.getString("telephone", "");
	}

	public static void writeTelephone(String telephone, Context context) {
		SharedPreferences user = context.getSharedPreferences("user_info", 0);
		Editor editor = user.edit();
		editor.putString("telephone", telephone);
		editor.commit();
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	public static String getTelephone(Context context) {
		String telephone = readTelephone(context);
		if (telephone != null && !"".equals(telephone)) {
			return telephone;
		} else {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// 先获取手机号
			String mobile = tm.getLine1Number();
			// System.out.println(mobile+"..................mobile");
			if (mobile != null && !"".equals(mobile)) {
				writeTelephone(mobile, context);
				return mobile;
			}
		}
		return null;
	}

	// ----------------------------------------jk
	public static boolean idNotInvalid(long num) {
		if (num > 0)
			return true;
		return false;
	}

	public static boolean isNull(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("null"))
			return true;
		return false;
	}

	// public static String getUserKey(Context context) {
	// TelephonyManager tm = (TelephonyManager) context
	// .getSystemService(Context.TELEPHONY_SERVICE);
	// // 先获取手机号
	// String mobile = tm.getLine1Number();
	// if (mobile != null && !"".equals(mobile)) {
	// return mobile;
	// }
	//
	// /*
	// * String simSerialNumber = tm.getSimSerialNumber(); if (simSerialNumber
	// * != null && !"".equals(simSerialNumber)) { return simSerialNumber; }
	// *
	// * String subscriberId = tm.getSubscriberId(); if (subscriberId != null
	// * && !"".equals(subscriberId)) { return subscriberId; }
	// */
	//
	// String imei = tm.getDeviceId();
	// if (imei != null && !"".equals(imei)) {
	// return imei;
	// }
	//
	// String mac = getLocalMacAddress(context);
	// if (mac != null && !"".equals(mac)) {
	// return mac;
	// }
	//
	// String userKey = readUserKey(context);
	// if (userKey != null && !"".equals(userKey)) {
	// return userKey;
	// }
	//
	// userKey = "" + new Date().getTime() + ((int) (Math.random() * 1000000));
	// writeUserKey(userKey, context);
	// return userKey;
	// }

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

	public static void appExit(final Activity activity) {

		String title = "您确认关闭"
				+ App.getInstance().getResources().getString(R.string.app_name)
				+ "客户端吗？";

		new AlertDialog.Builder(activity).setIcon(R.drawable.ic_launcher)
				.setTitle("关闭程序").setMessage(title)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// activity.finish();

						App.finishAll();

						AppConnect.getInstance(activity).finalize();

						Intent intent2 = new Intent(Intent.ACTION_MAIN);
						intent2.addCategory(Intent.CATEGORY_HOME);
						intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						activity.startActivity(intent2);
						if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
							android.os.Process.killProcess(android.os.Process
									.myPid());
						} else {

							ActivityManager am = (ActivityManager) activity
									.getSystemService(Context.ACTIVITY_SERVICE);
							am.restartPackage(activity.getPackageName());
							System.exit(0);
						}
					}
				}).show();
	}

	/**
	 * 生成极光推送发送编号
	 * 
	 * @return
	 */
	public static int getSendNo() {

		SimpleDateFormat format = new SimpleDateFormat("MM-ddHH-mm-ss");
		String time = format.format(new Date());
		System.out.println("//////times:" + time + "..."
				+ time.replaceAll("-", "") + "........"
				+ Integer.parseInt(time.replaceAll("-", "")));
		int jgno = Integer.parseInt(time.replaceAll("-", ""));
		return jgno;

	}

	/**
	 *  n_builder_id 可选 1-1000的数值，不填则默认为 0，使用 极光Push SDK 的默认通知样式。
	 *  n_title 可选通知标题。不填则默认使用该应用的名称。 
	 *  n_content 必须 通知内容。 
	 *  n_extras 可选通知附加参数。JSON格式。客户端可取得全部内容。
	 * @return
	 */
	public static JSONObject getMsgContent(String builder, String title,
			String content, String extras) {
		HashMap map = new HashMap();
		if (builder != null && !"".equals(builder)) {
			map.put("n_builder_id", builder);
		}
		if (title != null && !"".equals(title)) {
			map.put("n_title", title);
		} 
		if(content != null && !"".equals(content)) {
			System.out.println("content........"+content);
			map.put("n_content", content);
		} 
		if (extras != null && !"".equals(extras)) {
			map.put("n_extras", extras);
		}
		JSONObject obj = Tools.mapToJson(map);
		return obj;
	}

	/**
	 * 将Map转换为JSON格式数据 从Map中抽取数据返回JSON格式数据
	 * 
	 * @param map
	 *            Map<String, String[]>
	 * @return
	 * @see [类、类#方法、类#成员]
	 */

	public static JSONObject mapToJson(Map<String, String> map) {
		if (null == map) {
			return null;
		}
		Set<Map.Entry<String, String>> set = map.entrySet();
		JSONObject jObject = new JSONObject();

		for (Iterator<Entry<String, String>> it = set.iterator(); it
				.hasNext();) {
			Entry<String, String> entry = it.next();
			try {
				jObject.put(entry.getKey(), entry.getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jObject;
	}

}
