package com.aline.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

public class LogUtils {
	
	/**
	 * 调用方法
	 * String data = LogUtils.makeLogStr(str);    //形参为字符串
	   String data = LogUtils.makeLogStr(new String[]{});   //形参为字符串数组
       LogUtils.logFileOperate(context, data);
	 */
	/**
	 * 日志文件的创建写入操作
	 */
	public static void logFileOperate(Context context, String data){
		File sddir = createDirectory(context);
		if(sddir!=null){
			File console_file = new File(sddir, "Console.log");
			if(!console_file.exists()){
				try {
					console_file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    	LogUtils.writeLogToFile(console_file, data);
		}
	}
	/**
	 * 拼装log打印字符串
	 */
	public static String makeLogStr(String str){
		StringBuffer sb = new StringBuffer();
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String dateFormat = sdf.format(date);
		sb.append(dateFormat).append(" ").append(str).append("\r\n")
		.append(str);
		String data = sb.toString();
		return data;
	}
	public static String makeLogStr(String[] strs){
		StringBuffer sb = new StringBuffer();
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String dateFormat = sdf.format(date);
		int len = strs.length;
		for(int i=0; i<len; i++){
			String picStr = strs[i];
			sb.append(dateFormat).append(" ").append(picStr).append("\r\n");
		}
		String data = sb.toString();
		return data;
	}
	/**
	 * 写文件操作
	 */
	public static void writeLogToFile(File file, String data){
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, true);
			String str = data;
			byte[] dataByte = str.getBytes();
			outStream.write(dataByte);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(outStream!=null){
					outStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 创建日志文件目录
	 */
	public static File createDirectory(Context context){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String storagePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/emotte/logs";
			File sddir = new File(storagePath);
			if (!sddir.exists()) {
				sddir.mkdirs();
			}
			return sddir;			
		}else{
			return null;
		}
	}
//	/**
//	 * 生成log日志文件目录路径
//	 */
//	public static String getDirectoryPath(Context context){
//    	String storagePath;
//		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			storagePath = Environment.getExternalStorageDirectory()
//					.getAbsolutePath() + "/emotte/logs";
//		}else{
//			storagePath = context.getFilesDir().getAbsolutePath()
//					+ "/tuixin11/logs";
//		}
//		return storagePath;
//    }
    /**
  
    }
	
	
	
//	/**
//	 * 创建发送当天的日志文件Console.log
//	 */
//	public static File createConsoleFile(Context context, boolean isInstallDay, long dayMark){
//		//if(CommonData.DAY_MARK!=0){
//		//}
//		File console_file = null; 
//		File sddir = createDirectory(context);
//		if(isInstallDay){
//			return null;
//			//console_file = new File(sddir, "Console.log");
//		}else{
//			long currentTime = System.currentTimeMillis();
//			long currentDay = currentTime/1000/86400;
//			console_file = new File(sddir, "Console.log");
//			if(dayMark!=currentDay){
//				if(console_file.exists()){
//					console_file.delete();
//				}
//				console_file = new File(sddir, "Console.log");
//			}
//			return console_file;
//		}
//	}
//	/**
//	 * 创建安装当天的日志文件Previous.log,由于android Gmail只能发送一个附件，故不用创建此文件
//	 */
//	public static File createPreviousFile(Context context, boolean isInstallDay, long dayMark){
//		//if(CommonData.DAY_MARK!=0){
//		//}
//		File previous_file = null; 
//		File sddir = createDirectory(context);
//		if(isInstallDay){
//			previous_file = new File(sddir, "Previous.log");
//			return previous_file;
//		}else{
//			return null;
//		}
//	}
}
