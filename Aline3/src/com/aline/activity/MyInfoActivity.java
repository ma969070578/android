package com.aline.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView=new TextView(this);
		textView.setText("这是我的资料！");
		setContentView(textView);
	}
	
}
