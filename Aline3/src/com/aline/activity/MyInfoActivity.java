package com.aline.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aline.app.App;
import com.aline.ui.CornerListView;
import com.aline.util.LogUtil;
import com.aline.util.PushMsgUtil;

public class MyInfoActivity extends Activity {

	private static final String LOGTAG = LogUtil.makeLogTag(PushMsgUtil.class);

	private CornerListView mListView1 = null;
	private ArrayList<HashMap<String, String>> map_list1 = null;;
	private Button btn_back;
	private TextView title;
	private SimpleAdapter adapter1;
	public ProgressDialog progress;
	public ProgressDialog pBar;
	App app;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);
		app = App.getInstance();
		title = (TextView) findViewById(R.id.title);
		title.setText(getResources().getString(R.string.more));

		btn_back = (Button) findViewById(R.id.butt_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setText(getResources().getString(R.string.back));

		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// finish();
			}
		});

		mListView1 = (CornerListView) findViewById(R.id.set_list);
		getDataSource1();

		adapter1 = new SimpleAdapter(getApplicationContext(),
				map_list1, R.layout.simple_list_item_1,
				new String[] { "item" }, new int[] { R.id.item_title });
		mListView1.setAdapter(adapter1);

		CornerListView.setListViewHeightBasedOnChildren(mListView1);
		mListView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					Log.d(LOGTAG, "00000000000");
					System.out.println("0");
				} else if (arg2 == 1) {
					System.out.println("1");
				} else if (arg2 == 2) {
					System.out.println("2");
				} else if (arg2 == 31) {
					System.out.println("3");
					//地区选择
				} else if (arg2 == 4) {
					Intent i4=new Intent();
					i4.setClass(MyInfoActivity.this, City_SelectionActivity.class);
					startActivity(i4);
					System.out.println("4");
				} 
			}
		});

	}

	public ArrayList<HashMap<String, String>> getDataSource1() {

		map_list1 = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		HashMap<String, String> map3 = new HashMap<String, String>();
		HashMap<String, String> map4 = new HashMap<String, String>();
		HashMap<String, String> map5 = new HashMap<String, String>();

		map1.put("item", "名字");
		map2.put("item", "我的IMEI");
		map3.put("item", "二维码名片");
		map4.put("item", "性别");
		map5.put("item", "地区");

		map_list1.add(map1);
		map_list1.add(map2);
		map_list1.add(map3);
		map_list1.add(map4);
		map_list1.add(map5);

		return map_list1;
	}
}