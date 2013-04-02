package com.aline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aline.app.App;
import com.aline.download.AutoUpdater;
import com.aline.ui.CornerListView;
import com.aline.util.Tools;
import com.example.jpushdemo.MainActivity;

public class MoreActivity extends Activity {

	private CornerListView mListView1 = null;
	private List<Map<String, Object>> map_list1 = null;
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
		setContentView(R.layout.main_more);
		app=App.getInstance();
		title=(TextView) findViewById(R.id.title);
		title.setText(getResources().getString(R.string. more));
		
		btn_back = (Button) findViewById(R.id.butt_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setText(getResources().getString(R.string.back));

		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// finish();
			}
		});
	
		mListView1 = (CornerListView) findViewById(R.id. more_list);
		getDataSource1();
		adapter1= new SimpleAdapter(getApplicationContext(),
				map_list1, R.layout.simple_list_item_2, new String[] { "text",
						"img" }, new int[] { R.id.setting_list_item_text,
						R.id.setting_list_item_arrow });
		
		
		mListView1.setAdapter(adapter1);
		CornerListView.setListViewHeightBasedOnChildren(mListView1);
		mListView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				// howuse
				if (arg2 == 0) {
					System.out.println("1");
					Intent intent2 = new Intent();
					intent2.setClass( MoreActivity.this,  MainActivity.class);
					intent2.putExtra("appType", "jzb");
					startActivity(intent2);
				}
				// feedback
				else if (arg2 == 1) {
					Intent intent1 = new Intent();
					intent1.setClass( MoreActivity.this,  FeedBack.class);
					startActivity(intent1);
				}
				// checkupdate
				else if (arg2 == 2) {

					AutoUpdater.CheckForUpdate( MoreActivity.this,false);
				}
		
				else if (arg2 == 3) {
					Intent intent2 = new Intent();
					intent2.setClass( MoreActivity.this,  InfosActivity.class);
					startActivity(intent2);
				}

				else if (arg2 == 4) {
					Tools.recomment(MoreActivity.this, app);

				}

			}
		});

	}

	private List<Map<String, Object>> getDataSource1() {
		map_list1 = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("text", getResources().getString(R.string.howuse));
		map.put("img", R.drawable.dog_robot);
		map_list1.add(map);

		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.feedback));
		map.put("img", R.drawable.insect_robot);
		map_list1.add(map);

		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.checkupdate));
		map.put("img", R.drawable.scorpio_robot);
		map_list1.add(map);
		
		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.aboutus));
		map.put("img", R.drawable.paranoid_android);
		map_list1.add(map);
		
		
		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.sendtofriends));
		map.put("img", R.drawable.spider_robot);
		map_list1.add(map);
		
		return map_list1;
	}

	

	
	//返回按钮
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
		}
		return super.onKeyDown(keyCode, event);
	}

}
