package com.aline.download;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.aline.util.EdjTools;

class DownloadDialogHandler extends Handler 
{
	DownloadDialog mDownloadDialog;
	
	public DownloadDialogHandler(DownloadDialog downloadDialog) 
	{
		if(EdjTools.isDebug)Log.i("DownloadDialogHandler", "C'tor()");
		
		mDownloadDialog = downloadDialog;
	}
	
	@Override
	public void handleMessage(Message msg) 
	{
		if(EdjTools.isDebug)Log.i("DownloadDialogHandler", "handleMessage()");
		
		super.handleMessage(msg);
		
		DownloadStates downloadStates = (DownloadStates)msg.obj;
		Bundle data=msg.getData();
		String mstr=data.getString("msg");
	    switch(downloadStates) 
	    {
        	case MESSAGE_DOWNLOAD_STARTING :
					mDownloadDialog.show();
        	case MESSAGE_DOWNLOAD_COMPLETE :
        			mDownloadDialog.updateState(downloadStates,mstr);
        			break;
        	case MESSAGE_DOWNLOAD_PROGRESS :
        		mDownloadDialog.updateState(downloadStates, msg.arg1*100/msg.arg2,null,mstr);
//        		mDownloadDialog.updateState(downloadStates, msg.arg1*100/msg.arg2,msg.arg1+"/"+msg.arg2,mstr);
        		break;
        	default:
        		break;
    	}

	}
}
