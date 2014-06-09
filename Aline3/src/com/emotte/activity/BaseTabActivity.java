package com.emotte.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.aline.app.EdjApp;
import com.umeng.analytics.MobclickAgent;

public class BaseTabActivity extends TabActivity{
	public TabHost tHost;
	public EdjApp app;
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
		app =(EdjApp) this.getApplication();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		  MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onResume(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	}
//	/**
//	 * 需要各个子类重写的方法
//	 * @param id
//	 */
//	public abstract void mainTabItemBackground(int id);
//	public abstract void updateTabBackground(TabHost tabHost);
//	public void keyReturnLogic(){
//		tHost.setCurrentTab(0);
//		mainTabItemBackground(0);
//		updateTabBackground(tHost);
//	}
    
}
