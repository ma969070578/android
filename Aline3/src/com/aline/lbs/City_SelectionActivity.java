package com.aline.lbs;

//http://blog.csdn.net/forsta/article/details/7487229
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.aline.activity.R;
import com.aline.app.EdjApp;
import com.aline.util.EdjTools;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class City_SelectionActivity extends Activity {
    /** Called when the activity is first created. */
	
//	private File f = new File("/sdcard/weather/db_weather.db"); //数据库文件
	private File asset = new File("file:///android_asset/db_weather.db");   
	
    private String DB_NAME="db_weather.db";
    private String DB_PATH="";
	            // .getAssets()
	private Spinner province;  //省份spinner
	private Spinner city;      //城市spinner
	
	private List<String> proset=new ArrayList<String>();//省份集合
	private List<String> citset=new ArrayList<String>();//城市集合
	

	private Button button_left;
	private Button button_right;
	private String cityname="北京.北京";
	
	private int pro_id;
	Context tcontext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area);
        
        this.tcontext =City_SelectionActivity.this.getApplicationContext();
        DB_PATH=tcontext.getFilesDir().getPath();
        try {
//        	if(!checkDatabase()){
        		
        		copyDatabase();
//        	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
        province=(Spinner)findViewById(R.id.provinces);
        ArrayAdapter<String> pro_adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getProSet());
        province.setAdapter(pro_adapter);
        province.setOnItemSelectedListener(new SelectProvince());
        
        city=(Spinner)findViewById(R.id.city);
        city.setOnItemSelectedListener(new SelectCity());
        
        
        button_left = (Button) findViewById(R.id.butt_left);
    	button_left.setVisibility(View.VISIBLE);
    	button_left.setText(getResources().getString(R.string.back));

    	button_right = (Button) findViewById(R.id.butt_right);
    	button_right.setVisibility(View.VISIBLE);
    	button_right.setText(getResources().getString(R.string.ok));
    	
    	
    	button_left.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			finish();
    		}
    	});

    	button_right.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			if(!EdjTools.isNull(cityname))
    			EdjTools.writeShare(City_SelectionActivity.this, "user_address", cityname);
    			
    			Toast.makeText(getApplicationContext(), cityname, 2000).show();
    			finish();
    		}
    	});
        
    }
   
    //选择改变状态
    class SelectProvince implements OnItemSelectedListener{
    	public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
    		// TODO Auto-generated method stub
    		//获得省份ID
    		pro_id=position;  		
    		city.setAdapter(getAdapter());
    		
    	}
    	public void onNothingSelected(AdapterView<?> arg0) {
    		// TODO Auto-generated method stub
    		
    	}
    }
    
    //城市 选择改变状态
    class SelectCity implements OnItemSelectedListener{
    	public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
    		// TODO Auto-generated method stub
    		 cityname=parent.getItemAtPosition(position).toString();
    		//选择提示
//   		Toast.makeText(getApplicationContext(), cityname+" "+getCityNum(position), 2000).show();
    	    
    	  
    	}
    	public void onNothingSelected(AdapterView<?> arg0) {
    		// TODO Auto-generated method stub
    		
    	}
    }
    
    /**
     * 返回 省份集合
     */
    public List<String> getProSet(){
       //打开数据库 
    	if(checkDatabase()){
    		File ff = new File(DB_PATH+"/",DB_NAME);
    	SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(ff, null);
 		Cursor cursor=db1.query("provinces", null, null, null, null, null, null);
 		while(cursor.moveToNext()){
 			String pro=cursor.getString(cursor.getColumnIndexOrThrow("name"));
 			proset.add(pro);
 		}
 		cursor.close();
 		db1.close();
    	return proset;
    	}
    	return null;
    }
    /**
     * 返回 城市集合
     */
    public List<String> getCitSet(int pro_id){
    	//清空城市集合
    	citset.clear();
    	if(checkDatabase()){
        	File ff = new File(DB_PATH+"/",DB_NAME);
       //打开数据库 
    	SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(ff, null);
 		Cursor cursor=db1.query("citys", null, "province_id="+pro_id, null, null, null, null);
 		while(cursor.moveToNext()){
 			String pro=cursor.getString(cursor.getColumnIndexOrThrow("name"));
 			citset.add(pro);
 		}
 		cursor.close();
 		db1.close();
    	return citset;
    	}
    	return null;
    }
    /**
     * 返回适配器
     */
    public ArrayAdapter<String> getAdapter(){
    	  ArrayAdapter<String> adapter1=new ArrayAdapter(this, android.R.layout.simple_spinner_item,getCitSet(pro_id));
          return adapter1;
    }
     /**
      * 返回城市编号  以便调用天气预报api
      */
    public long getCityNum(int position){
    	if(checkDatabase()){
		File ff = new File(DB_PATH+"/",DB_NAME);
    	SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(ff, null);
 		Cursor cursor=db1.query("citys", null, "province_id="+pro_id, null, null, null, null);
 		cursor.moveToPosition(position);
 		long citynum=Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("city_num")));
 		cursor.close();
 		db1.close();
 		return citynum;
    }
    return 0L;
    }
    
    
//    我经常用下面的步骤从assets文件夹中复制数据库

    private void copyDatabase() throws IOException{

            InputStream inputStream = tcontext.getAssets().open(DB_NAME);
            String dbCreatePath = DB_PATH+"/"+DB_NAME;
            OutputStream outputStream = new FileOutputStream(dbCreatePath);
            byte[] buffer = new byte[1024];
            int length;
            while((length = inputStream.read(buffer))>0){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
    }

    private boolean checkDatabase(){
        SQLiteDatabase checkDB = null;
        try {
            String dbPath = DB_PATH+"/"+DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null,     
                SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // TODO: handle exception
        }
        if(checkDB!=null){
            checkDB.close();}
        return checkDB != null ? true:false;

    }

    
}