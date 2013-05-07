package com.aline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.aline.util.Tools;

public class MyInfoActivity extends Activity {

	private static final String LOGTAG = LogUtil.makeLogTag(PushMsgUtil.class);

	private CornerListView mListView1 = null;
	private List<Map<String, Object>> map_list1 = null;
	private Button btn_back;
	private TextView title;
	private SimpleAdapter adapter1;
	public ProgressDialog progress;
	public ProgressDialog pBar;
	private String msex;
	App app;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);
		app = App.getInstance();
		title = (TextView) findViewById(R.id.title);
		title.setText(getResources().getString(R.string.myinfo));

		// btn_back = (Button) findViewById(R.id.butt_left);
		// btn_back.setVisibility(View.VISIBLE);
		// btn_back.setText(getResources().getString(R.string.back));
		//
		// btn_back.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // finish();
		// }
		// });

		mListView1 = (CornerListView) findViewById(R.id.set_list);
		getDataSource1();
		showview();
//		adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
//				R.layout.simple_list_item_1, new String[] { "text", "img" },
//				new int[] { R.id.item_title, R.id.item_content });
//		mListView1.setAdapter(adapter1);
//
//		CornerListView.setListViewHeightBasedOnChildren(mListView1);
//		mListView1.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				if (arg2 == 0) {
//					Log.d(LOGTAG, "00000000000");
//					System.out.println("0");
//					Intent intent1 = new Intent();
//					intent1.setClass(MyInfoActivity.this,
//							EditTextActivity.class);
//					startActivity(intent1);
//				} else if (arg2 == 1) {
//					Intent intent1 = new Intent();
//					intent1.setClass(MyInfoActivity.this, MaxCardActivity.class);
//					startActivity(intent1);
//				} else if (arg2 == 2) {
//					System.out.println("2");
//				} else if (arg2 == 31) {
//					System.out.println("3");
//					// 地区选择
//				} else if (arg2 == 4) {
//					Intent i4 = new Intent();
//					i4.setClass(MyInfoActivity.this,
//							City_SelectionActivity.class);
//					startActivity(i4);
//					System.out.println("4");
//				}
//			}
//		});

	}
	
	
	public  void showview(){
		adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
				R.layout.simple_list_item_1, new String[] { "text", "img" },
				new int[] { R.id.item_title, R.id.item_content });
		mListView1.setAdapter(adapter1);

		CornerListView.setListViewHeightBasedOnChildren(mListView1);
		mListView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					Log.d(LOGTAG, "00000000000");
					System.out.println("0");
					Intent intent1 = new Intent();
					intent1.setClass(MyInfoActivity.this,
							EditTextActivity.class);
					startActivity(intent1);
				} else if (arg2 == 1) {
					selectsex();
//					Intent intent1 = new Intent();
//					intent1.setClass(MyInfoActivity.this, SelectSexActivity.class);
//					startActivity(intent1);
				} else if (arg2 == 2) {
					System.out.println("2");
				} else if (arg2 == 3) {
					Intent intent1 = new Intent();
					intent1.setClass(MyInfoActivity.this, MaxCardActivity.class);
					startActivity(intent1);
				} else if (arg2 == 4) {
					Intent i4 = new Intent();
					i4.setClass(MyInfoActivity.this,
							City_SelectionActivity.class);
					startActivity(i4);
					System.out.println("4");
				}
			}
		});
	}

	// public final class ViewHolder {
	//
	// public TextView titile1;
	// public TextView content1;
	//
	// }
	//
	// public class MyAdapter extends BaseAdapter {
	// private LayoutInflater mInflater;
	//
	// public MyAdapter(Context context) {
	// this.mInflater = LayoutInflater.from(context);
	// }
	//
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// if (map_list1 == null || map_list1.size() == 0) {
	// return 0;
	// }
	// return map_list1.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	//
	// ViewHolder holder = null;
	// if (convertView == null) {
	//
	// holder = new ViewHolder();
	// convertView = mInflater.inflate(R.layout.simple_list_item_1,
	// null);
	// CornerListView.setListViewHeightBasedOnChildren(mListView1);
	//
	// holder.titile1 = (TextView) convertView
	// .findViewById(R.id.item_title);
	// holder.content1 = (TextView) convertView
	// .findViewById(R.id.item_content);
	//
	// String tt = (String) map_list1.get(position).get("text");
	// String cc = (String) map_list1.get(position).get("img");
	//
	// holder.titile1.setText(tt);
	// holder.content1.setText(cc);
	//
	// Intent intent1 = new Intent();
	// if (position == 0) {
	//
	// intent1.setClass(MyInfoActivity.this,
	// EditTextActivity.class);
	//
	// } else if (position == 1) {
	//
	// intent1.setClass(MyInfoActivity.this, MaxCardActivity.class);
	//
	// } else if (position == 2) {
	//
	// } else if (position == 31) {
	//
	// } else if (position == 4) {
	//
	// intent1.setClass(MyInfoActivity.this,
	// City_SelectionActivity.class);
	//
	// }
	//
	// final Intent intent2=intent1;
	//
	// convertView.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// startActivity(intent2);
	// }
	//
	// });
	// }
	// return convertView;
	// }
	// };
	//
	// private Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 1:
	// map_list1=(List<Map<String, Object>>) msg.obj;
	// adapter1.notifyDataSetChanged();
	// mListView1.invalidateViews();
	//
	// break;
	// }
	// }
	// };
	
	private void selectsex(){
		new AlertDialog.Builder(this).setTitle("性别").setIcon(
			     android.R.drawable.ic_dialog_info).setSingleChoiceItems(
			     new String[] { "男", "女" }, 0,
			     new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  switch (which) {
					case 0:
						msex="男";
						break;
					case 1:
						msex="女";
						break;	
					}
			    	Tools.writeShare(MyInfoActivity.this, "user_sex", msex);
			        dialog.dismiss();
			        getDataSource1();
					Message m = mHandler.obtainMessage();
					m.what = 1;
					MyInfoActivity.this.mHandler.sendMessage(m);
					
			      }
			     }) .setNegativeButton("取消", null).show();


	}

	private List<Map<String, Object>> getDataSource1() {
		map_list1 = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("text", getResources().getString(R.string.user_name));
		String name = Tools.readShare(MyInfoActivity.this, "user_name");
		if (Tools.isNull(name)) {
			name = "请编辑";
		}
		map.put("img", name);
		map_list1.add(map);

		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.user_sex));
		String sex = Tools.readShare(MyInfoActivity.this, "user_sex");
		if (Tools.isNull(sex)) {
			sex = "请选择";
		}
		map.put("img", sex);
		map_list1.add(map);

		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.user_imei));
		map.put("img", "1");
		map_list1.add(map);

		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.user_card));
		map.put("img", "1");
		map_list1.add(map);

		map = new HashMap<String, Object>();
		map.put("text", getResources().getString(R.string.user_address));
		String address = Tools.readShare(MyInfoActivity.this, "user_address");
		if (Tools.isNull(address)) {
			address = "请选择";
		}
		map.put("img", address);
		map_list1.add(map);

		return map_list1;
	}

	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				showview();
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getDataSource1();
		Message m = mHandler.obtainMessage();
		m.what = 1;
		MyInfoActivity.this.mHandler.sendMessage(m);
		System.out.println(1111111+"1111111");
//		this.finish();
//		startActivity(new Intent(MyInfoActivity.this,MyInfoActivity.class));
		
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}