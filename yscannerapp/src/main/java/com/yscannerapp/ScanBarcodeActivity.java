package com.yscannerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ScanBarcodeActivity extends Activity {

	//UI instance variables
	private Button scanBtn;
	private Button doneBtn;
	private TextView formatTxt, contentTxt;
	String scanContent="";
	String scanFormat="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//instantiate UI items
		scanBtn = (Button)findViewById(R.id.scan_button);
		formatTxt = (TextView)findViewById(R.id.scan_format);
		contentTxt = (TextView)findViewById(R.id.scan_content);
		doneBtn = (Button)findViewById(R.id.done_button);
		//listen for clicks
		scanBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					//instantiate ZXing integration class
					//IntentIntegrator scanIntegrator = new IntentIntegrator(ScanBarcodeActivity.this);
					//start scanning
					//scanIntegrator.initiateScan();
				final Intent h = new Intent(ScanBarcodeActivity.this, CameraActivity.class);
				ScanBarcodeActivity.this.startActivity(h);
				
			}
		});
		
		doneBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Intent h = new Intent(ScanBarcodeActivity.this, WebViewActivity.class);
				//h.putExtra(WebViewActivity.SHORT_URL, shortURL);
				//h.putExtra(WebViewActivity.LONG_URL, longURL);
				//h.putExtra(WebViewActivity.LOGO, logo);
				h.putExtra(WebViewActivity.BARCODE_CODE, scanContent);
				ScanBarcodeActivity.this.startActivity(h);
				
			}
		});
	}

	

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		//check we have a valid result
		if (scanningResult != null) {
			//get content from Intent Result
			scanContent = scanningResult.getContents();
			//get format name of data scanned
			scanFormat = scanningResult.getFormatName();
			//output to UI
			formatTxt.setText("FORMAT: "+scanFormat);
			contentTxt.setText("CONTENT: "+scanContent);
		}
		else{
			//invalid scan data or scan canceled
			Toast toast = Toast.makeText(getApplicationContext(), 
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
