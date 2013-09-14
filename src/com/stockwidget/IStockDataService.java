package com.stockwidget;

import java.util.Map;

/**
 * The stock retrieve interface
 * @author simonsu
 *
 */
public interface IStockDataService {

	/**
	 * Get the stock data by stock ids
	 * The return will be string data separate by ","
	 * @param stockIds
	 * @return
	 */
	public abstract String getDataForResult(String[] stockIds);
	
	/**
	 * Get the stock data by stock ids
	 * The return data will wrappered to Map<id, Stock>
	 * @param stockIds
	 * @return
	 */
	public abstract Map<String, Stock> getDataForListResult(String[] stockIds);

}
