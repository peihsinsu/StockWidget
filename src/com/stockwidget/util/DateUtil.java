package com.stockwidget.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateUtil {
	/**
	 * yyyyMMdd HHmmss
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_TIME = "yyyyMMdd HHmmss";
	
	/**
	 * yyyyMMdd HHmm
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_TIME2 = "yyyyMMdd HHmm";
	
	/**
	 * 
	 */
	public static final String DATE_FORMAT_YEAR = "yyyy";
	
	//Format for Year/Month/Day
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_TIME_DASH = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_DASH = "yyyy-MM-dd";
	/**
	 * yyyy-MM
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DASH = "yyyy-MM";
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_TIME_SLASH = "yyyy/MM/dd HH:mm:ss";
	/**
	 * yyyy/MM/dd HH:mm
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_TIME2_SLASH = "yyyy/MM/dd HH:mm";
	/**
	 * yyyy/MM/dd
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_SLASH = "yyyy/MM/dd";
	
	/**
	 * yyyyMMdd
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY = "yyyyMMdd";
	
	/**
	 * yyyyMM
	 */
	public static final String DATE_FORMAT_YEAR_MONTH = "yyyyMM";
	
	/**
	 * yyyy/MM
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_SLASH = "yyyy/MM";
	
	/**
	 * yyyy_MM_dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_TIME_UNDERLINE = "yyyy_MM_dd HH:mm:ss";
	/**
	 * yyyy_MM_dd
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_UNDERLINE = "yyyy_MM_dd";
	/**
	 * yyyy_MM
	 */
	public static final String DATE_FORMAT_YEAR_MONTH_UNDERLINE = "yyyy_MM";
	
	//Format for Day/Month/Year
	/**
	 * dd-MM-yyyy HH:mm:ss
	 */
	public static final String DATE_FORMAT_DAY_MONTH_YEAR_TIME_DASH = "dd-MM-yyyy HH:mm:ss";
	/**
	 * dd-MM-yyyy
	 */
	public static final String DATE_FORMAT_DAY_MONTH_YEAR_DASH = "dd-MM-yyyy";
	/**
	 * MM-yyyy
	 */
	public static final String DATE_FORMAT_MONTH_YEAR_DASH = "MM-yyyy";
	
	/**
	 * dd/MM/yyyy HH:mm:ss
	 */
	public static final String DATE_FORMAT_DAY_MONTH_YEAR_TIME_SLASH = "dd/MM/yyyy HH:mm:ss";
	/**
	 * dd/MM/yyyy
	 */
	public static final String DATE_FORMAT_DAY_MONTH_YEAR_SLASH = "dd/MM/yyyy";
	/**
	 * MM/yyyy
	 */
	public static final String DATE_FORMAT_MONTH_YEAR_SLASH = "MM/yyyy";
	
	/**
	 * dd_MM_yyyy HH:mm:ss
	 */
	public static final String DATE_FORMAT_DAY_MONTH_YEAR_TIME_UNDERLINE = "dd_MM_yyyy HH:mm:ss";
	/**
	 * dd_MM_yyyy
	 */
	public static final String DATE_FORMAT_DAY_MONTH_YEAR_UNDERLINE = "dd_MM_yyyy";
	/**
	 * MM_yyyy
	 */
	public static final String DATE_FORMAT_MONTH_YEAR_UNDERLINE = "MM_yyyy";
	/**
	 * MM-dd-yyyy
	 */
	public static final String DATE_FORMAT_MONTH_DAY_YEAR_DASH = "MM-dd-yyyy";
	/**
	 * MM-dd-yyyy HH:mm:ss
	 */
	public static final String DATE_FORMAT_MONTH_DAY_YEAR_TIME_DASH = "MM-dd-yyyy HH:mm:ss";

	/**
	 * Date format instance for Tool class use
	 */
	private static SimpleDateFormat df = new SimpleDateFormat();
	public static HashMap specialHolidays;
	protected static String bundlePath = "com.alchip.utils.general.date.DateUtil";
	
	/**
	 * default constructor
	 */
	public DateUtil() {
		super();
	}
	
	/**
	 * @param beginDate The period start
	 * @param endDate The period end
	 * @param inputDate The date to check is in period or not
	 * @return true for yes and vice versa
	 * 
	 */
	public static boolean isInPeriod(Date beginDate, Date endDate, Date inputDate) {
	
		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		Calendar cal3 = new GregorianCalendar();
		
		cal1.setTime(beginDate);
		cal2.setTime(endDate);
		cal3.setTime(inputDate);
		
		Calendar maxCalendar;
		boolean theSame = false;
		
		//Date order 
		if ( cal1.getTimeInMillis() > cal2.getTimeInMillis() ) {
			maxCalendar = cal1;
			cal1 = cal2;
			cal2 = maxCalendar;
		}else if ( cal1.getTimeInMillis() == cal2.getTimeInMillis() ) {
			theSame = true;
		}else {
			//Do not thing, without change
		}
		
		//Date Compare
		if ( theSame ) {
			if ( cal1.getTimeInMillis() == cal3.getTimeInMillis() ) {
				return true;
			}
		}else {
			if ( (cal1.getTimeInMillis() < cal3.getTimeInMillis()) 
				&& (cal3.getTimeInMillis() < cal2.getTimeInMillis()) ) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Is the given start date befor the end date
	 * @param startDate Start date
	 * @param endDate End date
	 * @return True for yes, and vice versa.
	 */
	public static boolean isValidInterval(Date startDate, Date endDate){
		if(startDate.getTime() > endDate.getTime()){
			return false;
		}
		return true;
	}
	
	/**
	 * Is the given thisDate before the compareDate date
	 * @param thisDate Start date
	 * @param compareDate End date
	 * @return True for yes, and vice versa.
	 */
	public static boolean isBefore(Date thisDate, Date compareDate){
		if(thisDate !=null && compareDate != null){
			if(thisDate.getTime() < compareDate.getTime()){
				return true;
			}
		} else {
			if(thisDate == null)
				System.err.println("thisDate cannot null!");
			if(compareDate == null)
				System.err.println("compareDate cannot null!");
		}
		return false;
	}
	
	public static boolean isBeforeEq(Date thisDate, Date compareDate){
		if(thisDate !=null && compareDate != null){
			if(thisDate.getTime() <= compareDate.getTime()){
				return true;
			}
		} else {
			if(thisDate == null)
				System.err.println("thisDate cannot null!");
			if(compareDate == null)
				System.err.println("compareDate cannot null!");
		}
		return false;
	}
	
	/**
	 * Is the given start date befor the end date
	 * @param startDate Start date
	 * @param endDate End date
	 * @return True for yes, and vice versa.
	 */
	public static boolean isAfter(Date thisDate, Date compareDate){
		if(thisDate !=null && compareDate != null){
			if(thisDate.getTime() > compareDate.getTime()){
				return true;
			}
		} else {
			if(thisDate == null)
				System.err.println("thisDate cannot null!");
			if(compareDate == null)
				System.err.println("compareDate cannot null!");
		}
		return false;
	}
	
	public static boolean isAfterEq(Date thisDate, Date compareDate){
		if(thisDate !=null && compareDate != null){
			if(thisDate.getTime() >= compareDate.getTime()){
				return true;
			}
		} else {
			if(thisDate == null)
				System.err.println("thisDate cannot null!");
			if(compareDate == null)
				System.err.println("compareDate cannot null!");
		}
		return false;
	}
	
	/**
	 * Return the system date
	 * @see #getTodayAs(String)
	 * @param psFormat
	 * @return
	 */
	public String getSystemDate(String psFormat) {
		String sDate = "";
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		
		//Set Date Format
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(psFormat);
			sDate = dateFormat.format(currentDate);
		}catch (Exception ex) {
			//IF Error Return year-month-day format
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DAY_DASH);
			sDate = dateFormat.format(currentDate);
		}
		
		return sDate;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp getSystemTimestamp(){
		return  new Timestamp(getSystemDate().getTime());
	}
	
	/**
	 * Usage: String dateString = DateUtil.convertDateToString(new Date(), "MM-dd-yyyy HH:mm:ss");
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertDateToString(Date date, String pattern){
		String result = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			result = dateFormat.format(date);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Usage: String dateString = DateUtil.convertDateToString(new Date(), "yyyy/MM/dd HH:mm:ss");
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date){
		return convertDateToString(date, DATE_FORMAT_YEAR_MONTH_DAY_TIME_SLASH);
	}
	
	/**
	 * Usage: String dateString = DateUtil.convertDateToString(new Date());
	 * Default pattern: yyyy/MM/dd HH:mm:ss
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertDateToString(Timestamp date, String pattern){
		if(date == null ) {
			return null;
		}
		String result = "";
		Date d = new Date(date.getTime());
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			result = dateFormat.format(d);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Usage: String dateString = DateUtil.convertDateToString(new Date());
	 * Default pattern: yyyy/MM/dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Timestamp date){
		return convertDateToString(date, DATE_FORMAT_YEAR_MONTH_DAY_TIME_SLASH);
	}
	
	/**
	 * To get system date, today
	 * @return Date - Today
	 */
	public static java.util.Date getSystemDate() {
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		
		return currentDate;
	}
	
	
	/**
	 * To get system date, today
	 * @return Date - Today
	 */
	public static java.util.Date getSystemDateFirstTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		Date currentDate = cal.getTime();

		return currentDate;
	}
	
	/**
	 * To get system date, today
	 * @return String - Today string
	 */
	public static String getSystemDateString(String pattern) {
		return convertDateToString(new Date(), pattern);
	}
	
	/**
	 * To get system date, today
	 * @return String - Today string with format yyyy/MM/dd HH:mm:ss
	 * @return
	 */
	public static String getSystemDateString() {
		return convertDateToString(new Date(), DATE_FORMAT_YEAR_MONTH_DAY_TIME_SLASH);	
	}
	
	
	/**
	 * Usage: DateUtil.isValiedDate("05-01-2007 16:31:26", "MM-dd-yyyy HH:mm:ss")
	 * Result: If result true for the date is validated, vice versa.
	 * Note: The "0" of each column cannot be ignore. 
	 * For example:
	 * "05-01-2007 16:31:26" != "5-1-2007 16:31:26"
	 * @param dateTimeString
	 * @param pattern
	 * @return
	 */
	public static boolean isValiedDate(String dateTimeString, String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			Timestamp ts = new java.sql.Timestamp(df.parse(dateTimeString).getTime());
			
			String converted = convertDate(new Date(ts.getTime()), pattern);
			System.out.println(converted);
			if(converted.equalsIgnoreCase(dateTimeString)){
				System.out.println("Time is right!!");
				return true;
			}else
				System.out.println("Time is wrong!!");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * To convert java.util.Date object using pattern
	 * <pre>
	 * Date and Time Pattern Sample 
	 * (Reference to {@link http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html})  
	 * "yyyy.MM.dd G 'at' HH:mm:ss z"  => 2001.07.04 AD at 12:08:56 PDT  
	 * "EEE, MMM d, ''yy"              => Wed, Jul 4, '01  
	 * "h:mm a"                        => 12:08 PM  
	 * "hh 'o''clock' a, zzzz"         => 12 o'clock PM, Pacific Daylight Time  
	 * "K:mm a, z"                     => 0:08 PM, PDT  
	 * "yyyyy.MMMMM.dd GGG hh:mm aaa"  => 02001.July.04 AD 12:08 PM  
	 * "EEE, d MMM yyyy HH:mm:ss Z"    => Wed, 4 Jul 2001 12:08:56 -0700  
	 * "yyMMddHHmmssZ"                 => 010704120856-0700  
	 * "yyyy-MM-dd'T'HH:mm:ss.SSSZ"    => 2001-07-04T12:08:56.235-0700  
	 * </pre>
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertDate(Date date, String pattern) {
		if (date != null) {
			df.applyPattern(pattern);
			return df.format(date);
		} else {
			return "";
		}
	}
	
	/**
	 * Return the date interval of double type
	 * @param dateTimeString1
	 * @param dateTimeString2
	 * @param pattern
	 * @return
	 */
	public static double getDateInterval(String dateTimeString1, String dateTimeString2, String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			Timestamp ts1 = new java.sql.Timestamp(df.parse(dateTimeString1).getTime());
			Timestamp ts2 = new java.sql.Timestamp(df.parse(dateTimeString2).getTime());
			double i = ts2.getTime() - ts1.getTime();
//			System.out.println(i);
			return i;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Return the date interval in day
	 * 含頭尾
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static double getDateInterval(Date startDate, Date endDate){
		double diff = endDate.getTime()/1000 - startDate.getTime()/1000;
		return diff / 86400 + 1;
	}
	
	public static double getDateInterval(Timestamp startDate, Timestamp endDate){
		double diff = endDate.getTime() - startDate.getTime();
		return diff / 86400000 + 1;
	}
	
	/**
	 * 不含頭尾
	 * @param dStart
	 * @param dEnd
	 * @return
	 * @throws Exception
	 */
	public static double getDurationInMinutes(Timestamp dStart,Timestamp dEnd) throws Exception {
        if (dStart==null || dEnd==null)
            return -1;
        long s = dStart.getTime();
        long e = dEnd.getTime();
        double during = e-s;
        during/=(1000*60);
        return during;
	}
	
	public static double getDurationInHours(Timestamp dStart,Timestamp dEnd) throws Exception {
        return getDurationInMinutes(dStart, dEnd)/60;
	}
	
	public static double getDurationInHours(Date sDt,Date eDt) throws Exception {
		Timestamp dStart = getTimestampFromDate(sDt);
		Timestamp dEnd = getTimestampFromDate(eDt);
        return getDurationInMinutes(dStart, dEnd)/60;
	}
	
	/**
	 * Count the days of given year, month
	 * @param year
	 * @param month
	 * @return
	 */
	public static double getTotalDaysOfMonth(int year, int month){
		Date startDate = DateUtil.getDateFromString(year + "-" + month, DateUtil.DATE_FORMAT_YEAR_MONTH_DASH);
		Date endDate = DateUtil.getDateFromString(year + "-" + (month +1), DateUtil.DATE_FORMAT_YEAR_MONTH_DASH);
		
		return getDateInterval(startDate, endDate) - 1;
	}
	
	/**
	 * Count the days of given year, month
	 * @param year
	 * @param month
	 * @return
	 */
	public static double getTotalDaysOfMonth(String year, String month){
		Date startDate = DateUtil.getDateFromString(year + "-" + month, DateUtil.DATE_FORMAT_YEAR_MONTH_DASH);
		Date endDate = DateUtil.getDateFromString(year + "-" + (new Integer(month).intValue() +1), DateUtil.DATE_FORMAT_YEAR_MONTH_DASH);
		//TODO Check the exception
		return getDateInterval(startDate, endDate) - 1;
	}
	
	/**
	 * Return the date interval with divide to 86400000 (one date)
	 * @param dateTimeString1
	 * @param dateTimeString2
	 * @param pattern
	 * @return
	 */
	public static double getDateIntervalOfDate(String dateTimeString1, String dateTimeString2, String pattern){
		return DateUtil.getDateInterval(dateTimeString1, dateTimeString2, pattern)/86400000;
	}
	
	/**
	 * Return the date interval with divide to 3600000 (one hour)
	 * @param dateTimeString1
	 * @param dateTimeString2
	 * @param pattern
	 * @return
	 */
	public static double getDateIntervalOfHour(String dateTimeString1, String dateTimeString2, String pattern){
		return DateUtil.getDateInterval(dateTimeString1, dateTimeString2, pattern)/3600000;
	}
	
	/**
	 * Get the given days ago Dats String
	 * @param i how many days ago.
	 * @return 
	 */
	public static String getDateDaysAgo(int days, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		long dateTimeM = Calendar.getInstance().getTimeInMillis() / 60000 - (long) (days * 24 * 60);
		return df.format(new Date(dateTimeM * 60000));
	}
	
	public static java.util.Date getDateDaysAgo(int days){
		long current = new Date().getTime();
		System.out.println("------>" + current);
		long daysAgoInMiles = current / 1000 - (long) (days * 24 * 60 * 60 ); 
		return new Date(daysAgoInMiles * 1000 );
	}

	public static java.util.Date getDateDaysAgo(Date date, int days){
		long current = date.getTime();
		long daysAgoInMiles = current / 1000 - (long) (days * 24 * 60 * 60 ); 
		return new Date(daysAgoInMiles * 1000);
	}
	/**
	 * Get date String of the given days after
	 * @param i how many days after.
	 * @return
	 */
	public static String getDateDaysAfter(int days, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		long dateTimeM = Calendar.getInstance().getTimeInMillis() / 60000
				+ (long) (days * 24 * 60);
		return df.format(new Date(dateTimeM * 60000));
	}
	
	public static java.util.Date getDateDaysAfter(int days){
		long current = new Date().getTime();
		long daysAgoInMiles = current / 1000 + (long) (days * 24 * 60 * 60 ); 
		return new Date(daysAgoInMiles);
	}
	
	public static java.util.Date getDateDaysAfter(Date date, int days){
		long current = date.getTime();
		long daysAgoInMiles = current / 1000 + (long) (days * 24 * 60 * 60 ); 
		return new Date(daysAgoInMiles * 1000);
	}
	
	public static java.util.Date getDateMinutesAfter(Date date, int minutes){
		long current = date.getTime();
		long daysAgoInMiles = current / 1000 + (long) (minutes * 60 ); 
		return new Date(daysAgoInMiles * 1000);
	}
	
	public static java.util.Date getDateMinutesAgo(Date date, int minutes){
		long current = date.getTime();
		long daysAgoInMiles = current / 1000 - (long) (minutes * 60 ); 
		return new Date(daysAgoInMiles * 1000);
	}
	
	public static java.util.Date getDateShift(Date date, int hours, int minutes, int seconds){
		long current = date.getTime();
		long daysAgoInMiles = 
			current / 1000 +
				(long) (hours * 60 * 60 ) +
				(long) (minutes * 60 ) +
				(long) (seconds ); 
		return new Date(daysAgoInMiles * 1000);
	}
	
	/**
	 * 取最近半點時間(往右取)
	 * EX: 8:01~8:30 = 8:30
	 *     8:31~9:00 = 9:00
	 * PS: 秒數會自動捨去
	 * @param date
	 * @return
	 */
	public static java.util.Date getRightClosedHalfHour(Date date){
		int shift = DateUtil.getMinute(date);
		int s = DateUtil.getSecond(date);
		Date date2 = null;
		if(shift == 0)
			date2 = DateUtil.getDateShift(date, 0, 0, -s);
		else if(shift > 30)
			date2 = DateUtil.getDateShift(date, 0, (60-shift), -s);
		else 
			date2 = DateUtil.getDateShift(date, 0, (30-shift), -s);
		return date2;
	}
	/**
	 * 取最近半點時間(往左取)
	 * EX: 8:00~8:29 = 8:00
	 *     8:30~8:59 = 8:30
	 * PS: 秒數會自動捨去
	 * @param date
	 * @return
	 */
	public static java.util.Date getLeftClosedHalfHour(Date date){
		int shift = DateUtil.getMinute(date);
		int s = DateUtil.getSecond(date);
		Date date2 = null;
		if(shift == 0)
			date2 = DateUtil.getDateShift(date, 0, 0, -s);
		else if(shift >= 30)
			date2 = DateUtil.getDateShift(date, 0, -(shift - 30), -s);
		else 
			date2 = DateUtil.getDateShift(date, 0, -(shift), -s);
		return date2;
	}
	
	/**
	 * 取最近半點時間(往右取)
	 * EX: 8:01~8:30 = 8:30
	 *     8:31~9:00 = 9:00
	 * PS: 秒數會自動捨去
	 * @param date
	 * @return
	 */
	public static java.sql.Timestamp getRightClosedHalfTS(Date date){
		Date dt = getRightClosedHalfHour(date);
		Timestamp ts = new Timestamp(dt.getTime());
		return ts;
	}
	
	/**
	 * 取最近半點時間(往左取)
	 * EX: 8:00~8:29 = 8:00
	 *     8:30~8:59 = 8:30
	 * PS: 秒數會自動捨去
	 * @param date
	 * @return
	 */
	public static java.sql.Timestamp getLeftClosedHalfTS(Date date){
		Date dt = getLeftClosedHalfHour(date);
		Timestamp ts = new Timestamp(dt.getTime());
		return ts;
	}
	
	/**
	 * Get the given days ago Dats String
	 * @param i how many days ago.
	 * @return 
	 */
	public static String getDateHoursAgo(int hours, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		long dateTimeM = Calendar.getInstance().getTimeInMillis() / 60000
				- (long) (hours * 60);
		return df.format(new Date(dateTimeM * 60000));
	}
	
	/**
	 * Get date String of the given days after
	 * @param i how many days after.
	 * @return
	 */
	public static String getDateHoursAfter(int hours, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		long dateTimeM = Calendar.getInstance().getTimeInMillis() / 60000
				+ (long) (hours * 60);
		return df.format(new Date(dateTimeM * 60000));
	}
	
	public static String getDateHoursAfterFromString(String dateTimeString, int hours, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			Timestamp ts1 = new java.sql.Timestamp(df.parse(dateTimeString).getTime());
			long dateTimeHoursAfter = ts1.getTime() / 3600000 + (hours );
			return df.format(new Date(dateTimeHoursAfter * 3600000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static int getDayOfWeek(long dayTime){
		String[] ids = TimeZone.getAvailableIDs(+8 * 60 * 60 * 1000);
		SimpleTimeZone pdt = new SimpleTimeZone(+8 * 60 * 60 * 1000, ids[0]);
		Calendar calendar = new GregorianCalendar(pdt);
		Date trialTime = new Date(dayTime);
		calendar.setTime(trialTime);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date getPrvWeekFirstDay(Date dt){
		long dayOfWeek = getDayOfWeek(dt);
		//System.out.println("-->" + dayOfWeek + ";" + (new Long(dayOfWeek).intValue() + 7 - 1));
		Date ndt = getDateDaysAgo(dt, new Long(dayOfWeek).intValue() + 7 - 1 );
		return ndt;
	}
	
	public static Timestamp getPrvWeekFirstDayTS(Date dt){
		return new Timestamp(getPrvWeekFirstDay(dt).getTime());
	}
	
	public static Date getPrvWeekLastDay(Date dt){
		return getDateDaysAfter(getPrvWeekFirstDay(dt),7);
	}
	
	public static Timestamp getPrvWeekLastDayTS(Date dt){
		return new Timestamp(getPrvWeekLastDay(dt).getTime());
	}
	
	public static int getDayOfWeek(Date trialTime){
		String[] ids = TimeZone.getAvailableIDs(+8 * 60 * 60 * 1000);
		SimpleTimeZone pdt = new SimpleTimeZone(+8 * 60 * 60 * 1000, ids[0]);
		Calendar calendar = new GregorianCalendar(pdt);
		calendar.setTime(trialTime);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * To get month last day. ex: 20-02-2007 result 28 
	 * @param date
	 * @return int - the month last day
	 */
	public static int getMonthDays(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int days = 0;
		
		switch (month) {
			case Calendar.JANUARY:
				days = 31;
				break;
			case Calendar.FEBRUARY:
				// Check Leap Year
				if ( isleapYear(year) ) {
					days = 29;
				}
				else {
					days = 28;
				}
				break;
			case Calendar.MARCH:
				days = 31;
				break;
			case Calendar.APRIL:
				days = 30;
				break;
			case Calendar.MAY:
				days = 31;
				break;
			case Calendar.JUNE:
				days = 30;
				break;
			case Calendar.JULY:
				days = 31;
				break;
			case Calendar.AUGUST:
				days = 31;
				break;
			case Calendar.SEPTEMBER:
				days = 30;
				break;
			case Calendar.OCTOBER:
				days = 31;
				break;
			case Calendar.NOVEMBER:
				days = 30;
				break;
			case Calendar.DECEMBER:
				days = 31;
				break;
		}
		
		return days;
	}
	
	/**
	 * Check the day is holiday or not
	 * @param dayTime
	 * @return
	 */
	public static boolean isHoliday(long dayTime){
		Calendar cal = Calendar.getInstance();
		if(Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK))
			return true;
		else if (Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK))
			return true;
		else
			return false;
		
	}
	
	
	/**
	 * Convert the time long value to Date string format of Day-Month 
	 * (The day and month donnt start with "0") 
	 * @param t1
	 * @return
	 */
	public static String convertLongToDateString(long t1){
		 return new Date(t1).getDate() + "-" + (new Date(t1).getMonth() + 1);
	}
	
	/**
	 * Check the input year is leap year or not.
	 * @param year
	 * @return
	 */
	public static boolean isleapYear(int year) {

		if ( (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Get the date long value from string.
	 * @param dayTime
	 * @param pattern
	 * @return
	 */
	public static long getDateLongFromString(String dayTime, String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Timestamp ts1 = null;
		try {
			ts1 = new java.sql.Timestamp(df.parse(dayTime).getTime());
			return ts1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * pattern is string like: yyyy/MM/dd HH:mm:ss
	 * @param dayTime
	 * @param pattern
	 * @return
	 */
	public static Date getDateFromString(String dayTime, String pattern){
		try {
			return new Date(DateUtil.getDateLongFromString(dayTime, pattern));
		}
		catch (Exception ex_date) {
			System.err.println("Date string \"" + dayTime + "\" not compatible with the pattern!");
			return null;
		}
	}
	
	/**
	 * pattern is string like: yyyy/MM/dd HH:mm:ss
	 * @param dayTime
	 * @param pattern
	 * @return
	 */
	public static Timestamp getTSFromString(String dayTime, String pattern){
		if(isNotEmpty(dayTime)){
			Date dt = getDateFromString(dayTime, pattern);
			if(dt != null)
				return new Timestamp(dt.getTime());
		}
		return null;
	}
	
	private static boolean isNotEmpty(String string){
		if(string != null && !"".equalsIgnoreCase(string))
			return true;
		return false;
	}
	
	/**
	 * Get the calendar object by date string and pattern
	 * pattern is string like: yyyy/MM/dd HH:mm:ss
	 * @param dayTime
	 * @param pattern
	 * @return
	 */
	public static Calendar getCalendarFromString(String dayTime, String pattern){
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(DateUtil.getDateLongFromString(dayTime, pattern)));
			return calendar;
		}
		catch (Exception ex_date) {
			System.err.println("Date string \"" + dayTime + "\" not compatible with the pattern!");
			return null;
		}
	}
	
	/**
	 * 當年1月1日
	 * @return
	 */
	public static Calendar getThisYearFirstDateC(){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
	}
	
	/**
	 * 當年1月1日
	 * @return
	 */
	public static Date getThisYearFirstDate(){
		return getThisYearFirstDateC().getTime();
	}
	
	/**
	 * 當年1月1日
	 * @return
	 */
	public static Calendar getThisYearFirstDateC(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
	}
	
	/**
	 * 當年1月1日
	 * @return
	 */
	public static Date getThisYearFirstDate(Date date){
		return getThisYearFirstDateC(date).getTime();
	}
	
	/**
	 * 當年1月1日
	 * @return
	 */
	public static Timestamp getThisYearFirstTS(Date date){
		return new Timestamp(getThisYearFirstDateC(date).getTimeInMillis());
	}
	
	public static Calendar getThisYearFirstDateC(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
	}
	
	public static Date getThisYearFirstDate(int year){
		return getThisYearFirstDateC(year).getTime();
	}
	
	public static Timestamp getThisYearFirstDateTS(int year){
		return getTimestampFromDate(getThisYearFirstDateC(year).getTime());
	}
	
	/**
	 * 當年12月31日的最後一個毫秒
	 * @return
	 */
	public static Calendar getThisYearLastDateC(){
		Calendar calendar = getNextYearFirstDateC();
		calendar.add(Calendar.MILLISECOND, -1);
        return calendar;
	}
	
	/**
	 * 當年12月31日的最後一個毫秒
	 * @return
	 */
	public static Date getThisYearLastDate(){
		return getThisYearLastDateC().getTime();
	}
	
	public static Timestamp getThisYearLastDateTS(int year){
		return getTimestampFromDate(getThisYearLastDateC(year).getTime());
	}
	/**
	 * 給定日期，查詢當年12月31日的最後一個毫秒(Calendat)
	 * @return
	 */
	public static Calendar getThisYearLastDateC(Date date){
		Calendar calendar = getNextYearFirstDateC(date);
		calendar.add(Calendar.MILLISECOND, -1);
        return calendar;
	}
	
	/**
	 * 給定日期，查詢當年12月31日的最後一個毫秒(Calendat)
	 * @return
	 */
	public static Date getThisYearLastDate(Date date){
		return getThisYearLastDateC().getTime();
	}
	
	/**
	 * 給定日期，查詢當年12月31日的最後一個毫秒(Timestamp)
	 * @return
	 */
	public static Timestamp getThisYearLastTS(Date date){
		return new Timestamp(getThisYearLastDateC().getTimeInMillis());
	}
	
	/**
	 * 給定年分(如：2008年)，查詢當年12/31日的最後一個毫秒
	 * @param year
	 * @return
	 */
	public static Calendar getThisYearLastDateC(int year){
		Calendar calendar = getNextYearFirstDateC(year);
		calendar.add(Calendar.MILLISECOND, -1);
        return calendar;
	}
	
	/**
	 * 給定年分(如：2008年)，查詢當年12/31日的最後一個毫秒
	 * @param year
	 * @return
	 */
	public static Date getThisYearLastDate(int year){
		return getThisYearLastDateC().getTime();
	}
	
	/**
	 * 隔年1月1日
	 * @return
	 */
	public static Calendar getNextYearFirstDateC(){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,12);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
	}
	
	public static Date getThisWeekFirstDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getThisWeekLastDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getThisMonthFirstDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getThisMonthLastDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, getMonth(date));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return getDateDaysAgo(calendar.getTime(), 1);
	}

	/**
	 * 隔年1月1日
	 * @return
	 */
	public static Date getNextYearFirstDate(){
		return getNextYearFirstDateC().getTime();
	}
	
	/**
	 * 隔年1月1日
	 * @return
	 */
	public static Calendar getNextYearFirstDateC(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
        calendar.set(Calendar.MONTH,12);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
	}
	
	/**
	 * 隔年1月1日
	 * @return
	 */
	public static Date getNextYearFirstDate(Date date){
		return getNextYearFirstDateC().getTime();
	}
	
	/**
	 * 隔年的1月1號
	 * @param year
	 * @return
	 */
	public static Calendar getNextYearFirstDateC(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year + 1);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
	}
	
	/**
	 * 隔年的1月1號
	 * @param year
	 * @return
	 */
	public static Date getNextYearFirstDate(int year){
		return getNextYearFirstDateC().getTime();
	}
	
	/**
	 * To split year and month from string that format is YYYYMM
	 * @param year_month Year+Month
	 * @return Map The Map contains two entity, one key is "Year" another is "Month" 
	 */
	public static Map splitYearMonth(String year_month) {
		HashMap map = new HashMap();
		
		try {
			int iYearMonth = Integer.parseInt(year_month);
			map = (HashMap)splitYearMonth(iYearMonth);
		}
		catch (Exception ex) {
			System.out.println("transfer month is wrong!");
		}
		
		return map;
	}
	
	/**
	 * To split year and month from integer that format is YYYYMM
	 * @param year_month Year+Month
	 * @return Map The Map contains two entity that type is String, one key is "Year" another is "Month" 
	 */
	public static Map splitYearMonth(int pYearMonth) {
		HashMap map = new HashMap();
		String year = "";
		String month = "";
		
		try {
			year = String.valueOf(pYearMonth/100);
			
			double ym = (double)pYearMonth/100;
			float m = (float)(ym - Math.floor(ym))*100;
			
			String year_month = String.valueOf( m );
			month = year_month.substring(0, year_month.indexOf("."));
			
			map.put("Year", year);
			map.put("Month", month);
		}
		catch (Exception ex) {
			System.out.println("transfer month is wrong!");
		}
		
		return map;
	}
	
	/**
	 * To get last month
	 * @param year_month Year+Month(YYYYMM)
	 * @return Map The Map contains two entity that type is String, one key is "Year" another is "Month"
	 */
	public static Map getLastYearMonth(String yearMonth) {
		HashMap ymHashMap = new HashMap();
		
		try {
			HashMap yearMap = (HashMap)splitYearMonth(yearMonth);
			String year = (String)yearMap.get("Year");
			String month = (String)yearMap.get("Month");
			Date theDate = getDateFromString("01-"+ month+ "-"+ year, DateUtil.DATE_FORMAT_DAY_MONTH_YEAR_DASH);
			
			ymHashMap = (HashMap)getLastYearMonth(theDate);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return ymHashMap;
	}
	
	/**
	 * To get last month
	 * @param Date 
	 * @return Map The Map contains two entity that type is String, one key is "Year" another is "Month"
	 */
	public static Map getLastYearMonth(Date date) {
		System.out.println("date="+ date.toString());
		
		HashMap ymHashMap = new HashMap();
		
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			System.out.println("cal date="+ cal.getTime().toString());
			
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1; // month start from 0 --> 1
			
			System.out.println("cal.YEAR="+ year);
			System.out.println("cal.MONTH="+ month);
			
			month = month - 1;
			
			if ( month < 1 ) {
				month = 12;
				year = year - 1;
			}
			
			ymHashMap.put("Year", String.valueOf(year));
			ymHashMap.put("Month", String.valueOf(month));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return ymHashMap;
	}
	
	/**
	 * To get last month
	 * @param String month
	 * @return String last month, January is 1, December is 12
	 */
	public static String getLastMonth(String month) {
		try {
			int iMonth = Integer.parseInt(month);
			return getLastMonth(iMonth);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * To get last month
	 * @param int month
	 * @return String last month, January is 1, December is 12
	 */
	public static String getLastMonth(int month) {
		try {
			month = month - 1;
			
			if ( month < 1 )
				month = 12;
				
			return String.valueOf(month);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * To log the message
	 * @param msg
	 */
	public static void log(String msg){
//		System.out.println("[DateUtil]"+msg);
	}
	
	/**
	 * To get the java.sql.Date form String with pattern
	 * @param dayTime
	 * @param pattern
	 * @return
	 */
	public static java.sql.Date getSqlDateFromString(String dayTime, String pattern){
		return new java.sql.Date(DateUtil.getDateFromString(dayTime, pattern).getTime());
	}
	
	/**
	 * Convert the given date, hour, minutes to sql date
	 * @param date Format: yyyy/MM/dd
	 * @param HH Hour
	 * @param MM Minutes
	 * @return
	 */
	public static java.sql.Date getSqlDateFromString(String date, String HH, String MM){
		return new java.sql.Date(DateUtil.getDateFromString(date + " " + HH + ":" + MM, DATE_FORMAT_YEAR_MONTH_DAY_TIME2_SLASH).getTime());
	}
	/**
	 * To get the java.sql.Timestamp form String with pattern
	 * @param dayTime
	 * @param pattern
	 * @return
	 */
	public static java.sql.Timestamp getSqlTimestampFromString(String dayTime, String pattern){
		return new java.sql.Timestamp(DateUtil.getDateFromString(dayTime, pattern).getTime());
	}
	/**
	 * Convert the given date, hour, minutes to sql timestamp
	 * @param date
	 * @param HH
	 * @param MM
	 * @return
	 */
	public static java.sql.Timestamp getSqlTimestampFromString(String date, String HH, String MM){
		return new java.sql.Timestamp(DateUtil.getTSFromString(date + " " + HH + ":" + MM, DATE_FORMAT_YEAR_MONTH_DAY_TIME2_SLASH).getTime());
	}
	/**
	 * To return the day of year
	 * @param dayTime
	 * @return
	 */
	public static int getDayOfYear(long dayTime){
		String[] ids = TimeZone.getAvailableIDs(+8 * 60 * 60 * 1000);
		SimpleTimeZone pdt = new SimpleTimeZone(+8 * 60 * 60 * 1000, ids[0]);
		Calendar calendar = new GregorianCalendar(pdt);
		Date trialTime = new Date(dayTime);
		calendar.setTime(trialTime);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * To return the day of year
	 * @param date
	 * @return
	 */
	public static int getDayOfYear(java.util.Date date){
		return getDayOfYear(date.getTime());
	}
	
	/**
	 * To return the day of year with %
	 * @param dayTime
	 * @return
	 */
	public static double getPercentageOfDayOfYear(long dayTime){
		return new Integer(getDayOfYear(dayTime)).doubleValue()/365;
	}
	
	/**
	 * To get today as special format
	 * @see #getSystemDate(String)
	 * @return String - Today
	 */
	public static String getTodayAs(String pattern) {
		
		String sToday = null;
		Date dayTime = getSystemDate();
		
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			sToday = df.format(dayTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sToday;
	}
	
	public static String getTodayAs() {
		return getTodayAs(DATE_FORMAT_YEAR_MONTH_DAY_TIME_SLASH);
	}
	
	/**
	 * If today is 2007/10/4
	 * To get the Min: Thu Oct 04 00:00:00 CST 2007
	 * @return
	 */
	public static java.util.Date getTodayMin(){
		return new Date(new Integer(new Date().getYear()), new Integer(new Date().getMonth()), new Date().getDate());
	}
	
	/**
	 * If today is 2007/10/4
	 * To get the Max: Thu Oct 05 00:00:00 CST 2007
	 * @return
	 */
	public static java.util.Date getTodayMax(){
		return new Date(new Integer(new Date().getYear()), new Integer(new Date().getMonth()), new Date().getDate()+1);
	}
	
	/**
	 * To get the year of given date
	 * @param date 
	 * @return the int value of year of the given date
	 */
	public static int getYear(java.util.Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(calendar.YEAR);
	}
	
	public static int getYear(java.sql.Timestamp date){
		Date d = new Date(date.getTime());
		return getYear(d);
	}
	
	public static int getMonth(java.sql.Timestamp date){
		Date d = new Date(date.getTime());
		return getMonth(d);
	}
	
	public static int getMonth(java.util.Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	public static int getDate(java.sql.Timestamp date){
		Date d = new Date(date.getTime());
		return getDate(d);
	}
	
	public static int getDate(java.util.Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE) ;
	}
	
	public static int getHour(java.sql.Timestamp date){
		Date d = new Date(date.getTime());
		return getHour(d);
	}
	
	public static int getHour(java.util.Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) ;
	}
	
	public static int getMinute(java.util.Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE) ;
	}
	
	public static int getMinute(java.sql.Timestamp date){
		Date d = new Date(date.getTime());
		return getMinute(d);
	}
	
	public static int getSecond(java.util.Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND) ;
	}
	
	public static int getSecond(java.sql.Timestamp date){
		Date d = new Date(date.getTime());
		return getSecond(d);
	}
	
	/**
	 * Return the year string of this year
	 * @return
	 */
	public static String getThisYearString(){
		return new Integer(getYear(new Date())).toString();
	}
	
	/**
	 * Return the year of this year
	 * @return
	 */
	public static int getThisYear(){
		return getYear(new Date());
	}
	
	public static int getThisMonth(){
		return getMonth(new Date());
	}
	
	/**
	 * EX: 200903
	 * @return
	 */
	public static String getThisYearMonth(){
		int year = getThisYear();
		int month = getThisMonth();
		
		if(month < 10){
			return year + "0" + month;
		} else {
			return year + "" + month;
		}
	}
	
	/**
	 * 計算今天的年月之調整
	 * @param yearDiff
	 * @param monthDiff
	 * @return
	 */
	public static String getThisYearMonth(int yearDiff, int monthDiff){
		int year = getThisYear();
		int month = getThisMonth();
		
		if(String.valueOf((month + monthDiff)).length() < 2){
			return (year + yearDiff) + "0" + (month + monthDiff);
		} else {
			return (year + yearDiff) + "" + (month + monthDiff);
		}
	}
	
	/**
	 * Return the year of this year
	 * @return
	 */
	public static Integer getThisYearForInteger(){
		return new Integer(getYear(new Date()));
	}
	
	/**
	 * To check the given date is in this year
	 *  True for yes
	 *  False for no
	 * @param date
	 * @return
	 */
	public static boolean isThisYear(Date date){
		if(date != null && DateUtil.getYear(date) == DateUtil.getThisYear()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * To check the given date is in this year
	 *  True for yes
	 *  False for no
	 * @param date
	 * @return
	 */
	public static boolean isLastYear(Date date){
		if(date != null && DateUtil.getYear(date) < DateUtil.getThisYear()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp getTimestampFromDate(java.util.Date date){
		return  new Timestamp(date.getTime());
	}
	
	/**
	 * Get the total days count of given date's year
	 * @param date
	 * @return
	 */
	public static int getTotalDaysOfYear(java.util.Date date){
		return getThisYearLastDateC(date).get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * Get the total days count of given date's year
	 * @param date
	 * @return
	 */
	public static int getTotalDaysOfYear(int year){
		return getThisYearLastDateC(year).get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * To trunc the select column of given date
	 *   YY: keep year and other trunc
	 *   MM: keep to month and other trunc
	 *   DD: keep to day and other trunc
	 *   HH: keep to hour and other trunc
	 *   MI: keep to minute and other trunc
	 *   SS: keep to second and other trunc
	 * @param date 
	 * @return the int value of year of the given date
	 */
	public static Date trunc(java.util.Date date, String fmt){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if("YY".equalsIgnoreCase(fmt)){
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if("MM".equalsIgnoreCase(fmt)){
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if("DD".equalsIgnoreCase(fmt)){
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if("HH".equalsIgnoreCase(fmt)){
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if("MI".equalsIgnoreCase(fmt)){
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if("SS".equalsIgnoreCase(fmt)){
			calendar.set(Calendar.MILLISECOND, 0);
		} else {
			System.err.println("[ERROR]This is not a valied format of trunc : " + fmt);
		}
		
		return calendar.getTime();
	}
	
	public static String getFormattedDateString(String year, String month, String day, String interval){
		
		if(new BigDecimal(month).intValue() < 10){
			month = "0" + new Integer(month);
		} else {
			month = "" + new BigDecimal(month).intValue();
		}
		
		if(new BigDecimal(day).intValue() < 10){
			day = "0" + new Integer(day);
		} else {
			day = "" + new BigDecimal(day).intValue();
		}
		
		return year + interval + month + interval + day;
		
	}
	
	public static Date getDateForYear(Date date, int year){
		String MMddhhmmss =  convertDateToString(date, "MMdd hhmmss");
		String dtStr = String.valueOf(year) + MMddhhmmss;
		return getDateFromString(dtStr, "yyyyMMdd hhmmss");
	}
	
	public static Date getDateByShift(java.util.Date date, int yShift, int mShift, int dShift, int hour, int minutes, int second, int millsecond){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + yShift);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + mShift);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + dShift);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minutes);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + second);
		calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + millsecond);
		return calendar.getTime();
	}
	
	
	public static Date getTodayOf(String hour, String minutes){
		String dtString = DateUtil.getTodayAs(DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_SLASH) + " " + hour + ":" + minutes;
		return DateUtil.getDateFromString(dtString, DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_TIME2_SLASH);
	}
	
	/**
	 * 
	 * @param time format as 12:30
	 * @return
	 */
	public static Date getTodayOf(String time){
		String dtString = DateUtil.getTodayAs(DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_SLASH) + " " + time;
		return DateUtil.getDateFromString(dtString, DateUtil.DATE_FORMAT_YEAR_MONTH_DAY_TIME2_SLASH);
	}
	
	public static void main(String args[]){
		//System.out.println(convertDateToString(getDateByShift(getSystemDate(), 1,1,1,0,0,0,0)));
		System.out.println(getTodayOf("1","30"));
	}
		
}
