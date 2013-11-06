package com.aline.lbs;


import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocalService extends Service {

    private static final String TAG = "LocalService"; 
    private IBinder binder=new LocalService.LocalBinder();
    
    @Override
    public IBinder onBind(Intent intent) {
         
        return binder;
    }
    MediaPlayer mediaPlayer=null;
    @Override 
    public void onCreate() { 
            Log.i(TAG, "onCreate"); 
            //这里可以启动媒体播放器
           // if(mediaPlayer==null)
           //     mediaPlayer=MediaPlayer.create(this, uri);
            
            
      
            super.onCreate(); 
    } 

    @Override 
    public void onStart(Intent intent, int startId) { 
            Log.i(TAG, "onStart"); 
            super.onStart(intent, startId); 
    } 

    @Override 
    public int onStartCommand(Intent intent, int flags, int startId) { 
          Log.i(TAG, "onStartCommand"); 
        return START_STICKY;
    }

    
    
    @Override 
    public void onDestroy() { 
            Log.i(TAG, "onDestroy"); 
            super.onDestroy(); 
    } 

    
    //定义内容类继承Binder
    public class LocalBinder extends Binder{
        //返回本地服务
        LocalService getService(){
            return LocalService.this;
        }
    }
    
    
}