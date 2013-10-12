package com.baidu.mapapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.aline.app.App;
import com.aline.util.Tools;
import com.example.jpushdemo.MainActivity;


public class BdLocator implements LocationListener {
	private Activity activity;
	private Context context;
	
	
	//InitActivity 必须extends MapActivity才能取到位置
	public BdLocator(Activity c) { 
		this.activity=c;
		context= c;
		
		Mj.b.requestLocationUpdates(this);
	}

	public void onLocationChanged(Location location) {
		Mj.b.removeUpdates(this);
		
		double[] ll=baiduToMap84(location.getLongitude(),location.getLatitude());
		
		Intent nIntent = new Intent(activity, MainActivity.class);

		App App = (App) activity.getApplicationContext();
		App.lon = (int) (ll[0] * 100000);
		App.lat = (int) (ll[1] * 100000);
		App.city = "北京市";
		App.userKey = Tools.getUserKey(context);
		
		Log.i("INIT", App.lon+","+App.lat+":"+App.glon+","+App.glat);
		activity.startActivity(nIntent);
		activity.finish();
	}
	
	public static double[]map84ToBaidu(double lon,double lat){
		GeoPoint r=map84ToBaidu(new GeoPoint(lat,lon));
		return new double[]{r.getLongitudeE6()/(double)1000000,r.getLatitudeE6()/(double)1000000};
	}
	
	public static double[] baiduToMap84(double lon,double lat){
		GeoPoint r=baiduToMap84(new GeoPoint(lat,lon));
		return new double[]{r.getLongitudeE6()/(double)1000000,r.getLatitudeE6()/(double)1000000};
	}
	
	public static GeoPoint map84ToBaidu(GeoPoint p) {
		GeoPoint r = CoordinateConvert.bundleDecode(CoordinateConvert
				.fromWgs84ToBaidu(p));
		return r;
	}
	
	public static GeoPoint baiduToMap84(GeoPoint p) {
		GeoPoint r1 = CoordinateConvert.bundleDecode(CoordinateConvert
				.fromWgs84ToBaidu(p));
		GeoPoint r = new GeoPoint((2 * p.getLatitudeE6() - r1.getLatitudeE6())
				/ (double) 1000000,
				(2 * p.getLongitudeE6() - r1.getLongitudeE6())
						/ (double) 1000000);
		return r;
	}

	
	
}