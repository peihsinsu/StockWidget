package com.stockwidget.util;

import com.stockwidget.P;

import android.content.Context;
import android.content.SharedPreferences;


public class ConfigUtil {

	private static Context context;

	public static String[] stockName;

	public static int stockSize = 3;
	
	public static int[] stockFields;
	
	public static int[] getStockFields(){
		int base = 1000;
		int[] stocks = new int[stockSize];
		for(int i = 0 ; i < stocks.length ; i++) {
			stocks[i] = base + i;
		}
		return stocks;
	}
	
	public static String ctx, jsonctx, startTime, endTime;
	
    public static String PREF_STORE_NAME = P.TAG;
    
    public static boolean firstSync = true;
    
    public static boolean firstStart = true;
    
    public static int interval;
    
    public static final int DEFAULT_INTERVAL = 30;
    
    public static boolean isSkipWeekend = true;
    
    public static boolean isSingleColorMode = false;
    
    public static void initial(Context context){
    	ConfigUtil.context = context;
    }
    
	public static void writePrefs(){
		System.out.println("Writing preference..." + PREF_STORE_NAME);
    	SharedPreferences settings = context.getSharedPreferences(PREF_STORE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        int i = 0;
        for(String name : stockName){
        	if(i <= stockSize - 1){
	        	editor.putString("stockName_"+String.valueOf(i), name!=null?name.trim():"");
	        	i++;
        	}
        }
        
        editor.putString("startTime", startTime);
        editor.putString("endTime", endTime);
        editor.putString("jsonctx", jsonctx);
        editor.putInt("stockSize", stockSize);
        editor.putBoolean("firstStart", firstStart);
        editor.putBoolean("isSkipWeekend", isSkipWeekend);
        editor.putBoolean("isSingleColorMode", isSingleColorMode);
        if(isNotEmpty(ctx))
        	editor.putString("ctx", ctx);
        editor.putBoolean("firstSync", firstSync);
        editor.putInt("interval", interval);
        // Commit the edits!
        editor.commit();

    }
	
	private static boolean isNotEmpty(String str) {
		if(str != null && !"".equals(str))
			return true;
		return false;
	}

	public static void restorePrefs(){
    	System.out.println("ConfigureActivity restorePrefs:" + PREF_STORE_NAME);
    	SharedPreferences settings = context.getSharedPreferences(ConfigUtil.PREF_STORE_NAME, 0);
    	if(stockName == null || stockName.length != stockSize){
    		stockName = new String[stockSize];
    	}
    	if(stockName != null) 
    	for(int i=0;i<stockName.length;i++){
    		stockName[i] = settings.getString("stockName_"+i, null);
    	}
    	
    	startTime = settings.getString("startTime", "09:00");
    	endTime = settings.getString("endTime", "13:30");
    	startTime = formatTime(startTime);
    	endTime = formatTime(endTime);
    	stockSize = settings.getInt("stockSize", 3);
    	stockFields = getStockFields();
    	firstSync = settings.getBoolean("firstSync", true);
    	interval = settings.getInt("interval", DEFAULT_INTERVAL); //default 30 seconds to update
    	ctx = settings.getString("ctx", "");
    	jsonctx = settings.getString("jsonctx", "");
    	firstStart = settings.getBoolean("firstStart", true);
    	isSkipWeekend = settings.getBoolean("isSkipWeekend", true);
    	isSingleColorMode = settings.getBoolean("isSingleColorMode", false);
    }
	
	public static String formatTime(String time){
		if(time.split(":").length == 2){
			String h = time.split(":")[0];
			String m = time.split(":")[1];
			if(h.length() == 1)
				h = "0" + h;
			if(m.length() == 1)
				m = "0" + m;
			return h + ":" + m;
		}
		return "00:00";
		
	}
}
