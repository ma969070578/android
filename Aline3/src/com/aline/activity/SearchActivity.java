package com.aline.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView=new TextView(this);
		textView.setText("这是搜索！");
		setContentView(textView);
	}
	
}
