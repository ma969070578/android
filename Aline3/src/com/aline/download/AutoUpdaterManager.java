package com.aline.download;


import android.content.Context;

import com.aline.app.App;


/**
 * @author Administrator
 * 检测自动更新管理类
 */
public class AutoUpdaterManager {
	
	private static AutoUpdaterManager instance;
	 private Context tcontext;
	 
	public AutoUpdaterManager(Context context){
	   this.tcontext = App.getInstance();
	   
		
	}
	/*
	 * 自动检测更新
	 * 
	 */
	private void  StartAutoCheckUpdater(){
		 
			 if (!App.getInstance().isAutoUpate) {
				    if(!App.getInstance().isStartCheckUpdata){
				    	App.getInstance().isStartCheckUpdata=true;
					  AutoUpdater.CheckForUpdate(tcontext, true);
				    }
				 
		

		 }
	}
	/*
	 * 手动检测更新
	 * 
	 */
	private void StartHandCheckUpdater(){
		AutoUpdater.CheckForUpdate(tcontext,false);
	}
	
	public static AutoUpdaterManager getInstance(Context context) {
		if (instance == null) {
			instance = new AutoUpdaterManager(context);
		}
		return instance;
	}

}
