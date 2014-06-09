package com.aline.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TimeTools {

	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author maq   微博
	 */
	public static String formatStringDuring(long mss) {

		String time = null;
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		System.out.println(days + " days " + hours + " hours " + minutes
				+ " minutes " + seconds + " seconds ");
		if(days>0||hours>0||minutes>0){
			time=days*24+hours+"小时"+minutes+"分钟";
		}else if(days==0&&hours==0&&minutes==0&&seconds>0){
			time=seconds+"秒";
		}
		return time;

	}
	
	
	public static String formatTimeStringDuring(long mss) {

		String time;
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		System.out.println(days + " days " + hours + " hours " + minutes
				+ " minutes " + seconds + " seconds ");
		StringBuffer buffer=new StringBuffer();
		long mhours=days*24+hours;
		if(mhours<10){
			buffer.append("0"+mhours);
		}else {
			buffer.append(mhours);
		}
		buffer.append(":");
		if(minutes<10){
			buffer.append("0"+minutes);
		}else {
			buffer.append(minutes);
		}
		buffer.append(":");
		if(seconds<10){
			buffer.append("0"+seconds);
		}else {
			buffer.append(seconds);
		}
		time=days*24+hours+":"+minutes+":"+seconds;

		return buffer.toString();

	}
	
	
	
	//显示总运动小时
	public static String formathourDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		double b=(double)Math.round(minutes*100/60)/100;
		
		System.out.println( days*24+hours);
		System.out.println( b);
		return days*24+hours+b+"";
	}
	
	
	public static HashMap formatDuring(long mss) {

		HashMap map = new HashMap();
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		System.out.println(days + " days " + hours + " hours " + minutes
				+ " minutes " + seconds + " seconds ");
//		map.put("days", days + "");
		map.put("hours",days*24+ hours + "");
		map.put("minutes", minutes + "");
		map.put("seconds", seconds + "");

		return map;

	}
	//获取毫秒值
	public static Date getTime(String str){
		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date time = null;
		try {
			time = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return time;
	}
	//毫秒转时间
	public static String getStringSSTimeByMillisecond(String str) {

		Date date = new Date(Long.valueOf(str));


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String time = format.format(date);

		return time;
	}
	
	
	public static String getStringTimeByMillisecond(String str) {

		Date date = new Date(Long.valueOf(str));


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		String time = format.format(date);

		return time;
	}
	public static String getStringTimeByMillisecondall(String str) {

		Date date = new Date(Long.valueOf(str));


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String time = format.format(date);

		return time;
	}
	
	 //日期型转String
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.format(date);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    
    
    public static String dateToStringSS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(date);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    
    //String 转data
    public static Date stringDayToDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(s);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
    
    
  // member 转化时间
	public static String utctimetotime(String time){
	    String year = time.substring(0, 4);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        String timeString=year+"/"+month+"/"+day;
        return timeString;
		
	}	
	  // member 转化时间
	public static String getStringTime(String time){
			if(!EdjTools.isNull(time)){
				String[] strs=time.split("[.]");
				return strs[0];
			}else {
				return "";
			}

	}	
}
