package com.aline.download;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;

import com.aline.util.EdjTools;

class DownloadDialog 
{
	private ProgressDialog mProgressDialog = null;
	private Context mContext = null;
	
	public DownloadDialog(Context context,String title,String message) 
	{
		if(EdjTools.isDebug)Log.i("DownloadDialog", "C'tor()");
		
		mContext = context;
		
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.setCancelable(false);	
		mProgressDialog.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
				
			}
			
		});
	}
	
	public void show()
	{
		if(EdjTools.isDebug)Log.i("DownloadDialog", "show()");
		
		mProgressDialog.show();
	}
	
	public void cancel()
	{
		if(EdjTools.isDebug)Log.i("DownloadDialog", "cancel()");
		
		mProgressDialog.cancel();
	}
	
	public void updateState(DownloadStates downloadState,String message)
	{
		if(EdjTools.isDebug)Log.i("DownloadDialog", "updateState(DownloadStates )");
		
		updateState(downloadState, -1,null,message);
	}
	
	public void updateState(DownloadStates downloadState, int progressValue,String data,String message)
	{
		if(EdjTools.isDebug)Log.i("DownloadDialog", downloadState+"updateState(DownloadStates, progressValue)"+data);
		
	    switch(downloadState) 
	    {
        	case MESSAGE_DOWNLOAD_STARTING :
        		mProgressDialog.setIndeterminate(true);
        		mProgressDialog.setMessage(message);
        		break;
        	case MESSAGE_DOWNLOAD_PROGRESS :
        		mProgressDialog.setIndeterminate(false);
        		mProgressDialog.setMessage(message);
        		mProgressDialog.setProgress(progressValue);
        		break;
        	case MESSAGE_DOWNLOAD_COMPLETE :
        		if(mProgressDialog.isShowing())
        		 mProgressDialog.cancel();
//        		AutoUpdater.isUping=false;
        		break;
        	case MESSAGE_DOWNLOAD_ERROR :
        		mProgressDialog.setIndeterminate(false);
        		mProgressDialog.setMessage(message);
        		mProgressDialog.setProgress(progressValue);
        		break;
        	case MESSAGE_DOWNLOAD_NET_ERROR :
        		mProgressDialog.setIndeterminate(false);
        		mProgressDialog.setMessage(message);
        		mProgressDialog.setProgress(progressValue);
        		break;
        	default:
    	}
	}
}
