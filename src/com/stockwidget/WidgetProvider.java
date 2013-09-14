package com.stockwidget;

import com.stockwidget.util.ConfigUtil;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetProvider extends AppWidgetProvider {
	protected static final String TAG = P.TAG;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d(TAG, "Inside WidgetProvider onReceive.....");
		// To prevent any ANR timeouts, we perform the update in a service
		context.stopService(intent);
	}
	
	@Override
	public void onUpdate(final Context context,
			final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		Log.d(TAG, "Inside WidgetProvider onUpdate.....");
		ConfigUtil.firstSync = true;
		// To prevent any ANR timeouts, we perform the update in a service
		Intent intent = new Intent(context, SyncDataService.class);
		intent.putExtra(P.SERVICE_ACT_FIRST_SYNC, true);
		context.startService(intent);
	}
	
}