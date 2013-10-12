package com.aline.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.aline.app.App;
import com.aline.util.Tools;

public class LocationStationService extends Service {

	public static Handler mHandler;
	private LocationStation ls;
	App app;
	static Context conText;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		app = App.getInstance();

	}
	
	public static void registerHandler(Handler handler){
		mHandler = handler;
	}
    public static void getContext(Context context){
    	conText = context;
    }
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (Tools.isDebug) {
			if (!BLocationStation.getInstance(app).isLocallocation) {
				System.out.println("启动定位服务、、、、、、");
			}
		}
		if(!BLocationStation.getInstance(app).isLocallocation){
//			blocation = BLocationStation.getInstance(app);
			BLocationStation.getInstance(app).registerLocationListener();
			BLocationStation.getInstance(app).startManager();
		}
		if(!BLocationStation.getInstance(app).isLocallocation){
			ls = LocationStation.getInstance(app);
			ls.getLocation(this, 30000);
		}
		
		LocationStationService.this.stopSelf();
//		new Thread(new LocationRunnable()).start();
	}
	class LocationRunnable implements Runnable {
        public void run() {
            try{
            	
            }catch(Exception ex){
                ex.printStackTrace();

            }
        }
    }
	@Override
	public void onDestroy() {
		super.onDestroy();
	}	
	
	
	
	
}
