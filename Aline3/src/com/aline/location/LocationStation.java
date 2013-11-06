package com.aline.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.aline.app.EdjApp;
import com.aline.util.Constants;
import com.aline.util.EdjTools;

public class LocationStation {
	private static final String TAG = "LocationStation";
	TelephonyManager manager;
	int mcc, mnc, lac[], ci[];
	String mac;
	int count;
	PhoneStateListener listener;
	private static final int CHECK_INTERVAL = 1000 * 30;
	EdjApp app;
	// 变量定义
	public static LocationManager locationManager;
    public static LocationListener gpsListener = null;
	public static LocationListener networkListner = null;
//	private Context context;
	private static LocationStation instance;
	private Timer outtime;
	private Timer gpsTimeOut;//GPS定位超时
	Location currentLocation;
	String result_location;
	private String city;
	double la, lo;
	double accuracy;

    
	public static LocationStation getInstance(EdjApp edApp) {
		if (instance == null) {
			instance = new LocationStation(edApp);
		}
//		instance.app = edApp;
		return instance;
	}
	private LocationStation(){
	}
	public LocationStation(EdjApp edApp){
		app = edApp;
	}
	 public void timeOut(long outTime,final Context ct){
	    	if (outtime != null) {
				try {
					outtime.cancel();
				} catch (Exception e) {

				}
				outtime = null;
			}
			outtime = new Timer();
			outtime.schedule(
					new TimerTask() {
						public void run() {
							
							 broadcastGetLocFailed(Constants.INTENT_ACTION_GET_LOCATION_FAILED);

						}
					}, outTime);	
	 }
	 public void timeCancel(){
	    	if (outtime != null) {
				try {
					outtime.cancel();
				} catch (Exception e) {

				}
				outtime = null;
			}
	    }
	
	 public static void gpsCancel(){
		if(gpsListener!=null){
			locationManager.removeUpdates(gpsListener);
			gpsListener=null;
		}
		if(networkListner!=null){
			locationManager.removeUpdates(networkListner);
			networkListner=null;
		}
		if(locationManager != null){
			locationManager = null;
		}
		if(EdjTools.isDebug)Log.v(TAG, "gpsCancel");
	 }
	 public void initGpsTimeOut(long outTime){
		 gpsTimeOut =  new Timer();
		 gpsTimeOut.schedule(new TimerTask() {
				public void run() {
					gpsCancel();
				}
		 }, outTime);	
	 }
	 Handler locationHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch(msg.what){
				case 0:
					registerLocationListener();
					break;
				case 1:
					if(gpsListener!=null){
						locationManager.removeUpdates(gpsListener);
						gpsListener=null;
					}
					if(networkListner!=null){
						locationManager.removeUpdates(networkListner);
						networkListner=null;
					}
					if(locationManager != null){
						locationManager = null;
					}
					break;
				}
			}
			
		};
	public void getLocation(Context ct,long outTime) {
		Log.i(TAG, "startLcationStation");
		locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
//		if(!Utils.isReLocation()){
//			Location location = new Location("public");
//			location.setLatitude(App.lat);
//			location.setLongitude(App.lon);
//			showLocation(location);
//			return;
//		}
		manager = (TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE);
		listener = new CellStateListener ();
		ci = new int[10];
        lac = new int[10];
        locationHandler.sendEmptyMessage(0);
        timeOut(outTime,ct);
		if(EdjTools.getNetworkType(app)!=EdjTools.NET_NOT_AVAILABLE){
		  update(ct);
		}else{
		  broadcastGetLocFailed(Constants.LBS_INTENT_ACTION_NETWORK_LOCATION_FAILED);
		}
		
	}
	private class MyLocationListner implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the location provider.
			if(EdjTools.isDebug)Log.v(TAG, "Got New Location of provider:"
					+ location.getProvider());
			if (currentLocation != null) {
				if (isBetterLocation(location, currentLocation)) {
					if(EdjTools.isDebug)Log.v(TAG, "It's a better location");
					currentLocation = location;
//					showLocation(location);
					
				} else {
					if(EdjTools.isDebug)Log.v(TAG, "Not very good!");
				}
			} else {
				if(EdjTools.isDebug)Log.v(TAG, "It's first location");
				currentLocation = location;
//				showLocation(location);
				
				
				
			}
			/*
			 * // 移除基于LocationManager.NETWORK_PROVIDER的监听器 if
			 * (LocationManager.NETWORK_PROVIDER.equals(location.getProvider()))
			 * { locationManager.removeUpdates(this); }
			 */
			
			//locationManager.removeUpdates(this);
		}

		// 后3个方法此处不做处理
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i("TAG", "onStatusChanged-provider:"+provider);

		}

		public void onProviderEnabled(String provider) {
			Log.i("TAG", "onProviderEnabled-provider:"+provider);	 

		}

		public void onProviderDisabled(String provider) {
			Log.i("TAG", "onProviderDisabled-provider:"+provider);
			
				

		}
	};

	private void showLocation(Location location) {
		if(location!=null){
			// 纬度
			if(EdjTools.isDebug)Log.v(TAG, "Latitude:" + location.getLatitude());
			// 经度
			if(EdjTools.isDebug)Log.v(TAG, "Longitude:" + location.getLongitude());
			// 精确度
			if(EdjTools.isDebug)Log.v(TAG, "Accuracy:" + location.getAccuracy());
			if(location.getLatitude() != 0&&location.getLongitude() != 0){
//				App.lat = location.getLatitude();
//				App.lon = location.getLongitude();
//				Utils.saveLocationData(app, App.lat, App.lon);
			}
		}else{
			
		}
		Log.i(TAG, "Location: succes");

		timeCancel();
		broadcast(location);
	}

	private void registerLocationListener() {
		if(networkListner == null&&locationManager!=null){
			if(EdjTools.isDebug)Log.v(TAG, "register"+locationManager.getAllProviders());
			
			networkListner = new MyLocationListner();
			if(networkListner!=null&&locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, networkListner);
			}
			gpsListener = new MyLocationListner();
			if(gpsListener!=null&&locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
					0, gpsListener);
			}
			initGpsTimeOut(120*1000);
		}

	}

	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location,
		// use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must
			// be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
	public static boolean isNull(String str) {
		if (str == null || str.trim().equals(""))
			return true;
		return false;
	}
	
	void update(final Context ct) {
  		count = 0;
  		if(!isNull(manager.getNetworkOperator())){
//  			broadcastGetLocFailed(MsgInfor.INTENT_ACTION_GET_LOCATION_FAILED);
//  			return;
  			String tmcc=manager.getNetworkOperator().substring(0, 3);
  			String tmnc=manager.getNetworkOperator().substring(3, 5);
  			if(EdjTools.isNum(tmnc)){
  				mcc = Integer.valueOf(tmcc);
  			}
  			if(EdjTools.isNum(tmnc)){
  				mnc = Integer.valueOf(tmnc);
  			}
//			mcc = Integer.valueOf(manager.getNetworkOperator().substring(0, 3));
//			mnc = Integer.valueOf(manager.getNetworkOperator().substring(3, 5));
			double cdma_lat = 0;
			double cdma_lng = 0;
			manager.listen(listener, 0);
			CellLocation cellLocation = manager.getCellLocation();
			if(cellLocation instanceof GsmCellLocation){
				List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
				count = list.size();
				if(count>2)
					count=2;
				GsmCellLocation sm = (GsmCellLocation) cellLocation;
				for (int i = 0; i < count; i++) {
					lac[i] = sm.getLac();
					ci[i] = list.get(i).getCid();
				}
				lac[count] = sm.getLac();
				ci[count]=sm.getCid();
				count++;
			}else if(Integer.parseInt(Build.VERSION.SDK) >= 5&&cellLocation instanceof android.telephony.cdma.CdmaCellLocation){
				android.telephony.cdma.CdmaCellLocation cdmaCl = (android.telephony.cdma.CdmaCellLocation) cellLocation;
				lac[0] = cdmaCl.getBaseStationId();
				ci[0] = cdmaCl.getSystemId();
				int networkId = cdmaCl.getNetworkId();
				if(EdjTools.isDebug)Log.i(TAG, "networkid:" + networkId);
				cdma_lat = (double)cdmaCl.getBaseStationLatitude()/14400;
				if(EdjTools.isDebug)Log.i(TAG, "lat is:" + cdma_lat);
				cdma_lng = (double)cdmaCl.getBaseStationLongitude()/14400;
				if(EdjTools.isDebug)Log.i(TAG, "lng is:" + cdma_lng);
				Location location = new Location("cdma");
				location.setLatitude(cdma_lat);
				location.setLongitude(cdma_lng);
				
			//	showLocation(location);
//				if(lat != 0&& lng != 0){
//					Utils.saveLocationData(ct, lat, lng);
//				}
				
			//	return;
			}
			//手机不支持基站定位
  		}
		EdjTools.executorService.submit(new Runnable() {
			@Override
			public void run() {
//				Looper.prepare();
				HttpEntity entity = null;
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpParams params = client.getParams();
					HttpConnectionParams.setConnectionTimeout(params, 20000);
					HttpConnectionParams.setSoTimeout(params, 15000);
					HttpPost post = new HttpPost("http://www.google.com/loc/json");// http://www.google.com/glm/mmap");// http://maps.googleapis.com/maps/api/geocode/json?
					JSONObject holder = new JSONObject();

					holder.put("version", "1.1.0");
					holder.put("request_address", true);
					holder.put("address_language", "zh_CN");

					JSONObject data;
					JSONArray array;
					 array = new JSONArray();
					for (int i = 0; i < count ; i++) {
						data = new JSONObject();
						data.put("cell_id", ci[i]); // 9457, 8321,8243
						data.put("location_area_code", lac[i]);// 28602
						data.put("mobile_country_code", mcc);// 208
						data.put("mobile_network_code", mnc);// 0
						data.put("age", 0);
						array.put(data);
					}
					holder.put("cell_towers", array);

					 //////////////////////WIFI定位//////////////////////////////////////
					  if(array.length()==0){
							 WifiManager wm =
							 (WifiManager)app.getSystemService(Context.WIFI_SERVICE);
							 List<ScanResult> wifiList = wm.getScanResults();
							 for (int i = 0; i < wifiList.size(); i++) 
					            {
								 if(EdjTools.isDebug)Log.e("wifi",wifiList.get(i).toString());
					            }
//							 WifiInfo info = wm.getConnectionInfo();
//							 String mac = info.getMacAddress();
//							 if(Tools.isDebug)Log.e("LocationMe","mac:"+mac);
//							 data = new JSONObject();
//							 data.put("mac_address", info.getBSSID());
//							 data.put("signal_strength", wm.getScanResults().get(0).level);
//							 data.put("age", 0);
//							 array = new JSONArray();
//							 array.put(data);
//							 holder.put("wifi_towers", array);	
							for (int i = 0; i < wifiList.size(); i++) {
								data = new JSONObject();
								data.put("mac_address",
										wifiList.get(i).BSSID);
								data.put("ssid", wifiList.get(i).SSID);
								data.put("signal_strength",
										wifiList.get(i).level);
								array.put(data);
							}
							holder.put("wifi_towers", array);
	                    }
					 //////////////////////////////////
			    		Log.i(TAG, "Location: execute");
					StringEntity se = new StringEntity(holder.toString());
					post.setEntity(se);
					long b = System.currentTimeMillis();
					HttpResponse resp = client.execute(post);
					long rspe = System.currentTimeMillis();
					 if(resp.getStatusLine().getStatusCode() == 200){  
					   entity = resp.getEntity();
					 }

					BufferedReader br = new BufferedReader(
							new InputStreamReader(entity.getContent()));
					StringBuffer sb = new StringBuffer();
					String result = br.readLine();
					while (result != null) {
						sb.append(result);
						result = br.readLine();
					}
					result_location = sb.toString();
					if(EdjTools.isDebug)System.out.println("result_location:"+result_location);
					data = new JSONObject(sb.toString());
					data = (JSONObject) data.opt("location");
					if(data != null){
						la = (Double) data.opt("latitude");
						lo = (Double) data.opt("longitude");
						accuracy = (Double) data.opt("accuracy");
						data = (JSONObject) data.opt("address");
						if(data != null){
							String str="";
							if(accuracy <= 800){
								str = (String)data.opt("country")+(String)data.opt("region")
		                           +(String)data.opt("city")+(String)data.opt("street");
							}
							else if(accuracy <= 2000){
								str = (String)data.opt("country")+(String)data.opt("region")
		                           +(String)data.opt("city");
							}
							else {
								str = (String)data.opt("country")+(String)data.opt("region");
							}
							str = (String)data.opt("city");
							if(EdjTools.isDebug)
							System.out.println("str:"+str);
							if(str!=null&&!str.equals(""))
							app.city = str;
							if(str!=null&&!str.equals(""))
							city = str;
//							App.public_location_info = str+":"+la+":"+lo;
					    }
					}
					Location gsmLocation = new Location("net_work");
					gsmLocation.setAccuracy((float) accuracy);
					gsmLocation.setLatitude(la);
					gsmLocation.setLongitude(lo);
					if (currentLocation != null) {
						if (isBetterLocation(gsmLocation, currentLocation)) {
							currentLocation = gsmLocation;
							if(gsmLocation.getLatitude()!=0)
							 app.lat = (int) gsmLocation.getLatitude();
							if(gsmLocation.getLongitude()!=0)
							 app.lon = (int) gsmLocation.getLongitude();
							showLocation(gsmLocation);
							
						}else{
							showLocation(currentLocation);
						}
					} else {
						currentLocation = gsmLocation;
						showLocation(gsmLocation);
					}
//					if(la != 0&& lo != 0){
//						Utils.saveLocationData(ct, la, lo);
//					}
					if(EdjTools.isDebug)
						Log.i(TAG, "handle data cost time is:" + (System.currentTimeMillis() - rspe) + "ms");


				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();

				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					if (entity != null) {
						try {
							entity.consumeContent();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			
			}
		});
	}

	class CellStateListener extends PhoneStateListener {
		public void onCellLocationChanged(CellLocation location) {
			GsmCellLocation gsmL = (GsmCellLocation) location;
			if (count < 2) {
				ci[count-1] = gsmL.getCid();
				lac[count-1] = gsmL.getLac();
				//count++;
				GsmCellLocation.requestLocationUpdate();
				//if (count == 2) {
					manager.listen(this, 0);
				//}
			}
		}
	}
	private void broadcast(Location location){
		if(app.lat!=0&& app.lon!=0&&!app.city.equals("")){
			BLocationStation.getInstance(app).isLocallocation=true;
//             app.stopService(new Intent(app,LocationStationService.class));
			Intent intent = new Intent(Constants.INTENT_ACTION_GET_LOCATION_OK);
			intent.putExtra(Constants.EXTRA_LOCATION_KEY, location);
			intent.putExtra(Constants.EXTRA_LOCATION_CITY, city);
			app.sendBroadcast(intent);
		}
		timeCancel();
		locationHandler.sendEmptyMessage(1);
	}
	
	
	private void broadcastGetLocFailed(String action){
		BLocationStation.getInstance(app).isLocallocation=false;
		 if(app.lat==0&& app.lon==0&&app.city.equals("")){
			Intent intent = new Intent(action);
			app.sendBroadcast(intent);
		 }
		timeCancel();
		locationHandler.sendEmptyMessage(1);
	}
}
