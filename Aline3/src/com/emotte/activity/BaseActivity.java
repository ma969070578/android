package com.emotte.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.aline.activity.R;
import com.aline.android.http.RequestParams;
import com.aline.app.EdjApp;
import com.aline.util.Constants;
import com.aline.util.EdjTools;
import com.aline.util.SystemOut;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity {
	public EdjApp app;

	private NetCheckReceiver netCheckReceiver;
	private ShowNotifyReceiver verUpReceiver;

	public Intent recordServerIntent;
	protected Activity context;
	private Button mBtnRight;
	private static final int CHANG_USER_SUCCESS = 9999;
	private static final int CHANG_USER_FAIL = 8888;

	public ViewPager vp;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		EdjApp.addActivity(this);

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		app = (EdjApp) this.getApplication();
		context = this;
		if (!EdjTools.isDebug) {
			MobclickAgent.setDebugMode(true);
			com.umeng.common.Log.LOG = true;
		}
		// SystemOut.out("DeviceInfo:"+EdjTools.getDeviceInfo(this));

		// JPushInterface.resumePush(getApplicationContext());

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EdjApp.popActivityRemove(this);

		unregistReceiver();
		unregisterNetCheckReceiver();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		registerNetCheckReceiver();
		registReceiver();
		super.onResume();
		MobclickAgent.onResume(this);

		JPushInterface.onResume(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		registerNetCheckReceiver();
		// recordServerIntent = new Intent(getApplicationContext(),
		// Recorder.class);
		// startService(recordServerIntent);
		// bindService(recordServerIntent, serviceConnection, BIND_AUTO_CREATE);
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		// if (serviceBinder != null) {
		// unbindService(serviceConnection);
		// }
		super.onStop();
		// JPushInterface.stopPush(getApplicationContext());
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

	}

	public void registerNetCheckReceiver() {
		if (netCheckReceiver == null) {
			netCheckReceiver = new NetCheckReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Constants.CONNECTIVITY_CHANGE_ACTION);

			registerReceiver(netCheckReceiver, filter);
		}
	}

	private void unregisterNetCheckReceiver() {
		if (netCheckReceiver != null) {
			unregisterReceiver(netCheckReceiver);
			netCheckReceiver = null;
		}

	}

	class NetCheckReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// System.out.println("ACTION:" + intent.getAction());
			if (intent.getAction().equals(Constants.CONNECTIVITY_CHANGE_ACTION)) {
				SystemOut
						.out("CONNECTIVITY_CHANGE_ACTION---------- 网络改变--------------");
				ConnectivityManager mConnMgr = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (mConnMgr != null) {
					NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();
					// System.out.println("aActiveInfo=" + aActiveInfo);
					if (aActiveInfo == null) {
						Toast.makeText(BaseActivity.this, R.string.net_err,
								Toast.LENGTH_SHORT).show();
					} else {

						// CallTool.saveotherService(BaseActivity.this);
						// CallTool.saveNewOtherService();
					}

				}
			}

		}
	}

	private void registReceiver() {
		if (verUpReceiver == null) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Constants.INTENT_ACTION_GET_ISYS);
			verUpReceiver = new ShowNotifyReceiver();
			registerReceiver(verUpReceiver, filter);
		}
	}

	private void unregistReceiver() {
		if (verUpReceiver != null) {
			unregisterReceiver(verUpReceiver);
			verUpReceiver = null;
		}

	}

	private class ShowNotifyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.INTENT_ACTION_GET_ISYS.equals(intent.getAction())) {
				checkCityYs(app.city);
			}
		}

	}

	private void checkCityYs(String city) {
		RequestParams params = new RequestParams();
		// params.put("city", EdjTools.GetPeople(app.city));
		// HttpConnection.getMaternity_Matron(params,checkysAsyncHttpResponseHandler);

	}

	// AsyncHttpResponseHandler checkysAsyncHttpResponseHandler = new
	// AsyncHttpResponseHandler() {
	// @Override
	// public void onStart() {
	// SystemOut.out("开始");
	// super.onStart();
	//
	// }
	//
	// @Override
	// public void onFinish() {
	// SystemOut.out("完成");
	// super.onFinish();
	// }
	//
	//
	// @Override
	// public void onSuccess(String content) {
	// try {
	//
	// if(content!=null&&content.startsWith("{")){
	// JSONObject json = new JSONObject(content);
	// if(json.optInt("result")==0){
	// app.checkYs =true;
	// }else{
	// app.checkYs =false;
	//
	// }
	// }else{
	// app.checkYs =false;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// SystemOut.out("检测月嫂成功：" + content);
	// super.onSuccess(content);
	// }
	//
	//
	// @Override
	// public void onFailure(Throwable error, String content) {
	// SystemOut.out("失败：" + content);
	// app.checkYs =false;
	// super.onFailure(error, content);
	// }
	// };

}
