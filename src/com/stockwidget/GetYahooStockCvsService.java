package com.stockwidget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class GetYahooStockCvsService implements IStockDataService{
	private static final String TAG = P.TAG;
	private static final String stockUrl = "http://finance.yahoo.com/d/quotes.csv?s=STOCKS&f=snd1l1c6t1";
	private String[] stocks;
	@Override
	public String getDataForResult(String[] stockIds) {
		this.stocks = stockIds;
		return getData();
	}
	
	public Map<String, Stock> getDataForListResult(String[] stockIds){
		this.stocks = stockIds;
		Map<String, Stock> stocks = new TreeMap<String, Stock>();
		URI myURL = null;
		try {
			if(stockIds == null || getUrl() == null){
				Log.d(TAG, "Find null non tw stock selected...");
				return stocks;
			}
			String u = getUrl();
			Log.d(TAG, "Stock Query URL:" + u);
			myURL = new URI(u);
		} catch (URISyntaxException e1) {
			Log.d(TAG, e1.getMessage());
			return null;
		}
		
		/* The HTTP objects */
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(myURL);
		
		HttpResponse httpResponse;
		
		/* The query result */
		try {
			httpResponse = httpClient.execute(getMethod);
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader( new InputStreamReader(instream));
				String line = null;
				try {
					//    0               1              2        3        4      5
					//"2498.TW","HTC CORPORATION T","6/20/2011",997.00,"-1.00","1:30am"
					_inner:while ((line = reader.readLine()) != null) {
						String[] rec = line.split(",");
						if(rec.length <= 1){
							continue _inner;
						}
						Stock stock = new Stock(rec[0]);
						stock.setStockName(rec[1]);
						stock.setTime(rec[5]);
						stock.setCurrentPrice(rec[3]);
						stock.setDiff(rec[4]);
						stocks.put(rec[0], stock);
					} 
				} catch (IOException e) {
					Log.d(TAG, e.getMessage());
				} finally {
					try {
						instream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (ClientProtocolException e) {
			Log.d(TAG, e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
		}
		
		return stocks;
	}
	
	private String getData(){
		String result;
		URI myURL = null;
		try {
			String u = getUrl() + "&time=" + new Date().getTime();
			if (u == null)
				return null;
			Log.d(TAG, "Stock Query URL:" + u);
			myURL = new URI(u);
		} catch (URISyntaxException e1) {
			Log.d(TAG, e1.getMessage());
		}
		
		/* The HTTP objects */
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(myURL);
		
		HttpResponse httpResponse;
		
		/* The query result */
		try {
			httpResponse = httpClient.execute(getMethod);
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader( new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					if(sb != null && !"".equals(sb.toString())){
						result = sb.toString();
						return result;
					} 
				} catch (IOException e) {
					Log.d(TAG, e.getMessage());
				} finally {
					try {
						instream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (ClientProtocolException e) {
			Log.d(TAG, e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
		}
		
		return null;
	}
	

	/**
	 * Get the data url combination from pref
	 * @param url
	 * @return
	 */
	private String getUrl() {
		String v = "";
		Log.d(TAG, "Got url pattern=" + stockUrl + ";this.stocks.length="+(this.stocks!=null?this.stocks.length:-1));
		if(this.stocks != null && this.stocks.length > 0){
			for(String name: this.stocks){
				Log.d(TAG, "Got name..." + name);
				if(name != null && !"".equals(name)){
					v += ("+" + name);
				}
			}
			if(v.startsWith("+")){
				v = v.substring(1);
			}
			Log.d(TAG, "Final v=" + v);
			v = URLEncoder.encode(v);
			Log.d(TAG, "Final v after encoded=" + v);
			return stockUrl.replaceAll("STOCKS", v) + "&time=" + new Date().getTime();
		} 
		return null;
	}
	

}
