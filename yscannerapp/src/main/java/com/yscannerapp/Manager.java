package com.yscannerapp;


import com.yscannerapp.core.BaseActivity;
import com.yscannerapp.data.Storage;


import android.content.Context;
import android.content.Intent;

public class Manager {
	
	static BaseActivity mainActivity;
	
	public static String getGCMToken(Context context) {
		return 	Storage.getData(Storage.GCM_TOKEN,context,"-1");
	}
	
	public static void setGCMToken(String accessToken,Context context) {
		Storage.setData(Storage.GCM_TOKEN,accessToken, context);
	}
	
	public static void setMainActivity(BaseActivity activity) {
		mainActivity=activity;
	}
	
	
	public static BaseActivity getMainActivity() {
		return mainActivity;
	}
	
}

