package com.stockwidget.util;

import android.app.Activity;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class AdSenseUtil {
	public static boolean isTest = false;
	
	public static void addAdSense(Activity act, int adview){
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) act.findViewById(adview);
	    AdRequest request = new AdRequest();
	    
	    if(isTest)
	    	request.setTesting(true);
	    
	    adView.loadAd(request);
	}

}
