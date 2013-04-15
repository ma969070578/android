package com.aline.activity;

import com.aline.util.PushMsgUtil;
import com.aline.util.Tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends Activity {
	private String msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search);
		
		EditText editText=(EditText)this.findViewById(R.id.mymsg);
		Button	 subBtn=(Button)this.findViewById(R.id.button1);
		
		final String content=editText.getText().toString();
	    msg = "{\"icon\":\"来点外卖\",\"n_content\":\"你好\"}";
	    
	 
	   
	    
		subBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				   msg=Tools.getMsgContent(null, "1111111",content,null).toString();
				   msg = "{\"n_title\":\"111111\",\"n_content\":\"你好\"}";
				PushMsgUtil.pushMsg(msg);	
			}
		});
   }
}