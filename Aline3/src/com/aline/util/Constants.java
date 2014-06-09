package com.aline.util;

public class Constants {
	/**
	 * 获得地理位置失败  手机不支持基站定位
	 */
	public static final String INTENT_ACTION_GET_LOCATION_FAILED= "com.aline.app.get.failed";
	
	public static final String LBS_INTENT_ACTION_NETWORK_LOCATION_FAILED= "com.aline.app.network_lbs_.failed";
	/**
	 * 获得地理位置成功
	 */
	public static final String INTENT_ACTION_GET_LOCATION_OK= "com.aline.app.get.ok";
	
	/**
	 * Location Data Key
	 */
	public static final String EXTRA_LOCATION_KEY = "location";
	
	public static final String EXTRA_LOCATION_CITY = "location.city";
	
	//................................................................
	
	
	public static final String PUSH_ADD_FRIENDS="add.frieds";
	
	public static final String PUSH_FRINEDS_SUCCESS="push.success";
	
	public static final String PUSH_FRINEDS_FAILED="push.failed";

	public static final String CONNECTIVITY_CHANGE_ACTION = "net_change";

	public static final String INTENT_ACTION_GET_ISYS = "get_isys";

}
