package com.yscannerapp.data;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Storage {
	

	private static String C2DM_KEY = "c2dmPref";
	
	public static String GCM_TOKEN = "GCM_TOKEN";
	public static String IS_CREATE_NEW_NOTIFICATION_TOKEN = "IS_CREATE_NEW_NOTIFICATION_TOKEN";
	
	
	public static void setData(String key,String val,Context context) {
    	Editor editor =
                context.getSharedPreferences(C2DM_KEY, Context.MODE_PRIVATE).edit();
            editor.putString(key, val);
    		editor.commit();		
	}

	public static void setDataInt(String key,int val,Context context) {
    	Editor editor =
                context.getSharedPreferences(C2DM_KEY, Context.MODE_PRIVATE).edit();
            editor.putInt(key, val);
    		editor.commit();		
	}
	
	public static String getData(String key,Context context) {
		return context.getSharedPreferences(C2DM_KEY, Context.MODE_PRIVATE).getString(key, null);
	}

	public static String getData(String key,Context context,String defaultValue) {
		return context.getSharedPreferences(C2DM_KEY, Context.MODE_PRIVATE).getString(key, defaultValue);
	}
	
	public static int getDataInt(String key,Context context) {
		return context.getSharedPreferences(C2DM_KEY, Context.MODE_PRIVATE).getInt(key, -1);
	}

	public static int getDataInt(String key,Context context, int defaultValue) {
		return context.getSharedPreferences(C2DM_KEY, Context.MODE_PRIVATE).getInt(key, defaultValue);
	}
	
}
