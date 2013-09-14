package com.stockwidget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class GetYahooStockWebDataService implements IStockDataService{
	public static final String TAG = P.TAG;
	/**
	 * Using in regular expression to find the html start tags and close tags
	 */
	public static final String RE_HTML_TAG = "<{1}[^>]{1,}>{1}";

	/**
	 * Yahoo stock web page
	 */
	private static final String url = "http://tw.stock.yahoo.com/q/q?s=%STOCK%";
	
	/**
	 * The stock data return of query
	 */
	private Map<String, Stock> stocks ;
	
	/**
	 * Getter of stocks
	 * @return
	 */
	public Map<String, Stock> getStocks(){
		return this.stocks;
	}
	
	/**
	 * Convert url pattern to real url 
	 * @param stock
	 * @return
	 */
	private String getUrl(String stock){
		Log.d(TAG, "Using URL:"+url.replaceAll("%STOCK%", stock));
		return url.replaceAll("%STOCK%", stock);
	}
	
	/**
	 * Get the final CSV data
	 */
	@Override
	public String getDataForResult(String[] stockIds) {
		log("Get stored stock size:" + stockIds.length + ";real size:" + getRealSize(stockIds));
		stocks = getDataForListResult(stockIds);
		return (getDataForResult(stocks));
	}
	
	/**
	 * Get the stock CSV data from stock object to string
	 * @param stocks
	 * @return
	 */
	private String getDataForResult(Map<String, Stock> stocks){
		String result = "";
		if(stocks != null && stocks.size() > 0){
			Iterator iter = stocks.keySet().iterator();
			while(iter.hasNext()){
				Stock s = (Stock) iter.next();
				if(s != null)
					result = result + s.toCsv() + "\n";
			}
		}
		
		return result;
	}
	
	/**
	 * Check the size of input stock ids
	 * @param stockIds
	 * @return
	 */
	private int getRealSize(String[] stockIds) {
		int cnt = 0;
		for(int i = 0; i< stockIds.length ; i++){
			if(stockIds[i] != null && !"".equals(stockIds[i]))
				cnt ++;
		}
		return cnt;
	}

	/**
	 * Get multi-stock data (already parse to Stock object) from given stock ids
	 * @param stock
	 * @return
	 */
	@Override
	public Map<String, Stock> getDataForListResult(String[] stock){
		Map<String, Stock> stocks = new TreeMap<String, Stock>();
		int i = 0;
		for(String stockId : stock){
			if(stockId != null && !"".equals(stockId)){
				Stock _stock = getData(stockId);
				stocks.put(stockId, _stock);
			}
			i++;
		}
		
		return stocks;
	}
	
	/**
	 * Get one stock data (already parse to Stock object) from stock id
	 * @param stock
	 * @return
	 */
	private Stock getData(String stock){
		URI myURL = null;
		try {
			stock = stock.toLowerCase().replaceAll("\\.tw", "");
			myURL = new URI(getUrl(stock));
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			log("URISyntaxException:" + e1.getMessage());
			return null;
		}
		
		// The HTTP objects
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(myURL);
		HttpResponse httpResponse;
		
		try {
			httpResponse = httpClient.execute(getMethod);
			// You might want to check response.getStatusLine().toString()
		
			HttpEntity entity = httpResponse.getEntity();
		
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader( new InputStreamReader(instream, "BIG5"));
				List<String> htmllist = new ArrayList<String>();
				String line = null;
				int cnt = 0;
				try {
					boolean start = false;
					while ((line = reader.readLine()) != null && cnt <= 10) {

						if(line.contains("/q/bc?s=" + stock)){
							start = true;
						}
						
						if(start) {
							String str = line.trim();
							if(!str.startsWith("<"))
								str = "<" + str;
							htmllist.add(str);
							cnt++;
						}
						
					}
					
					return formatResult(stock, htmllist);
				} catch (IOException e) {
					Log.e(TAG, "IOException1:" + e.getMessage());
				} finally {
					try {
						instream.close();
					} catch (IOException e) {
						Log.e(TAG, "Close stream exception:"+ e.getMessage());
					}
				}
			
			}
		} catch (ClientProtocolException e) {
			Log.e(TAG, "ClientProtocolException:"+e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException:" + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Process the response of html text list to Stock object 
	 * @param stockId
	 * @param htmllist
	 * @return
	 */
	private Stock formatResult(String stockId, List<String> htmllist) {
		if(stockId == null || "".equals(stockId))
			return null;
		if(htmllist == null || htmllist.size() <=0)
			return null;
		Stock stock = new Stock(stockId);
		Iterator<String> iter = htmllist.iterator();
		int i = 0;
		while(iter.hasNext()){
			String data = (String) iter.next();
			if(data.indexOf("<br")>=0){
				data = data.substring(0, data.indexOf("<br"));
				data = data.replaceAll(stockId, "");
			}
			stock.put(i,data.replaceAll(RE_HTML_TAG, ""));
			i++;
		}
		
		return stock;
	}

	/**
	 * Do log
	 * @param msg
	 */
	private void log(String msg){
		Log.d(TAG, msg);
	}
}
