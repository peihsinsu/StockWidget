package com.stockwidget;

import java.math.BigDecimal;
/**
 * Stock VO to contain the stock information
 * @author simonsu
 *
 */
public class Stock {
	private String stockId;
	private String stockName;
	private String time;
	private String currentPrice;
	private String buyIn;
	private String saleOut;
	private String diff;
	private String yesturdayEndPrice;
	private String todayStartPrice;
	private String todayMax;
	private String todayMin;
	private String total;
	public Stock(String stockId) {
		this.stockId = stockId;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getBuyIn() {
		return buyIn;
	}
	public void setBuyIn(String buyIn) {
		this.buyIn = buyIn;
	}
	public String getSaleOut() {
		return saleOut;
	}
	public void setSaleOut(String saleOut) {
		this.saleOut = saleOut;
	}
	public void setDiff(String diff){
		this.diff = diff;
	}
	public String getDiff() {
		if(this.diff == null || "".equals(this.diff) || "null".equalsIgnoreCase(this.diff))
			if(currentPrice != null && !"".equals(currentPrice))
				return new BigDecimal(currentPrice).subtract(new BigDecimal(yesturdayEndPrice)).toString();
			else
				return "0";
		else
			return this.diff;
	}
	public String getYesturdayEndPrice() {
		return yesturdayEndPrice;
	}
	public void setYesturdayEndPrice(String yesturdayEndPrice) {
		this.yesturdayEndPrice = yesturdayEndPrice;
	}
	public String getTodayStartPrice() {
		return todayStartPrice;
	}
	public void setTodayStartPrice(String todayStartPrice) {
		this.todayStartPrice = todayStartPrice;
	}
	public String getTodayMax() {
		return todayMax;
	}
	public void setTodayMax(String todayMax) {
		this.todayMax = todayMax;
	}
	public String getTodayMin() {
		return todayMin;
	}
	public void setTodayMin(String todayMin) {
		this.todayMin = todayMin;
	}
	public void put(int i, String d) {
		//Log.d(P.TAG, "Putting " + i + ": " +d);
		switch(i){
		case 0:
			this.setStockName(d);
			break;
		case 1:
			this.setTime(d);
			break;
		case 2:
			this.setCurrentPrice(d);
			break;
		case 3:
			this.setBuyIn(d);
			break;
		case 4:
			this.setSaleOut(d);
			break;
//		case 5:
//			try {
//				this.setDiff(new String(d.getBytes("BIG5"), "UTF8"));
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			//this.setDiff(d);
//			break;
		case 6:
			this.setTotal(d);
			break;
		case 7:
			this.setYesturdayEndPrice(d);
			break;
		case 8:
			this.setTodayStartPrice(d);
			break;
		case 9:
			this.setTodayMax(d);
			break;
		case 10:
			this.setTodayMin(d);
			break;
		default:
			break;
		}
	}
	
	public String toString(){
//		try {
//			Class c = Class.forName(this.getClass().getName());
//			Field[] fs = c.getFields();
//			c.getMethods();
//			for(Field f : fs){
//				f.
//			}
//			
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		String result = "{";
		result += "\"StockId\":" + this.getStockId();
		result += ",\"StockName\":" + this.getStockName();
		result += ",\"Time\":" + this.getTime();
		result += ",\"CurrentPrice\":" + this.getCurrentPrice();
		result += ",\"BuyIn\":" + this.getBuyIn();
		result += ",\"SaleOut\":" + this.getSaleOut();
		result += ",\"Diff\":" + this.getDiff();
		result += ",\"Total\":" + this.getTotal();
		result += ",\"EndPrice\":" + this.getYesturdayEndPrice();
		result += ",\"TodayStart\":" + this.getTodayStartPrice();
		result += ",\"TodayMax\":" + this.getTodayMax();
		result += ",\"TodayMin\":" + this.getTodayMin();
		result += "}";
		return result;
	}
	
	/**
	 * Format to :
	 * "2498.TW","HTC CORPORATION T","6/20/2011",997.00,"-1.00","1:30am"
	 * "3008.TW","LARGAN PRECISION ","6/20/2011",900.00,"-15.00","1:30am"
	 * @return
	 */
	public String toCsv(){
		String S = "\"";
		String C = ",";
		String result = "";
		result = result + S + this.getStockId() + S + C;
		result = result + S + this.getStockName() + S + C;
		result = result + S + "DATE" + S + C;
		result = result + S + this.getCurrentPrice() + S + C;
//		result += ",Buy In:" + this.getBuyIn();
//		result += ",Sale Out:" + this.getSaleOut();
		result = result + S + this.getDiff() + S +C;
		result = result + S + this.getTime() + S + C;
//		result += ",Total:" + this.getTotal();
//		result += ",End Price:" + this.getYesturdayEndPrice();
//		result += ",Today Start:" + this.getTodayStartPrice();
//		result += ",Today Max:" + this.getTodayMax();
//		result += ",Today Min:" + this.getTodayMin();
		return result;
	}
}
