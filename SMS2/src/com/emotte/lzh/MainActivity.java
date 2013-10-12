package com.emotte.lzh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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

import com.emotte.data.ServiceInfo;
import com.emotte.util.Constants;
import com.emotte.util.EdjTools;
import com.emotte.util.SharedPreTools;

public class MainActivity extends Activity {

	private ListView mList;
	private MyAdapter adapter;
	
	private Button fail;
	private Button success;
	private Button nouser;
	
    private int  type= 0;  //0失败 1成功 2无效
	private List<ServiceInfo> map_list = new ArrayList<ServiceInfo>();
	private List<ServiceInfo> success_list = new ArrayList<ServiceInfo>();
	private List<ServiceInfo> fail_list = new ArrayList<ServiceInfo>();
	private List<ServiceInfo> nouser_list = new ArrayList<ServiceInfo>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		fail = (Button) findViewById(R.id.fail);
		success = (Button) findViewById(R.id.success);
		nouser = (Button) findViewById(R.id.nouser);

		boolean b = isConnectInternet(); 
		if (!b) { Toast.makeText(MainActivity.this,
				"您暂时没有可用的网络,请检查网络",
				Toast.LENGTH_SHORT).show();}
		EdjApp.getInstance().iscon=b;
		
		fail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				success.setBackgroundResource(R.drawable.jz_nocheck);
				nouser.setBackgroundResource(R.drawable.jz_nocheck);
				fail.setBackgroundResource(R.drawable.jz_check);
				
				type=0;
				EdjTools.saveotherService(MainActivity.this);
				new GetCallLogAsyncTask().execute("");
			
			}
		});

		success.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				EdjTools.saveotherService(MainActivity.this);
				success.setBackgroundResource(R.drawable.jz_check);
				nouser.setBackgroundResource(R.drawable.jz_nocheck);
				fail.setBackgroundResource(R.drawable.jz_nocheck);
				
				type=1;
				new GetCallLogAsyncTask().execute("");
				
			}
		});
		
		
		nouser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				success.setBackgroundResource(R.drawable.jz_nocheck);
				fail.setBackgroundResource(R.drawable.jz_nocheck);
				nouser.setBackgroundResource(R.drawable.jz_check);
				
				type=2;
			}
		});

		mList = (ListView) findViewById(R.id.list1);
		adapter = new MyAdapter(MainActivity.this);
		mList.setAdapter(adapter);
		
		

	}

	private List<ServiceInfo> getDataSource1() {
		success_list.clear();
		
		success_list=SharedPreTools.readAllObjectPre(Constants.SUCCESS_DATA);
	
		return success_list;

	}
	
	
	private List<ServiceInfo> getDataSource0() {
		fail_list.clear();
		
		fail_list=SharedPreTools.readAllObjectPre(Constants.FAIL_DATA);
	
		return fail_list;

	}
	
	private List<ServiceInfo> getDataSource2() {
		nouser_list.clear();
		
		nouser_list=SharedPreTools.readAllObjectPre(Constants.NOUSER_DATA);
	
		return nouser_list;

	}
	


	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				List<ServiceInfo> mlist=(List<ServiceInfo>) msg.obj;
				if(map_list.size()>0)
					map_list.clear();
				if(mlist!=null&&mlist.size()>0){
					map_list.addAll(mlist);
			    }
				adapter.notifyDataSetChanged();
				
				break;
			case 2:
				map_list.clear();
				adapter.notifyDataSetChanged();
				Toast.makeText(MainActivity.this, "无数据", Toast.LENGTH_SHORT)
				.show();
				break;
			}
		}
	};

	public final class ViewHolder {

		public TextView title;
		public TextView content;
		public TextView time;

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
				holder.time = (TextView) convertView
				.findViewById(R.id.item_time);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			if (map_list != null && map_list.size() > 0) {

				final String a = map_list.get(position).getNumber();
				final String b = (String) map_list.get(position).getContent();
			    Date mdate=	map_list.get(position).getCreateTime();
			    String c = "" ;
			    if(mdate!=null){
			    c=	EdjTools.dateToStringSS(mdate);
			    }
				holder.title.setText("电话：" + a);
				holder.content.setText("验证码：" + b);
				holder.time.setText("创建时间：" + c);

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
			
            
			List<ServiceInfo> temp_map_list=new ArrayList<ServiceInfo>();
			if(type==0){
				temp_map_list=getDataSource0();
			}else if(type==1){
				temp_map_list=getDataSource1();
			}else if(type==2){
				temp_map_list=getDataSource2();
			}
			
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
	
	
	

 public boolean isConnectInternet() { 
	 boolean netSataus = false; 
	 ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); 
	 NetworkInfo networkInfo = conManager.getActiveNetworkInfo(); if (networkInfo != null) 
	 { // 注意，这个判断一定要的哦，要不然会出错     n
		netSataus = networkInfo.isAvailable();   
		 } return netSataus; 
	} 

}
