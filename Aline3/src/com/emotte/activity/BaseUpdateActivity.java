package com.emotte.activity;

import org.apache.http.HttpConnection;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.aline.app.EdjApp;
import com.aline.download.AutoUpdater;
import com.aline.util.Constants;
import com.aline.util.EdjTools;

public class BaseUpdateActivity extends BaseActivity {
	private static final String TAG = "BaseActivity";
	public EdjApp app;

	public ProgressDialog pBar;;

	// private String updataStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (EdjApp) this.getApplication();

	}

	@Override
	protected void onPause() {

		if (EdjTools.isDebug)
			Log.i(TAG, "onPause");

		super.onPause();
		// MobclickAgent.onPause(this);
	}

	@Override
	protected void onRestart() {
		if (EdjTools.isDebug)
			Log.i(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		if (EdjTools.isDebug)
			Log.i(TAG, "onResume");

		AutoUpdater.CheckForUpdate(this, true);

		super.onResume();
		// MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		if (EdjTools.isDebug)
			Log.i(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		if (EdjTools.isDebug)
			Log.i(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (EdjTools.isDebug)
			Log.i(TAG, "onDestroy");

		super.onDestroy();
	}

}
