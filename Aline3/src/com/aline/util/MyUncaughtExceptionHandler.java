package com.aline.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.aline.app.EdjApp;
import com.aline.location.BLocationStation;

public class MyUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {
	private Thread.UncaughtExceptionHandler a;
	private Context mApp = null;  
	private PrintStream mErr;
	private String createExLogFile(String logFile) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String storagePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/Android/data/com.emotte.edj/logs";
				File sddir = new File(storagePath);
				if (!sddir.exists()) {
					sddir.mkdirs();
				}
				String tem=sddir.getAbsolutePath()+"/"+logFile;
				return tem;
			} else
				return null;
	}

	public MyUncaughtExceptionHandler(Context mApp){
		 this.a = Thread.getDefaultUncaughtExceptionHandler();
		 this.mApp=mApp;
		 String hFile = createExLogFile("Crash.log");
			if(hFile!=null){
				try {
					mErr = new PrintStream(new FileOutputStream(hFile, true));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if(EdjTools.isDebug)
		Log.i("MyUncaughtExceptionHandler","app Ex="+ex.getMessage());
		if (mErr != null) {
			mErr.print("time is:" + dateToString(new Date(), null));
			mErr.println(",ThreadName:" + thread.getName()+",ThreadId:" + thread.getId());
			ex.printStackTrace(mErr);
			mErr.flush();
		} else
			ex.printStackTrace();
//		broadcastMsg(mApp,debugStr);
		 if (!handleException(ex) && a != null) {  
	            //如果用户没有处理则让系统默认的异常处理器来处理  
	            a.uncaughtException(thread, ex); 
	            
	     } else {  
	            //Sleep一会后结束程序  
	            try {  
	                Thread.sleep(3000);  
	            } catch (InterruptedException e) {  
	                e.printStackTrace();
	            }  
	            android.os.Process.killProcess(android.os.Process.myPid());  
	            System.exit(0); 
	            EdjApp.finishAll();
//	            LocationStation.gpsCancel();
	            BLocationStation.getInstance(EdjApp.getInstance()).destroyManager();

	      }  
//       if(ex.getMessage().indexOf("bitmap size exceeds VM budget")!=-1){
//        	  try {  
//                  Thread.sleep(3000);  
//              } catch (InterruptedException e) {  
//                  e.printStackTrace();
//              }  
//              android.os.Process.killProcess(android.os.Process.myPid());  
//              System.exit(0);  
//        }else
//		if(a!=null)
//		   a.uncaughtException(thread, ex);
	}
	private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return true;  
        }  
        final String debugStr = getDebugReport(ex);
        
        String data = LogUtils.makeLogStr(debugStr);
        LogUtils.logFileOperate(mApp, data);
        
        if(EdjTools.isDebug)
        Log.i("MyUncaughtExceptionHandler","debugStr Ex="+debugStr);
        postException(debugStr);
    			
        return true;  
    } 
	

	private void postException(String debugStr){
        EdjApp App = (EdjApp) mApp.getApplicationContext();
        //192.168.1.39:8080
		String url="http://"+EdjTools.HOST+"/c/invok/saveerror";
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("userkey",App.userKey);
		////8集成版本  9.代驾宝 0 家政宝 6.健康宝，7定位宝
		
			map.put("servicetype","8");	
		
		map.put("clienttype","0");
		////0集成版本  1.代驾宝 2 家政宝 3.健康宝，4定位宝
		
			map.put("clientversion","1.00");	
		
//		map.put("clientversion",App.clientVersion);
		map.put("devicetype",App.mobileModel);
		map.put("osversion",App.sysVersion);
		map.put("networktype",App.networkType);
		map.put("errortype","1");
		map.put("errorlog",debugStr);
		try{
			if (EdjTools.checkNetworkAvailable1(mApp)) {
//				HttpUtil.post(url, map, "hhh", "utf-8");
			}
		}catch (Exception e) {
			// TODO: handle exception
		}

	}
	

	

	public static String dateToString(Date date, String par) {
		String string = "";

		if (date == null) {
			Calendar cal = Calendar.getInstance();
			date = cal.getTime();
		}
		if (par == null)
			par = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat dateformat = new SimpleDateFormat(par);
		string = dateformat.format(date);
		return string;
	}
	public String getDebugReport(Throwable aException) {  
	        NumberFormat theFormatter = new DecimalFormat("#0.");  
	        String theErrReport = "";  
	          
	        theErrReport += mApp.getPackageName()+" generated the following exception:\n";  
	        theErrReport += aException.toString()+"\n";  
	          
	        //stack trace  
	        StackTraceElement[] theStackTrace = aException.getStackTrace();  
	        if (theStackTrace.length>0) {  
	            theErrReport += "--------- Stack trace ---------\n";  
	            for (int i=0; i<theStackTrace.length; i++) {  
	                theErrReport += theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n";  
	            }//for  
	            theErrReport += "-------------------------------\n";  
	        }  
	          
	        //if the exception was thrown in a background thread inside  
	        //AsyncTask, then the actual exception can be found with getCause  
	        Throwable theCause = aException.getCause();  
	        if (theCause!=null) {  
	            theErrReport += "----------- Cause -----------\n";  
	            theErrReport += theCause.toString() + "\n";  
	            theStackTrace = theCause.getStackTrace();  
	            for (int i=0; i<theStackTrace.length; i++) {  
	                theErrReport += theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n";  
	            }//for  
	            theErrReport += "-----------------------------\n";  
	        }//if  
	          
	        //app environment  
	        PackageManager pm = mApp.getPackageManager();  
	        PackageInfo pi;  
	        try {  
	            pi = pm.getPackageInfo(mApp.getPackageName(), 0);  
	        } catch (NameNotFoundException eNnf) {  
	            //doubt this will ever run since we want info about our own package  
	            pi = new PackageInfo();  
	            pi.versionName = "unknown";  
	            pi.versionCode = 69;  
	        }  
	        Date theDate = new Date();  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        theErrReport += "-------- Environment --------\n";  
	        theErrReport += "Time\t="+sdf.format(theDate)+"\n";  
	        theErrReport += "Device\t="+Build.FINGERPRINT+"\n";  
	        try {  
	            Field theMfrField = Build.class.getField("MANUFACTURER");  
	            theErrReport += "Make\t="+theMfrField.get(null)+"\n";  
	        } catch (SecurityException e) {  
	        } catch (NoSuchFieldException e) {  
	        } catch (IllegalArgumentException e) {  
	        } catch (IllegalAccessException e) {  
	        }  
	        theErrReport += "Model\t="+Build.MODEL+"\n";  
	        theErrReport += "Product\t="+Build.PRODUCT+"\n";  
	        theErrReport += "App\t\t="+mApp.getPackageName()+", version "+pi.versionName+" (build "+pi.versionCode+")\n";  
	        theErrReport += "Locale="+mApp.getResources().getConfiguration().locale.getDisplayName()+"\n";  
	        theErrReport += "-----------------------------\n";  
	      
	        theErrReport += "END REPORT.";  
	        return theErrReport;  
	  }
}

