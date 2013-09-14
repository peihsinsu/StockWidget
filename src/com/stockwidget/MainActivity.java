package com.stockwidget;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.stockwidget.util.AdSenseUtil;
import com.stockwidget.util.ConfigUtil;

/**
 * Main program for configuration
 * @author simonsu
 *
 */
public class MainActivity extends Activity {

	private EditText syncInterval;
	private EditText[] stockId = new EditText[ConfigUtil.stockSize];
	private TextView workTimeStart, workTimeEnd;
	private Button save;
	private ImageView help;
	private TableLayout table;
	private CheckBox isSkipWeekend, isSingleColorMode;

	private Resources rs ;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        rs = this.getResources();
        ConfigUtil.initial(this);
		ConfigUtil.restorePrefs();
		
		initialize();
		
        findViews();
        setListeners();
        
        // Google AdSense
		AdSenseUtil.addAdSense(this, R.id.adViewinconfig);
    }
	
    private void initialize() {
    	if(ConfigUtil.firstStart)
    		showInitText();
	}

	private void findViews(){
    	save = (Button) findViewById(R.id.save);
    	help = (ImageView) findViewById(R.id.help);
    	table = (TableLayout) findViewById(R.id.TableLayout01);
    	isSkipWeekend = (CheckBox) findViewById(R.id.skipWeekend);
    	isSingleColorMode = (CheckBox) findViewById(R.id.singleColorMode);
    	int s = 4;
    	int cur = s;
    	for(int i = 0 ; i < stockId.length ; i++){
    		TableRow tr = new TableRow(this);
    		cur ++;
    		//Separator
    		table.addView(createSpacer(this), cur);
    		//Text
    		tr.addView(createText(rs.getString(R.string.StockCode) + String.valueOf(i+1)));
    		
    		//Field
    		stockId[i] = new EditText(this);
    		stockId[i].setId(ConfigUtil.getStockFields()[i]);
    		stockId[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		stockId[i].setBackgroundDrawable(rs.getDrawable(R.drawable.input_bg));
    		stockId[i].setPadding(2, 2, 2, 2);
    		tr.addView(stockId[i]);
    		cur ++;
    		table.addView(tr, cur);
    	}

    	workTimeStart = (TextView) findViewById(R.id.workTimeStart);
    	workTimeEnd = (TextView) findViewById(R.id.workTimeEnd);
    	syncInterval = (EditText) findViewById(R.id.syncInterval);
    	
    	ConfigUtil.restorePrefs();
    	if(ConfigUtil.stockName != null){
    		for(int i = 0 ; i < stockId.length ; i++){
    			if(ConfigUtil.stockName[i] != null && ConfigUtil.stockName[i] != null)
    				stockId[i].setText(ConfigUtil.stockName[i]);
    		}
    		
    		if(ConfigUtil.startTime != null){
    			workTimeStart.setText(ConfigUtil.startTime);
    		}
    		if(ConfigUtil.endTime != null){
    			workTimeEnd.setText(ConfigUtil.endTime);
    		}
    		if(ConfigUtil.interval > 0){
    			syncInterval.setText(String.valueOf(ConfigUtil.interval));
    		}
    	}
    	
    	//Set the field of weekend setup
    	isSkipWeekend.setChecked(ConfigUtil.isSkipWeekend);
    	isSingleColorMode.setChecked(ConfigUtil.isSingleColorMode);
    }
    
    /**
     * Create description text to table raw
     * @param mainActivity
     * @param textString
     * @return
     */
    private TextView createText(String textString) {
    	TextView txt = new TextView(this);
		txt.setText(textString);
		txt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
		txt.setPadding(2, 2, 2, 2);
		txt.setTextColor(Color.BLACK);
		txt.setTextSize(12);
		return txt;
	}

    /**
     * To create the separator to table row
     * @param activity
     * @return
     */
	private View createSpacer(Context activity) {
    	  View spacer = new View(activity);

    	  spacer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 2));
    	  spacer.setBackgroundColor(R.color.line);

    	  return spacer;
    }
    
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    
    private void setListeners(){
    	//Listener for help content
    	help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showHelpText();
			}
		});
    	
    	//Listener for save pref
    	save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(ConfigUtil.stockName == null){
					ConfigUtil.stockName = new String[3];
				}
				
				for(int i = 0 ; i < ConfigUtil.stockSize ; i++){
					if(stockId[i].getText() != null && !"".equals(stockId[i].getText())){
						ConfigUtil.stockName[i] = String.valueOf(stockId[i].getText());
					}
				}
				
				if(workTimeStart.getText() != null && !"".equals(workTimeStart.getText())){
					ConfigUtil.startTime = ConfigUtil.formatTime(String.valueOf(workTimeStart.getText()));
				}
				if(workTimeEnd.getText() != null && !"".equals(workTimeEnd.getText())){
					ConfigUtil.endTime = ConfigUtil.formatTime(String.valueOf(workTimeEnd.getText()));
				}
				if(syncInterval.getText() != null && !"".equals(syncInterval.getText())){
					try{
						ConfigUtil.interval = Integer.parseInt(String.valueOf(syncInterval.getText()));
						if(ConfigUtil.interval < 30){
							Toast.makeText(getApplicationContext(), rs.getString(R.string.syncIntervalTooShort), Toast.LENGTH_SHORT).show();
						}
						
					} catch (Exception e){
						Toast.makeText(getApplicationContext(), rs.getString(R.string.syncIntervalNotValied), Toast.LENGTH_SHORT).show();
						ConfigUtil.interval = ConfigUtil.DEFAULT_INTERVAL;
					}
				} else {//set default sync to 30 seconds
					ConfigUtil.interval = ConfigUtil.DEFAULT_INTERVAL;
				}
				
				//Write the weekend show policy
				ConfigUtil.isSkipWeekend = isSkipWeekend.isChecked();
				ConfigUtil.isSingleColorMode = isSingleColorMode.isChecked();
				/*Show the sync message first time*/
				ConfigUtil.firstSync = true;
				ConfigUtil.writePrefs();
				Toast.makeText(v.getContext(), rs.getString(R.string.SavePrefDone), Toast.LENGTH_SHORT).show();
				

		        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(v.getContext());
				updateAppWidget(v.getContext(), appWidgetManager, mAppWidgetId);
				
		        finish();
			}
			
			private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
		            int appWidgetId) {
				ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
				int[] w = appWidgetManager.getAppWidgetIds(thisWidget);
				if(w.length <= 0) {
					Log.d(P.TAG, "No widget for update...");
					return;
				}
		        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_initial_layout);
		        Intent intent = new Intent(context, SyncDataService.class);
		        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
				views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);
				
				//First sync...
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(intent);
				appWidgetManager.updateAppWidget(appWidgetId, views);
		    }
    	});
    	
    	//Listener for time start
    	workTimeStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setupTime(workTimeStart);
			}
    	});
    	
    	//Listener for time stop
    	workTimeEnd.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setupTime(workTimeEnd);
			}
    	});
    }
    
    public final static int MENU_HELP = Menu.FIRST;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case R.id.menu_help:
    		showHelpText();
    		break;
    	case R.id.menu_stocksize:
    		setupStockSize();
    	 	break;
    	case R.id.menu_contact_me:
    		showInitText();
    		break;
    	}
    	return true;
    }
    
    private static EditText size;
    
    public void setupStockSize(){
		String title = rs.getString(R.string.menu_stocksize);
		String msg = rs.getString(R.string.SetupStockSizeDesc);
		
		Builder builder = new Builder(this);
    	builder.setTitle(title);
    	builder.setIcon(R.drawable.icon);
    	builder.setMessage(msg);
    	size = new EditText(this);
    	size.setText(getStoredStockSize());
    	builder.setView(size);
    	builder.setPositiveButton(rs.getString(R.string.OK), new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				//Toast.makeText(getApplicationContext(), ">>"+size.getText(), Toast.LENGTH_LONG).show();
				try {
					ConfigUtil.stockSize = Integer.parseInt(String.valueOf(size.getText()));
					if(ConfigUtil.stockSize > 10){
						sayToast(R.string.TooMuchStocks);
						ConfigUtil.stockSize = 10;
					}
					ConfigUtil.writePrefs();
					finish();
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
				} catch (NumberFormatException e){
					sayToast(R.string.NumberFormatException);
				}
			}
			
		});
    	builder.show();
    }
    
    private void sayToast(int textId){
    	Toast.makeText(getApplicationContext(), rs.getString(textId), Toast.LENGTH_LONG).show();
    }
    
    private CharSequence getStoredStockSize() {
		return String.valueOf(ConfigUtil.stockSize);
	}

    /**
     * Show the help text
     */
	public void showHelpText(){
		String title = rs.getString(R.string.msg_title);
		String msg = rs.getString(R.string.config_desc);
		
		Builder builder = new Builder(this);
    	builder.setTitle(title);
    	builder.setIcon(R.drawable.icon);
    	builder.setMessage(msg);
    	builder.setPositiveButton(rs.getString(R.string.OK), new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});
    	
    	builder.setNegativeButton(rs.getString(R.string.StockId), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				String url = rs.getString(R.string.StockWidgetWiki);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
    	builder.show();
    }
    
	/**
	 * Show the initial text when system start
	 */
	public void showInitText(){
		String title = rs.getString(R.string.first_start_msg_title);
		String msg = rs.getString(R.string.first_start_msg);
		
		Builder builder = new Builder(this);
    	builder.setTitle(title);
    	builder.setIcon(R.drawable.icon);
    	builder.setMessage(msg);
    	builder.setPositiveButton(rs.getString(R.string.first_start_btn), new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				ConfigUtil.firstStart = false;
				ConfigUtil.writePrefs();
			}
			
		});
    	builder.setNegativeButton(rs.getString(R.string.mail_me), new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				try {
					sendMail(new String[]{"simonsu.mail@gmail.com"}, null, null, "Stock Widget Usget Report", "Hi Simon<br/><br/>This is a Stock Widget response mail....<br/>", null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void sendMail(String[] receiver, String[] ccReceiver, String[] bccReceiver, String subject,
		    		String content, String[] attachmentPath) throws Exception{
		    	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("text/html");
				if(receiver == null || receiver.length == 0){
					//throw new CommonException("No receiver found!");
					return;
				}
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, receiver);
				if(ccReceiver != null && ccReceiver.length > 0)
					emailIntent.putExtra(android.content.Intent.EXTRA_CC, ccReceiver);
				if(bccReceiver != null && bccReceiver.length > 0)
					emailIntent.putExtra(android.content.Intent.EXTRA_BCC, bccReceiver);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(content));

				if(attachmentPath != null && attachmentPath.length > 0){
					emailIntent.setType("image/jpeg");
					for(String path : attachmentPath){
						emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
					}
				}
				//startActivity(emailIntent);
				startActivity(Intent.createChooser(emailIntent, "Send your email in:"));

		    }
			
		});
    	builder.show();
    }
	
    private TimePicker tps;
    
    public void setupTime(final TextView time){
		Builder builder = new Builder(this);
    	builder.setTitle(rs.getString(R.string.timepicker_title));
    	builder.setIcon(R.drawable.icon);
    	builder.setMessage(rs.getString(R.string.timepicker_msg));
    	tps = new TimePicker(this);
        tps.setIs24HourView(true);
        tps.setCurrentHour(Integer.parseInt(String.valueOf(time.getText()).split(":")[0]));
        tps.setCurrentMinute(Integer.parseInt(String.valueOf(time.getText()).split(":")[1]));
    	builder.setView(tps);
    	
    	builder.setPositiveButton(rs.getString(R.string.OK), new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				time.setText(ConfigUtil.formatTime(tps.getCurrentHour() + ":" + tps.getCurrentMinute()));
			}
		});
    	builder.show();
    }
}