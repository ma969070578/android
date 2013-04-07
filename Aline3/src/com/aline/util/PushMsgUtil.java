package com.aline.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import cn.jpush.api.StringUtils;

/**
 * 调用远程api实现推送
 * @author naiyu
 *
 */

//http://blog.csdn.net/heynine/article/details/8140000
public class PushMsgUtil {
	private static final String LOGTAG = LogUtil.makeLogTag(PushMsgUtil.class);
	
//	public static final String PUSH_URL = "https://api.jpush.cn:443/sendmsg/sendmsg";
	public static final String PUSH_URL = "http://api.jpush.cn:8800/sendmsg/sendmsg";
	
	public static void pushMsg(String msg) {
		BasicNameValuePair name = new BasicNameValuePair("username", Tools.pushusername);  //用户名
		BasicNameValuePair sendno = new BasicNameValuePair("sendno", "3621");  // 发送编号。由开发者自己维护，标识一次发送请求
		BasicNameValuePair appkeys = new BasicNameValuePair("appkeys", Tools.AppKey);  // 待发送的应用程序(appKey)，只能填一个。
		BasicNameValuePair receiver_type = new BasicNameValuePair("receiver_type", "4");  
		//验证串，用于校验发送的合法性。
		BasicNameValuePair verification_code = new BasicNameValuePair("verification_code", getVerificationCode());
		//发送消息的类型：1 通知 2 自定义
		BasicNameValuePair msg_type = new BasicNameValuePair("msg_type", "1");
		BasicNameValuePair msg_content = new BasicNameValuePair("msg_content", msg);
		//目标用户终端手机的平台类型，如： android, ios 多个请使用逗号分隔。
		BasicNameValuePair platform = new BasicNameValuePair("platform", "android");
		List<BasicNameValuePair> datas = new ArrayList<BasicNameValuePair>();
		datas.add(name);
		datas.add(sendno);
		datas.add(appkeys);
		datas.add(receiver_type);
		datas.add(verification_code);
		datas.add(msg_type);
		datas.add(msg_content);
		datas.add(platform);
		
		Log.d(LOGTAG, "pushMsg"+datas.toString()); 
		try {
			HttpEntity entity = new UrlEncodedFormEntity(datas, "utf-8");
			HttpPost post = new HttpPost(PUSH_URL);
			post.setEntity(entity);
			HttpClient client = ClientUtil.getNewHttpClient();
			HttpResponse reponse = client.execute(post);
			HttpEntity resEntity = reponse.getEntity();
			System.out.println(EntityUtils.toString(resEntity));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
	private static String getVerificationCode() {

		String username = Tools.pushusername;  //username 是开发者Portal帐户的登录帐户名
		String password = Tools.pushpasswd;
		int sendno = 3621;
		int receiverType = 4;
		String md5Password = StringUtils.toMD5(password);; //password 是开发者Portal帐户的登录密码
		 
		String input = username + sendno + receiverType + md5Password;
		String verificationCode = StringUtils.toMD5(input);
		return verificationCode;
	}
	
	public static void main(String[] args) {
		String msg = "{\"n_title\":\"来点外卖\",\"n_content\":\"你好\"}";
		System.out.println(msg);
		PushMsgUtil.pushMsg(msg);
	}

}