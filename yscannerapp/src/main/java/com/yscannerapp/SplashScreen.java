package com.yscannerapp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yscannerapp.core.BaseActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SplashScreen extends BaseActivity {

	public static final int splashTime = 3000;
	LinearLayout splashPanel;
	public boolean isStartRegistered=false;
	protected boolean bClose  = true;
	protected boolean bInit  = true;
	private int bOpenScreen=0;    
	private boolean isAutoRegister = false;

	public Handler mHandler = new Handler();
	private Button start;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
		        init();
			}
		}).start();
			
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void init() {
		
		long beforetime = System.currentTimeMillis();
		SplashScreen.this.startService(new Intent(SplashScreen.this, MessageReceivingService.class));
		long aftertime = System.currentTimeMillis();
		long timeDuration = aftertime-beforetime;
		if(timeDuration<1500) {
			try {
				Thread.sleep(1500-timeDuration);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	

	@Override
	public int getLayoutXML() {
		return R.layout.splash_screen;
	}

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		}
		return super.onKeyDown(keyCode, event); 
	}
	
}
