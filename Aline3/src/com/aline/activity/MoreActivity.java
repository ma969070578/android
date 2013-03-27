package com.aline.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView=new TextView(this);
		textView.setText("这是更多！");
		setContentView(textView);
	}
	
}
