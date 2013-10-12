package com.emotte.lzh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emotte.util.EdjTools;

public class MainActivity extends Activity {

	private ListView mList;
	private MyAdapter adapter;
	private Button upload;
	private Button noupload;
    private Boolean  isupload=false;
	private List<Map<String, Object>> map_list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> map_list1 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> map_list2 = new ArrayList<Map<String, Object>>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		noupload = (Button) findViewById(R.id.noupload);
		upload = (Button) findViewById(R.id.upload);

		boolean b = isConnectInternet(); 
		if (!b) { Toast.makeText(MainActivity.this,
				"您暂时没有可用的网络,请检查网络",
				Toast.LENGTH_SHORT).show();}
		EdjApp.getInstance().iscon=b;
		
		noupload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				upload.setBackgroundResource(R.drawable.jz_nocheck);
				noupload.setBackgroundResource(R.drawable.jz_check);
				
				isupload=false;
				EdjTools.saveotherService(MainActivity.this);
				new GetCallLogAsyncTask().execute("");
				Message m = mHandler.obtainMessage();
				m.what = 1;
				MainActivity.this.mHandler.sendMessage(m);
			
			}
		});

		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				EdjTools.saveotherService(MainActivity.this);
				upload.setBackgroundResource(R.drawable.jz_check);
				noupload.setBackgroundResource(R.drawable.jz_nocheck);
				
				isupload=true;
				new GetCallLogAsyncTask2().execute("");
				
			}
		});

		mList = (ListView) findViewById(R.id.list1);
		adapter = new MyAdapter(MainActivity.this);
		mList.setAdapter(adapter);
		
		

	}

	private List<Map<String, Object>> getDataSource1() {
		map_list1.clear();
		SharedPreferences call = MainActivity.this.getApplicationContext()
				.getSharedPreferences("call_info", 0);
		String group = call.getString("group", "");

		// 获取未上传成功的 组id 只上传一个
		if (group != null && !"".equals(group)) {
			String[] ss = group.split(",");
			for (int i = 0; i < ss.length; i++) {
				String mobile = ss[i];
				String number = EdjTools.readInfo(mobile,
						MainActivity.this.getApplicationContext());
				if (mobile != null && number != null && mobile.contains("no")) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("mobile", mobile.replaceAll("no", "").trim());
					map1.put("number", number);
					map_list1.add(map1);
				} 
				
			}
	
		}
		return map_list1;

	}
	
	
	
	private List<Map<String, Object>> getDataSource2() {
		
		map_list2.clear();
		SharedPreferences call = MainActivity.this.getApplicationContext()
				.getSharedPreferences("call_info", 0);
		String group = call.getString("group", "");

		// 获取未上传成功的 组id 只上传一个
		if (group != null && !"".equals(group)) {
			String[] ss = group.split(",");
			for (int i = 0; i < ss.length; i++) {
				String mobile = ss[i];
				String number = EdjTools.readInfo(mobile,
						MainActivity.this.getApplicationContext());
			 if (mobile != null && number != null&& mobile.contains("yes")) {
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("mobile", mobile.replaceAll("yes", "").trim());
					map2.put("number", number);
					map_list2.add(map2);
				}
				
			  }
			}
				return map_list2;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				List<Map<String, Object>> mlist=(List<Map<String, Object>>) msg.obj;
				if(map_list.size()>0)
					map_list.clear();
				if(mlist!=null&&mlist.size()>0){
					map_list.addAll(mlist);
			    }
				adapter.notifyDataSetChanged();
				
				break;
			case 2:
				Toast.makeText(MainActivity.this, "无数据", Toast.LENGTH_SHORT)
				.show();
				break;
			}
		}
	};

	public final class ViewHolder {

		public TextView title;
		public TextView content;

	}

	class MyAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		public MyAdapter(Context context) {
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {

			if (map_list == null && map_list.size() <= 0) {
				return 0;
			} else {
				return map_list.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = (View) layoutInflater.inflate(R.layout.list_item,
						null);
				holder.title = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.content = (TextView) convertView
						.findViewById(R.id.item_content);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			if (map_list != null && map_list.size() > 0) {

				final String a = (String) map_list.get(position).get("mobile");
				final String b = (String) map_list.get(position).get("number");

				holder.title.setText("电话：" + a);
				holder.content.setText("验证码：" + b);

			}
			return convertView;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("onResume");
		// EdjTools.saveotherService(MainActivity.this);
		super.onResume();
	}
	
	
	//---------------------------------------

	class GetCallLogAsyncTask extends AsyncTask<String, String, String> {

		public GetCallLogAsyncTask() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... params) {

			List<Map<String, Object>> temp_map_list=getDataSource1();
			System.out.println("1111111111111----------");
			if(temp_map_list!=null&&temp_map_list.size()>0){
				System.out.println("22222222222221----------");
				Message m = mHandler.obtainMessage();
				m.what = 1;
				m.obj=temp_map_list;
				MainActivity.this.mHandler.sendMessage(m);
			}else{
				System.out.println("33333333----------");
				Message m = mHandler.obtainMessage();
				m.what = 2;
//				m.obj=temp_map_list;
				MainActivity.this.mHandler.sendMessage(m);
			}
			
			return null;

		}
	}
	
	
	
	//---------------------------------------
	class GetCallLogAsyncTask2 extends AsyncTask<String, String, String> {
	public GetCallLogAsyncTask2() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... params) {

		List<Map<String, Object>> temp_map_list=getDataSource2();
		System.out.println("1111111111111----------");
		if(temp_map_list!=null&&temp_map_list.size()>0){
			System.out.println("22222222222221----------");
			Message m = mHandler.obtainMessage();
			m.what = 1;
			m.obj=temp_map_list;
			MainActivity.this.mHandler.sendMessage(m);
		}else{
			System.out.println("33333333----------");
			Message m = mHandler.obtainMessage();
			m.what = 2;
//			m.obj=temp_map_list;
			MainActivity.this.mHandler.sendMessage(m);
		}
		
		return null;
	}

	}
	
	

 public boolean isConnectInternet() { 
	 boolean netSataus = false; 
	 ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); 
	 NetworkInfo networkInfo = conManager.getActiveNetworkInfo(); if (networkInfo != null) 
	 { // 注意，这个判断一定要的哦，要不然会出错     n
		netSataus = networkInfo.isAvailable();   
		 } return netSataus; 
	} 

}
