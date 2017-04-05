package com.yscannerapp;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.yscannerapp.core.BaseActivity;

import java.net.URLEncoder;
import java.util.Vector;


public class WebViewActivity extends BaseActivity{
	public static final String BARCODE_CODE = "BARCODE_CODE";
	public static final String TOKEN = "TOKEN";
	private WebView webView;
	
	public String barcodeCode="";
	String token = "";
	public static Boolean inBackground = true;
	
	private Button clearCacheBtn;
	
	final Activity activity = this;
	public static final String LOGINPAGE = "";

    public static final String BaseURL = "http://34.250.107.106:3000/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Manager.setMainActivity(this);
		
		if(this.getIntent().getStringExtra(BARCODE_CODE)!=null){
			barcodeCode = this.getIntent().getStringExtra(BARCODE_CODE);
		}else{
			barcodeCode = "";
		}

//		if(Manager.getGCMToken(activity)!=null){
//			token = Manager.getGCMToken(activity);
//		}else{
//			token = "";
//		}

		clearCacheBtn = (Button)findViewById(R.id.clear_cache);
        
		clearCacheBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				webView.clearCache(true);
//				CookieSyncManager.createInstance(activity);         
//				 CookieManager cookieManager = CookieManager.getInstance();        
//				 cookieManager.removeAllCookie();
//				 webView.reload();
				
				Intent h = new Intent(activity, CameraActivity.class);
        		activity.startActivity(h);
			}
		});
		
		 
		webView = (WebView) mActivity.findViewById(R.id.webViewTerms); 
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		
		
		webView.setWebChromeClient(new WebChromeClient() {
	        public void onProgressChanged(WebView view, int progress)
	        {
	            activity.setTitle("Loading...");
	            activity.setProgress(progress * 100);

	            if(progress == 100)
	                activity.setTitle("Done");
	        }
	    });
		
		webView.setWebViewClient(new WebViewClient() {
	        @Override
	       public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
	       {
	           // Handle the error
	       }


	    	@Override
	    	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
	    	    handler.proceed(); // Ignore SSL certificate errors
	    	}


	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url)
	        {
	        	//TODO: if is loginpage add token, just if its not empty
	        	if(url.startsWith("com.yscannerapp")){
	        		if(url.contains("activity")){
	        			Intent h = null;
		        		if(url.contains("ScanBarcode")){
		        			h = new Intent(activity, CameraActivity.class);
		        		}else if(url.contains("MyMessagesActivity")){
		        			h = new Intent(activity, MyMessagesActivity.class);
		        		}
		        		activity.startActivity(h);
	        		}else if(url.contains("FuncClearCache")){
	        				webView.clearCache(true);
	        				CookieSyncManager.createInstance(activity);         
	        				CookieManager cookieManager = CookieManager.getInstance();        
	        				cookieManager.removeAllCookie();
	        				webView.reload();
	        		}
	        	}
	        	else{
	        		String url_par_del = "?";
	        		if(url.contains("?"))
	        			url_par_del = "&";
	        		StringBuffer buffer=new StringBuffer(url);
	        		Vector<Pair<String,String>> array = new Vector();  
	        		if(!barcodeCode.isEmpty())
	        		{
	        			buffer.append(url_par_del);
	        			array.add(new Pair("barcode",URLEncoder.encode(barcodeCode)));
	        		}

	        		buffer.append(createURLParams(array));
	        		view.loadUrl(buffer.toString());
	        	}
	            return true;
	        }
	    });
		
		
		reloadUrl(BaseURL+LOGINPAGE);
		
		//activity.startService(new Intent(activity, MessageReceivingService.class));
		
	}
	
	public void reloadUrl(String url)
	{
		String url_par_del = "?";
		if(url.contains("?"))
			url_par_del = "&";
		StringBuffer buffer=new StringBuffer(url+url_par_del);
		Vector<Pair<String,String>> array = new Vector();  
		if(!barcodeCode.isEmpty())
		{
			array.add(new Pair("barcode",URLEncoder.encode(barcodeCode)));
			//array.add(new Pair("barcode","Haim"));
		}
		
		buffer.append(createURLParams(array));
		final String the_url = buffer.toString();
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				webView.loadUrl(the_url);
			}
		});
	}
	

	@Override
	public int getLayoutXML() {
		return R.layout.webview_activity;
	}
	
	@Override
	protected void onStop() {
		inBackground = true;
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		inBackground = false;
		super.onResume();
	}

	public static String createURLParams(Vector<Pair<String,String>> array) {
		String str="";
		for(Pair pair : array) {
			if(str.length()>0)
				str+="&";
			//str+=pair.first+"="+pair.second;
			str = updateQueryString(str,pair.first.toString(),pair.second.toString());
		}
		return str;
	}


	public static String updateQueryString (String queryString, String name, String value) {
	if (queryString != null) {
	      queryString = queryString.replaceAll(name + "=.*?($|&)", "").replaceFirst("&$", "");
	   }
	 return addParameter(queryString, name, value);
	}

	public static String addParameter(String queryString, String name, String value) {      
	  return TextUtils.isEmpty(queryString) ? (name + "=" + value) : (queryString + "&" + name + "=" + value);

	}
	
}
