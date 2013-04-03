package com.aline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aline.app.App;
import com.aline.util.Tools;

/**
 * maq 2013.04.03
 * 信息 
 */
public class NewsActivity extends Activity {

	// private AsyncImageLoader loader = new AsyncImageLoader();
	private List<Map<String, Object>> mData;
	private Map<String, Object> map;
	private ListView listView;
	MyAdapter adapter ;
	App app;
	Button left_btn;
	Button right_btn;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (App) this.getApplication();
		mData = getData();
//		app.GetInitMapDriverData(mData);
		setContentView(R.layout.vlist);
		left_btn = (Button) this.findViewById(R.id.butt_left);
		right_btn = (Button) this.findViewById(R.id.butt_right);
		
		listView = (ListView) this.findViewById(R.id.list1);
		adapter = new MyAdapter(this);
		listView.setAdapter(adapter);
		
		left_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Tools.recomment(NewsActivity.this, app);
			}
		});
		right_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent nIntent = new Intent(NewsActivity.this, MainTabActivity.class);
				startActivity(nIntent);
				finish();
			}
		});
//		setListAdapter(adapter); 
	}


	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		 if(Tools.isConNetwork(NewsActivity.this)){
		    	return list;
		    }
		 return null;
//		 String url="http://dj.95081.com/c/invok/mapList?lon="+app.lon+"&lat="+app.lat+"&city="+app.city+"&currentpage=1&pagenum=10";
//		String r=HttpUtil.get(url,"hhh", "utf-8");
//		
//		try {
//			JSONArray arr = new JSONArray(r);
//			int len = arr.length();
//
//			new HashSet<String>();
//
//			
//			for (int i = 0; i <len; i++) {
//					JSONObject obj = arr.getJSONObject(i);
//					String city=obj.getString("city");
//					
//					map.put("mobile", mobile);
//					
//					list.add(map);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return list;
	}

	/**
	 * listview中点击按键弹出对话框
	 */
	public void showInfo(){
		new AlertDialog.Builder(this)
		.setTitle("我的listview")
		.setMessage("介绍...")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
		
	}
	

	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		if(Tools.isDebug)
		System.out.println("main onrestart");
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(Tools.isDebug)
		System.out.println("main onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(Tools.isDebug)
		System.out.println("main onPause");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(Tools.isDebug)
		System.out.println("main onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(Tools.isDebug)
		System.out.println("main onDestroy");
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if(Tools.isDebug)
		System.out.println("main onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			 Tools.appExit(this);		
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public final class ViewHolder{
		public TextView name;
		public TextView time;
		public TextView msg;
	
	}
	
	
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
//			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.msg_list_item, null);
			
				holder.name = (TextView)convertView.findViewById(R.id.name);
				holder.time = (TextView)convertView.findViewById(R.id.time);
				holder.msg = (TextView)convertView.findViewById(R.id.msg);
				
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
//			final String pic=(String)mData.get(position).get("img");
//			System.out.println(pic+"........"+pic.length());
//			
//				
//			final String c=(String)mData.get(position).get("workStatus");
//			
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent1=new Intent();
//					intent1.putExtra("name", name);
//					System.out.println(name);
					intent1.setClass(NewsActivity.this, MsgInfoActivity.class);
					startActivity(intent1);
					
				}
			});
			return convertView;
		}
		
	}
	
	
}
