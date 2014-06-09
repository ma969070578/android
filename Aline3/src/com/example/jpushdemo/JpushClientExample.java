package com.example.jpushdemo;




import java.util.HashMap;
import java.util.Map;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.IOSExtra;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JpushClientExample {

	private static final String appKey ="a59219778be8585b98c730b8";//"57b9ef19d4be5de08df12aa0";	//必填，例如466f7032ac604e02fb7bda89
	
	private static final String masterSecret = "e8b968b25c853df55e0e88a5";//"13ac09b17715bd117163d8a1";//必填，每个应用都对应一个masterSecret
	
	private static JPushClient jpush = null;

	/*
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。
	 * 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒）。	
	 */
	private static long timeToLive =  0;//60 * 60 * 24;  

	public static String sendMsg(String msgTitle,String alias,String msgContext){
		if(jpush == null){
			jpush = new JPushClient(masterSecret, appKey, timeToLive);
		}
		int sendNo = getRandomSendNo();
		//MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo, msgTitle, msgContext);
		MessageResult msgResult =jpush.sendCustomMessageWithAlias(sendNo,alias,msgTitle,msgContext);
		if (null != msgResult) {
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("发送成功， sendNo=" + msgResult.getSendno());
				return "success";
			} else {
				System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
				return "error";
			}
		} else {
			return "notFindReturnData";
		}
	}
	
	
	
	public static void testSend() {
	    // 在实际业务中，建议 sendNo 是一个你自己的业务可以处理的一个自增数字。
	    // 除非需要覆盖，请确保不要重复使用。详情请参考 API 文档相关说明。
		
		
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
		
		
	    int sendNo = getRandomSendNo();
		String msgTitle = "+;//jpush\"\"";
		String msgContent = "\\&;w\"\"a";
		
		/*
		 * IOS设备扩展参数,
		 * 设置badge，设置声音
		 */

//		Map<String, Object> extra = new HashMap<String, Object>();
//		IOSExtra iosExtra = new IOSExtra(10, "WindowsLogonSound.wav");
//		extra.put("ios", iosExtra);
//		MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo, msgTitle, mss);

		//MessageResult msgResult =jpush.sendCustomMessageWithAlias(sendNo,null,"订单消息","{\"a\":\"213\"}");
		//对所有用户发送通知, 更多方法请参考文档
		MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo,msgTitle, msgContent);
		
		if (null != msgResult) {
			System.out.println("服务器返回数据: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("发送成功， sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("无法获取数据");
		}
		
		
	}
	
	public static void main(String[] args) {
		/*
		 * Example1: 初始化,默认发送给android和ios，同时设置离线消息存活时间
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive);
		 * 
		 * Example2: 只发送给android
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.Android);
		 * 
		 * Example3: 只发送给IOS
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.IOS);
		 * 
		 * Example4: 只发送给android,同时设置离线消息存活时间
		 * jpush = new JPushClient(masterSecret, appKey, timeToLive, DeviceEnum.Android);
		 */
		jpush = new JPushClient(masterSecret, appKey, timeToLive);

		/* 
		 * 是否启用ssl安全连接, 可选
		 * 参数：启用true， 禁用false，默认为非ssl连接
		 */
		//jpush.setEnableSSL(true);

		//测试发送消息或者通知
		testSend();
	}
	
    public static final int MAX = Integer.MAX_VALUE;
	public static final int MIN = (int) MAX/2;
	
	/**
	 * 保持 sendNo 的唯一性是有必要的
	 * It is very important to keep sendNo unique.
	 * @return sendNo
	 */
	public static int getRandomSendNo() {
	    return (int) (MIN + Math.random() * (MAX - MIN));
	}
	
}
