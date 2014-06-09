package com.emotte.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.emotte.data.ServiceInfo;
import com.emotte.data.ServiceInfoComparator;
import com.emotte.lzh.EdjApp;

public class SharedPreTools {
	
	//单键值存储----------------------------
	public static String readShare(String sharename,String key) {
		SharedPreferences user =  EdjApp.getInstance().getSharedPreferences(sharename,0);
		return user.getString(key, "");
	}

	public static void writeShare(String sharename,String key,String value ) {
		SharedPreferences user = EdjApp.getInstance().getSharedPreferences(sharename, 0);
		Editor editor = user.edit();
		editor.putString(key, value);
		editor.commit();
	}  
	
	public static void removeShare(String sharename,String key ) {
		SharedPreferences user = EdjApp.getInstance().getSharedPreferences(sharename, 0);
		Editor editor = user.edit();
		editor.remove(key);
		editor.commit();
	} 
	
	
	//多键值存储
	public static void writegroup(String sharename,String key,String value) {
		SharedPreferences call = EdjApp.getInstance().getSharedPreferences(sharename, 0);
		String group = call.getString(key, "");
		if (group.indexOf(value) < 0) {
			String newgroup = group + "," + key;
			Editor editor = call.edit();
			editor.putString(key, newgroup);
			editor.commit();
		}
	}

	public static void removegroup(String sharename,String key) {
		SharedPreferences call =  EdjApp.getInstance().getSharedPreferences(sharename, 0);
		String group = call.getString(key, "");
		String newgroup = group.replace("," + key, "");
		Editor editor = call.edit();
		editor.putString(key, newgroup);
		editor.commit();
	}
	
	
	
	/**
	 * 储存单条对象  serviceInfo
	 * @param per_name
	 * @param key
	 * @param vaule
	 */
	public static void saveServicePre(String per_name,String key,Object vaule){
		SharedPreferences loginUserInfo = EdjApp.getInstance().
				getSharedPreferences(per_name, Context.MODE_APPEND);
		Editor preEd = null;
		if(loginUserInfo != null){
			preEd = loginUserInfo.edit();
		}
		if(preEd != null){
			if(vaule != null){				
				ServiceInfo info1=(ServiceInfo)vaule;
				info1.setId(getTimeId());
				info1.setCreateTime(getCreateTime());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        ObjectOutputStream oos=null;
		        try {
		         oos = new ObjectOutputStream(baos);
		         
		         oos.writeObject(info1);
		        } catch (IOException e) {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		        }
				String info = new String(Base64.encodeBase64(baos.toByteArray()));
				preEd.putString(key, info);
			}
			preEd.commit();
		}
	}
	
	/**
	 * 读取对象集合   serviceInfo
	 * @param per_name
	 * @param oblist
	 */
	public static <T> List<T> readAllServicePre(String per_name){
		  SharedPreferences loginUserInfo = EdjApp.getInstance().
				  getSharedPreferences(per_name, Context.MODE_APPEND);
		  Map<String, ?> maps = loginUserInfo.getAll();//取出所有数据
	      List<T> oblist = new ArrayList<T>();
		  Iterator it = maps.values().iterator();
		  while(it.hasNext()){
			 String base64Str = (String) it.next();
			 byte[] base64Bytes = Base64.decodeBase64(base64Str .getBytes());
			 ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			 ObjectInputStream ois;
			 try {
			   ois = new ObjectInputStream(bais);
			   T tempobj = (T) ois.readObject();
			   oblist.add(tempobj);
//			   LoginUser loginUser = (LoginUser)tempobj;
			 } catch (StreamCorruptedException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			 } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			 } catch (ClassNotFoundException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			 }
		  }
		    ArrayList<ServiceInfo>tempserviceInfo = new ArrayList<ServiceInfo>();
		    tempserviceInfo.addAll((Collection<? extends ServiceInfo>) oblist);
		    
			Comparator<ServiceInfo> ascComparator = new ServiceInfoComparator(); 
			Collections.sort(tempserviceInfo, ascComparator);
			oblist.clear();
			oblist.addAll((Collection<? extends T>) tempserviceInfo);
			
		  return oblist;
	}
	/**
	 * 删除单条对象数据
	 * @param per_name
	 * @param key
	 */
	public static void removeObjectPre(String per_name,String key){
		SharedPreferences loginUserInfo = EdjApp.getInstance().
				getSharedPreferences(per_name, Context.MODE_APPEND);
		Editor preEd = null;
		if(loginUserInfo != null){
			preEd = loginUserInfo.edit();
		}
		if(key==null||"".equals(key)){
			preEd.clear();
		}else{
			preEd.remove(key);
		}
		preEd.commit();
	}
	
	//当前时间为id
	public static long getTimeId() {
		Date data = new Date();
		long time=data.getTime();
		return time;
	}
	
	public static Date getCreateTime() {
		Date data = new Date();
		return data;
	}
	
	//---------------------------
	/**
	 * 储存单条对象 
	 * @param per_name
	 * @param key
	 * @param vaule
	 */
	public static void saveObjectPre(String per_name,String key,Object vaule){
		SharedPreferences loginUserInfo = EdjApp.getInstance().
				getSharedPreferences(per_name, Context.MODE_APPEND);
		Editor preEd = null;
		if(loginUserInfo != null){
			preEd = loginUserInfo.edit();
		}
		if(preEd != null){
			if(vaule != null){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        ObjectOutputStream oos=null;
		        try {
		         oos = new ObjectOutputStream(baos);
		         oos.writeObject(vaule);
		        } catch (IOException e) {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		        }
				String info = new String(Base64.encodeBase64(baos.toByteArray()));
				preEd.putString(key, info);
			}
			preEd.commit();
		}
	}
	
	/**
	 * 读取对象集合 
	 * @param per_name
	 * @param oblist
	 */
	public static <T> List<T> readAllObjectPre(String per_name){
		  SharedPreferences loginUserInfo = EdjApp.getInstance().
				  getSharedPreferences(per_name, Context.MODE_APPEND);
		  Map<String, ?> maps = loginUserInfo.getAll();//取出所有数据
	      List<T> oblist = new ArrayList<T>();
		  Iterator it = maps.values().iterator();
		  while(it.hasNext()){
			 String base64Str = (String) it.next();
			 byte[] base64Bytes = Base64.decodeBase64(base64Str .getBytes());
			 ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			 ObjectInputStream ois;
			 try {
			   ois = new ObjectInputStream(bais);
			   T tempobj = (T) ois.readObject();
			   oblist.add(tempobj);
			 } catch (StreamCorruptedException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			 } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			 } catch (ClassNotFoundException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			 }
		  }
		  return oblist;
	}
}
