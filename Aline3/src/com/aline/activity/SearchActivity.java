package com.aline.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aline.util.PushMsgUtil;
import com.aline.util.Tools;

public class SearchActivity extends Activity {
	private String msg;
	private String content;
	private EditText editText;
	private Button subBtn;
	private static final int SHOW_PROMPT_SUCCESS = 13;
	private static final int SHOW_PROMPT_FAIL = 14;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search);
		
		title=(TextView) findViewById(R.id.title);
		title.setText(getResources().getString(R.string.search));

		editText = (EditText) this.findViewById(R.id.mymsg);
		subBtn = (Button) this.findViewById(R.id.button1);
	
//		 msg = "{\"icon\":\"来点外卖\",\"n_content\":\"你好\"}";

		subBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				content = editText.getText().toString();
				Tools.hideSoftInput(SearchActivity.this, SearchActivity.this);
				msg = Tools.getMsgContent(null, getResources().getString(R.string.app_name), content, null)
						.toString();
				new pushAsyncTask(msg).execute("");
			}
		});
	}
	
	
	class pushAsyncTask extends AsyncTask<String, Integer, String> {

		public pushAsyncTask(String msg) {
			
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			int code= PushMsgUtil.pushMsg(msg);
			 
			if(code==0){
				Message m = mHandler.obtainMessage();
				m.what = SHOW_PROMPT_SUCCESS;
				SearchActivity.this.mHandler.sendMessage(m);
			}else {
				Message m = mHandler.obtainMessage();
				m.what = SHOW_PROMPT_FAIL;
				SearchActivity.this.mHandler.sendMessage(m);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}


	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_PROMPT_SUCCESS:
				Toast.makeText(SearchActivity.this, "发送成功！",
						Toast.LENGTH_SHORT).show();
				break;
			case SHOW_PROMPT_FAIL:
				Toast.makeText(SearchActivity.this,"发送失败，请重试！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}