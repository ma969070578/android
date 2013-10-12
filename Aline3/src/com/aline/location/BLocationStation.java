
package com.aline.location;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.aline.activity.R;
import com.aline.app.App;
import com.aline.util.Constants;
import com.aline.util.SystemOut;
import com.aline.util.Tools;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.BdLocator;
import com.baidu.mapapi.CoordinateConvert;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;

/**
 * @author Administrator
 *
 */
public class BLocationStation {
	private String TAG="BLocationStation";
	private static BLocationStation instance;
	public boolean isLocallocation;
	public LocationFinish locationFinish; 
	/**
	 * 百度定位用到的变量
	 */
	private LocationClient mLocationClient = null;

	
	private  MKSearch mkSearch;
    private Context tcontext;
	private Handler objHandler;
	private int REQUEST_FILED=10;
	private  final static int UNSUPPORT_NETWORK=11;
	private BMapManager mBMapMan;
	public String mStrKey = "4ABB85705129C07AD749199DA0241D6325A10474";
//	private LocationListener BLocationListener; 
	private MyLocationListenner BLocationListener;  
	private Timer bOuttime;//百度获取信息超时
	/**
	 * 定位返回结果相关
	 */
	public static final int location_return_sucess = 1;
	
	public static final int location_return_fail = 0;

     private int googleRrquestTime = 1000*30;
     private int  bOutTime= 1000*25;
//    private LocationResultInterface locationResultInterface;
	private  BLocationStation(Context context){
		  this.tcontext = App.getInstance();
		  Log.i(TAG, "init BLocationStation");
		  if(mBMapMan == null){
		     mBMapMan = new BMapManager(tcontext);
		     mBMapMan.init(mStrKey, new MyGeneralListener());
		  }
		  if(mkSearch == null){
			 mkSearch=new MKSearch();
			 mkSearch.init(mBMapMan, new MySearchListener());  
		  }
		 if(mLocationClient == null){ 
			  BLocationListener = new MyLocationListenner();;
			  registerLocationClientListener(BLocationListener);
			  setLocationOption(mLocationClient);
			  locationClientStart();
			  bTimeOut(bOutTime);
		 
		 }

		  
	}
	/**
	 * 设置定位返回结果后的回调
	 * @param locationFinish
	 */
	public void setFinish(LocationFinish locationFinish){
		this.locationFinish = locationFinish;
	}
	
	public void startGoogleLocation(){
//		  SystemOut.out("gLocationManager="+gLocationManager);
//		  gLocationManager = new GLocationManager(tcontext);
//		  if(gLocationManager != null){
//			 gLocationManager.registLocationListener();
//		  }
//		  gLocationManager.getLocation(googleRrquestTime, new LocationResult() {
//			
//			@Override
//			public void onLocationFinish(int statusCode, double lng, double lat,
//					String city,String street) {
//				// TODO Auto-generated method stub
//				SystemOut.out("google location: statusCode="+statusCode+":lat="+lat+":lng="+lng
//						+":street="+street);
//				isLocallocation=false;
//				unRegisterLocationListener();
//				if(statusCode == LocationResult.CODE_SUCCESS){
//					App.getInstance().city = city;
//					App.getInstance().street = street;
//					App.getInstance().lon=(int)lng;
//                    App.getInstance().lat=(int)lat;
//                    if(locationFinish != null){
//					   locationFinish.callBack(LocationFinish.SUCESS);
//                    }
//				}
//				else if(statusCode == LocationResult.CODE_FAILED){
//					 handler.sendEmptyMessage(REQUEST_FILED);
//					 if(locationFinish != null){
//		                locationFinish.callBack(LocationFinish.FAIL);
//					 }
//				}
//			}
//		  });
//		  gLocationManager.registLocationListener();
	}
	  private class MySearchListener implements MKSearchListener  {
	        
	        public void onGetAddrResult(MKAddrInfo result, int iError) { 
//	        	Log.i(TAG, "baidu location: statusCode="+iError);
	        	SystemOut.out("baidu location: iError="+iError);
//	        	iError = 1;
	        	
	        	unRegisterLocationListener();
	            if( iError != 0 || result == null){  
	            	SystemOut.out("baidu loaction fail");
//	            	startGoogleLocation();
	            	 handler.sendEmptyMessage(REQUEST_FILED);
					 if(locationFinish != null){
		                locationFinish.callBack(LocationFinish.FAIL);
					 }
	            }else {  
	            	 SystemOut.out("baidu loaction sucess");
//	            	unRegisterLocationListener();
	                App.getInstance().city =result.addressComponents.city;
	                App.getInstance().address=result.strAddr;
	                if(result.addressComponents.street==null||result.addressComponents.street.equals("")){
	                      App.getInstance().street =result.addressComponents.district;
	                }else{
	                	  App.getInstance().street =result.addressComponents.street;	
	                }
	                SystemOut.out("city111="+(result.addressComponents.city+result.addressComponents.street));
	                isLocallocation=false;
	                if(locationFinish != null){
	                   locationFinish.callBack(LocationFinish.SUCESS);
	                }
	            }  
	        }

			/* (non-Javadoc)
			 * @see com.baidu.mapapi.MKSearchListener#onGetBusDetailResult(com.baidu.mapapi.MKBusLineResult, int)
			 */
			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see com.baidu.mapapi.MKSearchListener#onGetDrivingRouteResult(com.baidu.mapapi.MKDrivingRouteResult, int)
			 */
			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see com.baidu.mapapi.MKSearchListener#onGetPoiResult(com.baidu.mapapi.MKPoiResult, int, int)
			 */
			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see com.baidu.mapapi.MKSearchListener#onGetSuggestionResult(com.baidu.mapapi.MKSuggestionResult, int)
			 */
			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see com.baidu.mapapi.MKSearchListener#onGetTransitRouteResult(com.baidu.mapapi.MKTransitRouteResult, int)
			 */
			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see com.baidu.mapapi.MKSearchListener#onGetWalkingRouteResult(com.baidu.mapapi.MKWalkingRouteResult, int)
			 */
			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}

			public void onGetRGCShareUrlResult(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}  

    }
	  
	  /** 注册百度定位事件 */  
	  
	
	    
	public void registerLocationListener() {
	  if(!Tools.isSupportLocation(tcontext)){
		  SystemOut.out("unsupport 提示");
		  handler.sendEmptyMessage(UNSUPPORT_NETWORK);
		  return;
	  }
//	  if(BLocationListener == null){
//		  BLocationListener = new MyLocationListner();
//		 if(mBMapMan!= null&&mBMapMan.getLocationManager().enableProvider((int) MKLocationManager.MK_GPS_PROVIDER)){
//	    	 mBMapMan.getLocationManager().requestLocationUpdates(BLocationListener);
//	    	 mBMapMan.getLocationManager().setNotifyInternal(8, 3);
//	    	 bTimeOut(bOutTime);
//	     }
//		
//	  }
	  

	  
	 
		 
	}
	
	// 设置相关参数
	private void setLocationOption(LocationClient mLocationClient  ) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all");
//		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
//		option.setPriority(LocationClientOption.GpsFirst); // 不设置，默认是gps优先
		option.setScanSpan(0);
		mLocationClient.setLocOption(option);
	}
		
    public void startManager(){
    	if (mBMapMan != null) {
			mBMapMan.start();
			
		}
    	SystemOut.out("start baidu loaction");
    	
    }
	public void unRegisterLocationListener() {
//		mBMapMan.getLocationManager().removeUpdates(
//				BLocationListener);
		
//		BLocationListener = null;
//		if(gLocationManager != null){
//			gLocationManager.removeLocationListener();
//		}
		locationClientStop();
		
	}
	
	public void registerLocationClientListener(BDLocationListener BLocationListener) {
		  mLocationClient = new LocationClient(tcontext);
		  mLocationClient.registerLocationListener(BLocationListener);
		  setLocationOption(mLocationClient);
		
	}
	
	
	public void locationClientStart(){
		if(mLocationClient!=null)
			mLocationClient.start();
	}
	
	public void locationClientStop(){
		if(mLocationClient!=null)
			mLocationClient.stop();
	}
	
	
	public void destroyManager(){
		if(mBMapMan != null){
			unRegisterLocationListener();
			mBMapMan.destroy();
			
		}
	}
	
//	private Runnable timeTask = new Runnable() {
//		public void run() {
//					try{
//						isLocallocation=false;
//						handler.sendEmptyMessage(REQUEST_FILED);
////						 Intent intent = new Intent(
////									Constants.B_INTENT_ACTION_GET_LOCATION_FAILED);
////							App.getInstance().sendBroadcast(intent);
//						if(locationFinish != null){
//							locationFinish.callBack(LocationFinish.FAIL);
//						}
//						unRegisterLocationListener();
//						SystemOut.Out("baidu location outtime");
//
//					}catch (Exception e) {
//						// TODO: handle exception
//					}
//				
//		}
//	};
	public void bTimeOut(long outTime) {
		if (bOuttime != null) {
			try {
				bOuttime.cancel();
			} catch (Exception e) {

			}
			bOuttime = null;
		}
		bOuttime = new Timer();
		bOuttime.schedule(new TimerTask() {
			public void run() {
				isLocallocation=false;
				handler.sendEmptyMessage(REQUEST_FILED);
				if(locationFinish != null){
					locationFinish.callBack(LocationFinish.FAIL);
				}
				unRegisterLocationListener();
				SystemOut.out("baidu location outtime");
			}
		}, outTime);
	}

	public void bTimeCancel() {
		if (bOuttime != null) {
			try {
				bOuttime.cancel();
			} catch (Exception e) {

			}
			bOuttime = null;
		}
	}
	
	 Handler handler=new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==REQUEST_FILED){
//					if(Tools.CURRENTTYPE==1){
						Toast.makeText(tcontext, R.string.location_fail_show_default_info, Toast.LENGTH_LONG).show(); 
//					}else{
//						Toast.makeText(tcontext, R.string.location_fail, Toast.LENGTH_LONG).show(); 
//					}
					 
					try{
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//192.168.1.39:8080
//							String url="http://"+Tools.HOST+"/c/invok/saveerror";
//							HashMap<String, String> map=new HashMap<String, String>();
//							map.put("userkey",App.getInstance().userKey);
//							////8集成版本  9.代驾宝 0 家政宝 6.健康宝，7定位宝
//							if(Tools.CURRENTTYPE==0){
//								map.put("servicetype","8");	
//							}else if(Tools.CURRENTTYPE==1){
//								map.put("servicetype","9");
//							}else if(Tools.CURRENTTYPE==2){
//								map.put("servicetype","0");
//							}else if(Tools.CURRENTTYPE==3){
//								map.put("servicetype","6");
//							}else if(Tools.CURRENTTYPE==4){
//								map.put("servicetype","7");
//							}
//							map.put("servicetype","9");
//							map.put("clienttype","0");
							////0集成版本  1.代驾宝 2 家政宝 3.健康宝，4定位宝
//							if(Tools.CURRENTTYPE==0){
//								map.put("clientversion",Tools.sh_request_vision);	
//							}else if(Tools.CURRENTTYPE==1){
//								map.put("clientversion",Tools.dj_request_vision);
//							}else if(Tools.CURRENTTYPE==2){
//								map.put("clientversion",Tools.jz_request_vision);
//							}else if(Tools.CURRENTTYPE==3){
//								map.put("clientversion",Tools.jk_request_vision);
//							}else if(Tools.CURRENTTYPE==4){
//								map.put("clientversion",Tools.dw_request_vision);
//							}
//							map.put("devicetype",App.getInstance().mobileModel);
//							map.put("osversion",App.getInstance().sysVersion);
//							map.put("networktype",App.getInstance().networkType);
//							map.put("errortype","0");
							if (Tools.checkNetworkAvailable1(tcontext)){
//							 HttpUtil.post(url, map, "hhh", "utf-8");
							}
						}
					});	
					t.start();
					}catch (Exception e) {
						// TODO: handle exception
					}
				
				}
				else if(msg.what==UNSUPPORT_NETWORK){
					Toast.makeText(tcontext, R.string.unsupport_network_location, Toast.LENGTH_LONG).show(); 
				}
			};
		};
		public static BLocationStation getInstance(Context context) {
			if (instance == null) {
				instance = new BLocationStation(context);
			}
			return instance;
		}
		
		public BMapManager getBMapManager(){
			return mBMapMan;
		}
		
		public LocationClient getLocationClient(){
			 return mLocationClient;
		}
		
		public MKSearch getMKSearch(){
			return mkSearch;
		}
//		public GLocationManager getGLocationManager(){
//			return gLocationManager;
//		}
		static boolean m_bKeyRight = true; // 授权Key正确，验证通过
		// 常用事件监听，用来处理通常的网络错误，授权验证错误等
		public static class MyGeneralListener implements MKGeneralListener {
			public void onGetNetworkState(int iError) {
				Log.d("MyGeneralListener", "onGetNetworkState error is " + iError);
				/*
				 * Toast.makeText(mApp.getApplicationContext(), "网络出错！",
				 * Toast.LENGTH_LONG).show();
				 */
			}

			public void onGetPermissionState(int iError) {
				Log.d("MyGeneralListener", "onGetPermissionState error is "
						+ iError);
				if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
					// 授权Key错误：
					/*
					 * Toast.makeText(mApp.getApplicationContext(), "授权失败！",
					 * Toast.LENGTH_LONG) .show();
					 */
					m_bKeyRight = false;
				}
			}

		}
		
		public  interface LocationFinish {
		   public static final int SUCESS = 1;
		   public static final int FAIL = 0;
		   public abstract void callBack(int state);
		}
		
		private class MyLocationListner implements LocationListener {

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				bTimeCancel();
				if (location != null) {  
	                try {
	                	double[] ll=BdLocator.baiduToMap84(location.getLongitude(), location.getLatitude());
	                	
	                    int longitude = (int) (100000 * ll[0]);  
	                    int latitude = (int) (100000 * ll[1]);  

	                    App.getInstance().lon=longitude;
	                    App.getInstance().lat=latitude;
	                    App.getInstance().glon=(int)(location.getLongitude()*100000);
	                    App.getInstance().glat=(int)(location.getLatitude()*100000);
	                    App.getInstance().userKey = Tools.getUserKey(tcontext);
	                    App.getInstance().telephone = Tools.getTelephone(tcontext);
	                    
//	                    Toast.makeText(InitActivity.this, "获取坐标："+App.getInstance().lon+","+App.getInstance().lat, Toast.LENGTH_LONG).show();
	                    GeoPoint point = new GeoPoint(latitude*10, longitude*10);
	                    GeoPoint abpoint2 = CoordinateConvert.bundleDecode(CoordinateConvert
	        					.fromWgs84ToBaidu(point));
	                    mkSearch.reverseGeocode(abpoint2);  
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                }  
	            } 
			}
			
		}
		
		/**
		 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
		 */
		public class MyLocationListenner implements BDLocationListener {
			@Override
			public void onReceiveLocation(BDLocation location) {
				bTimeCancel();
				if (location == null){
	            	SystemOut.out("baidu loaction fail");
//	            	startGoogleLocation();
	            	 handler.sendEmptyMessage(REQUEST_FILED);
					 if(locationFinish != null){
		                locationFinish.callBack(LocationFinish.FAIL);
					 }
					return ;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("time : ");
				sb.append(location.getTime());
				sb.append("\nerror code : ");
				sb.append(location.getLocType());
				sb.append("\nlatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nradius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation){
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\n省：");
					sb.append(location.getProvince());
					sb.append("\n市：");
					sb.append(location.getCity());
					sb.append("\n区/县：");
					sb.append(location.getDistrict());
					sb.append("\naddr : ");
					sb.append(location.getAddrStr());
				}
				sb.append("\nsdk version : ");
				sb.append(mLocationClient.getVersion());
				sb.append("\nisCellChangeFlag : ");
				sb.append(location.isCellChangeFlag());
				SystemOut.out(sb.toString());
				Log.i(TAG, sb.toString());
				unRegisterLocationListener();

				
				double[] ll = BdLocator.baiduToMap84(location.getLongitude(),
						location.getLatitude());
	
				int longitude = (int) (100000 * ll[0]);
				int latitude = (int) (100000 * ll[1]);
				
				App.getInstance().lon = longitude;
				App.getInstance().lat = latitude;
				App.getInstance().glon = longitude;
				App.getInstance().glat = latitude;
				App.getInstance().userKey = Tools.getUserKey(tcontext);
				App.getInstance().telephone = Tools.getTelephone(tcontext);
                 
	           	 SystemOut.out("baidu loaction sucess");
	//         	unRegisterLocationListener();
	           	if(!Tools.isNull(location.getCity()))
	             App.getInstance().city =location.getCity();
	           	if(!Tools.isNull(location.getAddrStr()))
	             App.getInstance().address=location.getAddrStr();
	             
	             if(!Tools.isNull(location.getStreet())&&Tools.isNull(location.getAddrStr())){
	                   App.getInstance().street =location.getStreet();
	                   
	             }
	             
//	         	Intent intent1 = new Intent(
//						Constants.INTENT_ACTION_GET_ISYS);
//				App.getInstance().sendBroadcast(intent1);
				
	             SystemOut.out("city111="+(location.getStreet()));
	             isLocallocation=false;
	             if(locationFinish != null){
	                locationFinish.callBack(LocationFinish.SUCESS);
	             }  
			}
			
			public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ; 
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Poi time : ");
				sb.append(poiLocation.getTime());
				sb.append("\nerror code : "); 
				sb.append(poiLocation.getLocType());
				sb.append("\nlatitude : ");
				sb.append(poiLocation.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(poiLocation.getLongitude());
				sb.append("\nradius : ");
				sb.append(poiLocation.getRadius());
				if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\naddr : ");
					sb.append(poiLocation.getAddrStr());
				} 
				if(poiLocation.hasPoi()){
					sb.append("\nPoi:");
					sb.append(poiLocation.getPoi());
				}else{				
					sb.append("noPoi information");
				}
				
				Log.i(TAG, sb.toString());
			}
		}
		
}
