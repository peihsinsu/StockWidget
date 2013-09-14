package com.stockwidget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Looper;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.stockwidget.util.ConfigUtil;
import com.stockwidget.util.DateUtil;

public class SyncDataService extends Service {
	protected static final String TAG = P.TAG;
	private static final int BASE_COLOR = Color.BLACK;
	public RemoteViews widgetViews;
	public static String result;
	public static String result_name;
	public static String result_value;
	public static String result_diff;
	public static String result_updtime;
	public static String exception1;

	private Resources rs ;

	private static Thread oThread ;
	private static int currentWidgetLength;
	private static Map<Long, Integer> runMap = new HashMap<Long, Integer>();
	
	/**
	 * Prepare the system parameters
	 */
	private void init(){
		rs = this.getApplication().getResources();
		ConfigUtil.initial(this.getApplicationContext());
		ConfigUtil.restorePrefs();
		exception1 = rs.getString(R.string.ExceptionMsg1);
	}
	
	/**
	 * @see android.app.Service#onBind(Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Check current time is in configured period or not
	 * @return
	 */
	private boolean isInSyncPeriod(){
		boolean isInSyncPeriod;
		if(ConfigUtil.startTime == ConfigUtil.endTime)
			return true;
		
		Date from = DateUtil.getTodayOf(ConfigUtil.startTime);
		Date to = DateUtil.getTodayOf(ConfigUtil.endTime);
		isInSyncPeriod = DateUtil.isInPeriod(from, to, DateUtil.getSystemDate());
		
		if(isInSyncPeriod && ConfigUtil.isSkipWeekend)
			return isInSyncPeriod;
		return false;
	}
	
	private boolean isEmpty(String v){
		if(v == null || 
				"".equals(v)){
			return true;
		}
		return false;
	}
	
	/**
	 * To clean the thread cache
	 */
	private void cleanCache(){
		Iterator<Long> iter = runMap.keySet().iterator();
		while(iter.hasNext()){
			Long ts = (Long) iter.next();
			runMap.put(ts, 0);
		}
		
		if(oThread != null){
			try {
				oThread.interrupt();
				oThread.stop();
			} catch (Exception e){
				System.err.println("oThread interrupt error...." + e.getMessage() );
			}
		}
	}
	
	@Override
	public void onStart(final Intent intent, final int startId) {
		/* Initialize the configure */
		init();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		currentWidgetLength = appWidgetIds.length;
		Log.d(TAG, "appWidgetIds.length=" + appWidgetIds.length);
		
		//Clean all old caches
		cleanCache();
		if(appWidgetIds.length > 0){
			
			oThread = 
				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						long ts = new Date().getTime();
						loop:while (true) {
							Log.d(TAG, ">>>>>>>>>Loop [" + ts + "]:<<<<<<<<<<<");
							int flag = 1;
							if(runMap.get(ts) != null) {
								flag = runMap.get(ts);

								if(flag == 0){
									Log.d(TAG, "Thread [" + ts + "] go to stop...");
									runMap.remove(ts);
									break loop;
								}
								
							} else {
								runMap.put(ts, 1); //set as running
							}

							Log.d(TAG, "Thread pool dump..." + runMap);
							
							if(currentWidgetLength > 0 ){
								doService(intent, startId);
							} else if(currentWidgetLength <=0){
								Log.d(TAG, "Thread [" + ts + "] go to stop...");
								break loop;
							}
							sleep();
						}
					}
					
					private void sleep(){
						try{
							Thread.sleep(ConfigUtil.interval * 1000);
						} catch(IllegalArgumentException e) {
							//When parse exception of too big, set to default 1 day
							Toast.makeText(getApplicationContext(), rs.getString(R.string.sleepIntervalTooLong), Toast.LENGTH_LONG).show();
							ConfigUtil.interval = 86400;
							ConfigUtil.writePrefs();
							try {
								Thread.sleep(86400 * 1000);
							} catch (InterruptedException e1) {
								//e1.printStackTrace();
								System.err.println("Interrupt sleep when IllegalArgumentException...");
							}
						} catch (InterruptedException e) {
							//e.printStackTrace();
							System.err.println("Interrupt sleep...");
						}
					}
			});
			oThread.start();
		}
			
	}

	/** 
	 * clean up all thread to prevent memory leak 
	 * */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "SyncDataService destroy...");
		
		cleanCache();
	}
	
	
	/**
	 * Execute the data load and update widget
	 * @param intent
	 * @param startId
	 */
	public void doService(Intent intent, int startId) {
		boolean isWidgetClick = intent!=null?intent.getBooleanExtra(P.SERVICE_ACT_WIDGET_CLICK, false):false;
		boolean isFirstSync = intent!=null?intent.getBooleanExtra(P.SERVICE_ACT_FIRST_SYNC, false):false;
		if(isFirstSync)
			intent.removeExtra(P.SERVICE_ACT_FIRST_SYNC);
		
		Log.d(TAG, "[doService]"+isEmpty(ConfigUtil.ctx) +"::"+ 
				isInSyncPeriod()+";;"+
				ConfigUtil.firstSync+"::"+isFirstSync+"::"+
				isWidgetClick+"::"+startId);
		
		if(isEmpty(ConfigUtil.ctx) || 
				isInSyncPeriod()||
				ConfigUtil.firstSync||isFirstSync||
				isWidgetClick){ /* do update widget */
			Log.d(TAG, "Updating Widget Data...");
			updateWidgetViews(widgetViews);
			if(ConfigUtil.firstSync){
				Toast.makeText(this, rs.getString(R.string.UpdateWidgetData), Toast.LENGTH_SHORT).show();
				ConfigUtil.firstSync = false;
			}
			
			ConfigUtil.ctx = result;
			/*Persistence the first sync parameter*/
			ConfigUtil.writePrefs();
			
		} else { /* skip update widget */
			Log.d(TAG, "Not time interval, skip fetching data...");
			
			if(!isEmpty(ConfigUtil.ctx))
				updateWidgetViewsFromPref(widgetViews);
			
			if(ConfigUtil.firstSync){
				Toast.makeText(this, rs.getString(R.string.NotSyncIntervalMsg), Toast.LENGTH_SHORT).show();
				ConfigUtil.firstSync = false;
				/*Persistence the first sync parameter*/
				ConfigUtil.writePrefs();
			}
		}
	}
	
	/**
	 * Get the remote view
	 * @return
	 */
	private RemoteViews getWidgetViews(){
		if(widgetViews == null)
			widgetViews = new RemoteViews(this.getPackageName(), R.layout.widget_initial_layout);
		return widgetViews;
	}
	
	/**
	 * Update the widget content from url
	 * @param widgetViews
	 */
	private void updateWidgetViews(RemoteViews widgetViews){
		/* Prepare stock datas */
		boolean isSuccess = getStockData(this);
		Log.d(TAG, "Get data " + (isSuccess?"SUCCESS!":"FAILED!") );
		/* Refresh widget view */
		widgetViews = getWidgetViews();
		
		
//		widgetViews.add
		
		if(!isSuccess && (isEmpty(ConfigUtil.ctx) )){ //failed retrieve data, and no stored data
			widgetViews.setTextViewText(R.id.TextView01, exception1);
		} else {
//			widgetViews.setTextViewText(R.id.TextView01, result_name);
//			widgetViews.setTextViewText(R.id.TextView02, result_value);
//			widgetViews.setTextViewText(R.id.TextView03, result_diff);
			
			mergeView();
		}
		//widgetViews.setTextViewText(R.id.TextView04, result_updtime);
		//widgetViews.setTextViewText(R.id.UpdDate, getCurrentTime());
		
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        Intent syncService = new Intent(this, SyncDataService.class);
        //Put the widget update flag for force update
        syncService.putExtra(P.SERVICE_ACT_WIDGET_CLICK, true);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, syncService, 0);
        widgetViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent);
        /* Push update for this widget to the home screen */
        ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
        manager.updateAppWidget(thisWidget, widgetViews);
	}
	
	private void mergeView() {
		int i = 0;
		//TODO I still cannot find the better way to dynamic generate the stock data to widget view
		int[][] row = {
				{R.id.TextView01, R.id.TextView02, R.id.TextView03, R.id.TextView04},
				{R.id.TextView11, R.id.TextView12, R.id.TextView13, R.id.TextView14}, 
				{R.id.TextView21, R.id.TextView22, R.id.TextView23, R.id.TextView24}, 
				{R.id.TextView31, R.id.TextView32, R.id.TextView33, R.id.TextView34}, 
				{R.id.TextView41, R.id.TextView42, R.id.TextView43, R.id.TextView44}, 
				{R.id.TextView51, R.id.TextView52, R.id.TextView53, R.id.TextView54}, 
				{R.id.TextView61, R.id.TextView62, R.id.TextView63, R.id.TextView64}, 
				{R.id.TextView71, R.id.TextView72, R.id.TextView73, R.id.TextView74}, 
				{R.id.TextView81, R.id.TextView82, R.id.TextView83, R.id.TextView84}, 
				{R.id.TextView91, R.id.TextView92, R.id.TextView93, R.id.TextView94}};
				//{R.id.TextViewA1, R.id.TextViewA2, R.id.TextViewA3, R.id.TextViewA4}};
		
		if(result_name != null){

			//Clean all set fields
			for(int h = 0 ; h <= row.length - 1; h ++){
				for(int f = 0 ; f<4 ; f++){
					widgetViews.setTextViewText(row[h][f], "");
				}
			}
			
			for(String rname : result_name.split(P.SEP)){
				
				//Clear value
				for(int f = 0 ; f<4 ; f++){
					widgetViews.setTextViewText(row[i][f], "");
				}
				
				Log.d(TAG, "----->" + i);
				/* 1. Set the stock id */
				widgetViews.setTextViewText(row[i][0], rname);
				widgetViews.setTextColor(row[i][0], BASE_COLOR);
				widgetViews.setInt(row[i][0], "setHeight", P.ITEM_HEIGHT);
				
				/* 2. Set the stock price */
				widgetViews.setTextViewText(row[i][1], result_value.split(P.SEP)[i]);
				widgetViews.setTextColor(row[i][1], BASE_COLOR);
				widgetViews.setInt(row[i][1], "setHeight", P.ITEM_HEIGHT);
				
				/* 3. Set the diff with color */
				widgetViews.setTextViewText(row[i][2], result_diff.split(P.SEP)[i]);
				if(!ConfigUtil.isSingleColorMode) {
					if(result_diff.split(P.SEP)[i] !=null){
						if(result_diff.split(P.SEP)[i].startsWith("-"))
							widgetViews.setTextColor(row[i][2], Color.rgb(46, 139, 87)); //Green
							//widgetViews.setTextColor(row[i][2], Color.GREEN); //Green
						else
							widgetViews.setTextColor(row[i][2], Color.RED);
					}
				} else {
					widgetViews.setTextColor(row[i][2], BASE_COLOR);
				}
						
				widgetViews.setInt(row[i][2], "setHeight", P.ITEM_HEIGHT);
				
				/* 4. Set the update time for each stock */
				widgetViews.setTextViewText(row[i][3], result_updtime.split(P.SEP)[i]);
				widgetViews.setTextColor(row[i][3], BASE_COLOR);
				widgetViews.setInt(row[i][3], "setHeight", P.ITEM_HEIGHT);
				
				/* 5. Set the update time */
				widgetViews.setTextViewText(R.id.UpdDate, getCurrentTime());
				widgetViews.setTextColor(R.id.UpdDate, BASE_COLOR);
				
				i++;
			}
			
		}
	}

	/**
	 * Update the widget from pref data
	 * @param widgetViews
	 */
	private void updateWidgetViewsFromPref(RemoteViews widgetViews){
		/* Prepare stock datas */
		if(result != null && result.equals(ConfigUtil.ctx))
			return;
		
		Log.d(TAG, "Update widget from preference...");
		result = ConfigUtil.ctx;
		handleResult(result);
		/* Refresh widget view */
		widgetViews = getWidgetViews();
		/* Old function: Use one component for all stock by separator char 
		widgetViews.setTextViewText(R.id.TextView01, result_name);
		widgetViews.setTextViewText(R.id.TextView02, result_value);
		widgetViews.setTextViewText(R.id.TextView03, result_diff);
		widgetViews.setTextViewText(R.id.TextView04, result_updtime);
		widgetViews.setTextViewText(R.id.UpdDate, getCurrentTime());
		*/
		
		//New way to merge components to widget view
		mergeView();
		
		//Flush widget view
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        Intent syncService = new Intent(this, SyncDataService.class);
        syncService.putExtra(P.SERVICE_ACT_WIDGET_CLICK, true);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, syncService, 0);
        widgetViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent);
        
        /* Push update for this widget to the home screen */
        ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
        manager.updateAppWidget(thisWidget, widgetViews);
	}
	
	/**
	 * Get the current time
	 * @return
	 */
	private CharSequence getCurrentTime() {
		Date dt = new Date();
		int h = dt.getHours();
		int m = dt.getMinutes();
		return rs.getString(R.string.UpdateTime)+ format(h,2) + ":" + format(m,2);
	}
	
	/**
	 * format the number to string
	 * @param num
	 * @param digit
	 * @return
	 */
	private String format(int num, int digit){
		String s = String.valueOf(num);
		if(s.length() < digit){
			int diff = digit - s.length();
			for(int i = 0; i< diff; i++){
				s = "0" + s;
			}
			return s;
		} else {
			return s;
		}
	}

	/**
	 * To get the stock data from url
	 * @param context
	 * @return
	 */
	private boolean getStockData(Context context){
		Map<String, Stock> stocks = new TreeMap<String, Stock>();
		/*
		 * Get the TW stock data using yahoo web page 
		 */
		IStockDataService service = new GetYahooStockWebDataService();
		String[] twStocks = getTwStocks(ConfigUtil.stockName);
		if(twStocks != null && twStocks.length > 0)
			stocks.putAll(service.getDataForListResult(twStocks));
		
		/*
		 * Get the other stock data using yahoo stock api
		 */
		IStockDataService service2 = new GetYahooStockCvsService();
		String[] notTwStocks = getNotTwStocks(ConfigUtil.stockName);
		if(notTwStocks != null && notTwStocks.length > 0)
			stocks.putAll(service2.getDataForListResult(notTwStocks));
			
		convertToPref(stocks);
		if(isEmpty(result))
			result = ConfigUtil.ctx;
		//To update filed of widget components
		handleResult(result);
		return true;
	}
	
	/**
	 * 1. write list to cvs and save to result
	 * 2. write list to jsonctx of pref
	 * @param resultStock
	 */
	private void convertToPref(Map<String, Stock> stocks) {
		Log.d(TAG, "convert stock size:" + (stocks!=null?stocks.size():-1));
		if(stocks != null){
			try {
				//compare with the cache
				//1. reverse the cache to list
				Log.d(TAG, "ConfigUtil.jsonctx="+ConfigUtil.jsonctx);
				if(!isEmpty(ConfigUtil.jsonctx)){
					JSONArray ja = new JSONArray(ConfigUtil.jsonctx);
					for(int i = 0 ; i < ja.length() ; i++){
						
						JSONObject jobj = ja.getJSONObject(i);
						Stock s = convertJsonToStock(jobj);
						if(s != null && stocks.containsKey(s.getStockId())){
							Log.d(TAG, "s !=null and stocks contains key of s.stockId");
							Stock s2 = stocks.get(s.getStockId());
							//If get the data error, use the cached data
							if(s2 != null && isEmpty(s2.getCurrentPrice())){
								Log.d(TAG, "s2.currentPrice is null....");
								stocks.put(s.getStockId(), s);
							} else {
								Log.d(TAG, "using new data....");
							}
						}
					}
				}
				Log.d(TAG, ">>stock values: "+stocks.values());
				JSONArray ja2 = //new JSONArray(stocks.values());
					toJsonArray(stocks);
				if(ja2.length() > 0)
					ConfigUtil.jsonctx = ja2.toString();
				Log.d(TAG, ">>ja2 size:" + ja2.length());
				Log.d(TAG, ">>stock to json:" + ja2.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				ConfigUtil.jsonctx = "";
			}
			result = getStockListForCvsResult(stocks);
		} else if(ConfigUtil.jsonctx != null){
			
		}
		
	}

	/**
	 * Map data to json array
	 * @param stocks
	 * @return
	 */
	private JSONArray toJsonArray(Map<String, Stock> stocks) {
		JSONArray ja = new JSONArray();
		for(Stock s : stocks.values()){
			JSONObject jo = new JSONObject();
			if(s != null){
				try {
					jo.put("stockId", s.getStockId());
					jo.put("stockName", s.getStockName());
					jo.put("currentPrice", s.getCurrentPrice());
					jo.put("time", s.getTime());
					jo.put("todayMax", s.getTodayMax());
					jo.put("todayMin", s.getTodayMin());
					jo.put("diff", s.getDiff());
					//Add the extra value here...
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ja.put(jo);
			}
		}
		return ja;
	}

	/**
	 * Get the string from json object
	 * @param jobj
	 * @param key
	 * @return
	 */
	private String getStringFormJson(JSONObject jobj, String key){
		try {
			return jobj.getString(key);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		return null;
	}
	
	/**
	 * convert the json string to Stock object
	 * @param jobj
	 * @return
	 */
	private Stock convertJsonToStock(JSONObject jobj) {
		String stockId = getStringFormJson(jobj, "stockId");
		if(stockId == null)
			return null;
		Stock s = new Stock(stockId);
		s.setStockName(getStringFormJson(jobj, "stockName"));
		s.setCurrentPrice(getStringFormJson(jobj, "currentPrice"));
		s.setTime(getStringFormJson(jobj, "time"));
		//s.setTodayMax(jobj.getString("todayMax"));
		//s.setTodayMin(jobj.getString("todayMin"));
		s.setDiff(getStringFormJson(jobj, "diff"));
		//Add the extra value here...
		return s;
	}

	/**
	 * Separate the tw stocks
	 * @param stockName
	 * @return
	 */
	private String[] getTwStocks(String[] stockName) {
		List<String> twStocksList = new ArrayList<String>();
		if(stockName != null && stockName.length > 0)
		for(String s : ConfigUtil.stockName) {
			if(!isEmpty(s))
			if(s.toLowerCase().endsWith("tw")){
				twStocksList.add(s);
			} else{
				try{
					Integer.parseInt(s);
					twStocksList.add(s);
				} catch (Exception e) {
					//Do nothing
				}
			}
		}
		return getArrayFromList(twStocksList);
	}

	/**
	 * Separate the not-tw stocks
	 * @param stockName
	 * @return
	 */
	private String[] getNotTwStocks(String[] stockName) {
		List<String> notTwStocksList = new ArrayList<String>(); 
		for(String s : ConfigUtil.stockName) {
			if(!isEmpty(s) && !s.toLowerCase().endsWith("tw")){
				try{
					Integer.parseInt(s);
				} catch (Exception e) {
					notTwStocksList.add(s.toUpperCase());
				}
			}
		}
		return getArrayFromList(notTwStocksList);
	}
	
	/**
	 * Translate array from list
	 * @param strlist
	 * @return
	 */
	private String[] getArrayFromList(List<String> strlist) {
		if(strlist != null && strlist.size() > 0) {
			String[] theResult = new String[strlist.size()];
			int i = 0;
			for(String s : strlist){
				theResult[i] = s;
				i++;
			}
			return theResult;
		} else 
			return null;
	}

	/**
	 * Process the result string to every column
	 * 
	 * @param result Sample of source: 2330.TW,"TAIWAN SEMICON MA","5/18/2011",75.30,"-0.30"
	 */
	private boolean handleResult(String result) {
		Log.d(TAG, ">>>Get the result=" + result);
		if("Missing Symbols List.".equals(result) || result == null || "".equals(result)){
			return false;
		}
		reset();
		
		/*
		 * Handle the network issue, cannot get content... use old fetching data
		 */
		if(!isEmpty(ConfigUtil.ctx) && isEmpty(result)){
			result = ConfigUtil.ctx;
		} else {
			ConfigUtil.ctx = result;
		}
		
		String[] set = result.split("\\\n");
		
		for(String ori : set){
			String[] s = ori.replaceAll("\"", "").split(",");
			try{
				result_name = combine(result_name, s[0].replace(".TW", ""));
				result_value = combine(result_value, s[3]);
				result_diff = combine(result_diff, s[4]);
				result_updtime = combine(result_updtime, s[5].replaceAll("am", "").replaceAll("pm", ""));
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	/**
	 * Reset data, to prevent the data append to widget
	 */
	private void reset() {
		SyncDataService.result_name = "";
		SyncDataService.result_value = "";
		SyncDataService.result_diff = "";
		SyncDataService.result_updtime = "";
	}

	/**
	 * To add the string to the next line
	 * @param src
	 * @param add
	 * @return
	 */
	private String combine(String src, String add){
		if(src != null && src.length()>0){
			src = src.concat("\n");
		} else if(src == null){
			src = "";
		}
		
		return src.concat(add);
	}

	/**
	 * Get the stock list to cvs result
	 * @param stocks
	 * @return
	 */
	private String getStockListForCvsResult(Map<String, Stock> stocks){
		String result = "";
		if(stocks != null && stocks.size() > 0){
			Iterator iter = stocks.keySet().iterator();
			while(iter.hasNext()){
				String _stockId = (String) iter.next();
				Stock s = stocks.get(_stockId);
				if(s != null)
					result = result + s.toCsv() + "\n";
			}
		}
		
		return result;
	}
}
