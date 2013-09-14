package com.stockwidget;

import com.stockwidget.util.ConfigUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DataLoaderActivity extends Activity {
	TextView text ;
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.data);
        text = (TextView) findViewById(R.id.test);
        
		ConfigUtil.initial(this.getApplicationContext());
		ConfigUtil.restorePrefs();
		
//		Intent i = new Intent(this, GetDataService.class);
//		//i.putExtra("STOCK", "3008");
//		i.putExtra("STOCK", ConfigUtil.stockName);
//		startService(i);
		IStockDataService service = new GetYahooStockWebDataService();
		String result = service.getDataForResult(ConfigUtil.stockName);
		text.setText(result);
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
	}
}
