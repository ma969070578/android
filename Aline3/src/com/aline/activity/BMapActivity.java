package com.aline.activity;



import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.aline.app.App;
import com.aline.util.Tools;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;

public class BMapActivity extends MapActivity {
    /** Called when the activity is first created. */
	private MapView mapView;//百度地图控件
	private BMapManager bMapManager;//百度地图的引擎
	private String Key = "2360A4148EF0C9C9C6C3ECBF93D719299F903C01";//初始化Baidu引擎需要Key
	private MapController mapController;//设置地图缩放的工具
	private LocationListener mLocationListener = null;//onResume时注册此listener，onPause时需要Remove
	private MyLocationOverlay mLocationOverlay = null;	//定位图层
	private GeoPoint pt;
	private LocationManager locationManager; 
	private static final int quit = 1;
	private static final int about = 2;
	App mApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);
        
        mapView = (MapView)findViewById(R.id.bmapView);
        bMapManager = new BMapManager(BMapActivity.this);
        //必须加载Key
        bMapManager.init(Key, new MKGeneralListener() {
			
			public void onGetPermissionState(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 == 300){
					Toast.makeText(BMapActivity.this, "输入的Key有错", 1).show();
				}
			}
			
			public void onGetNetworkState(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        initMapActivity(bMapManager);
        mapView.setBuiltInZoomControls(true);//表示可以设置缩放功能
        mapController = mapView.getController();//获取缩放控件
        GeoPoint geoPoint = new GeoPoint((int)(39.915*1E6),(int)(116.404*1E6));//获取经纬度坐标
        mapController.setCenter(geoPoint);//将该点设置为地图中心点坐标
        mapController.setZoom(12);//设置缩放程度为12
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        boolean GPS_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        String status = "";  
        if(GPS_status){  
            status += "GPS已开启";  
            
            }else{  
            status += "GPS未开启 ,开启后定位更加准确！ ";  
            
            
            }  
        Toast.makeText(this,status,1).show();  

        mLocationOverlay = new MyLocationOverlay(this, mapView);
	    mapView.getOverlays().add(mLocationOverlay);
	
	
        // 注册定位事件
        mLocationListener = new LocationListener() {
		
		
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			System.out.println("aaaaaaaa");
			if (location != null){
				pt = new GeoPoint((int)(location.getLatitude()*1e6),
						(int)(location.getLongitude()*1e6));
				System.out.println("aaaaaaaaaa=>"+ location.getLatitude()*1e6);
				mApp.lon=(int)(location.getLongitude()*1e6);
				mApp.lat=(int)(location.getLatitude()*1e6);
//				Toast.makeText(MainBMapActivity.this,"lon="+mApp.lon+",lat="+mApp.lat,1).show(); 
				mapController.setCenter(pt);
				mapView.getController().animateTo(pt);
			}
		}
	};
  }


    //必须要复写如下的生命周期函数
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	if(bMapManager != null){
    		bMapManager.destroy();
    		bMapManager = null;
    	}
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	bMapManager.getLocationManager().requestLocationUpdates(mLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // 打开指南针
        bMapManager.start();
		super.onResume();

    }
    
    
    
//    @Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//    	menu.add(0, quit, 1, R.string.myMap_quit);
//		menu.add(0, about, 2, R.string.myMap_about);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		if(item.getItemId() == quit){
//			finish();
//		}
//		return super.onOptionsItemSelected(item);
//	}


	@Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	//BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
    	bMapManager.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
//		  mLocationOverlay.disableCompass()  // 关闭指南针
        bMapManager.stop();
		super.onPause();

    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
		
	}
	
	
	
	
	private Menu mMenu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Hold on to this
		mMenu = menu;

		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId()== R.id.referesh){
			 Intent intent = new Intent();                 
			 intent.setClass(BMapActivity.this, ScrollLayoutActivity.class);
			 startActivity(intent);               
			 finish();
			 return true;
		}
		else if (item.getItemId()== R.id.search){
	
			Intent nIntent = new Intent(this, FriendsActivity.class);
			startActivity(nIntent);
			finish();
			return true;
		}
		return false;
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
		Tools.appExit(this);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}