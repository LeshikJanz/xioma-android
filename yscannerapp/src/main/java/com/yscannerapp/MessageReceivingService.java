package com.yscannerapp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.yscannerapp.R;
/*
 * This service is designed to run in the background and receive messages from gcm. If the app is in the foreground
 * when a message is received, it will immediately be posted. If the app is not in the foreground, the message will be saved
 * and a notification is posted to the NotificationManager.
 */
public class MessageReceivingService extends Service{
    private GoogleCloudMessaging gcm;
    public static SharedPreferences savedValues;
    
    private final static String DB_NAME = "MessagesDB";
	private final static String TABLE_NAME = "GCMTable";
    static SQLiteDatabase customersDB = null;
    
    public static void sendToApp(Bundle extras, Context context){
        Intent newIntent = new Intent();
        newIntent.setClass(context, WebViewActivity.class);
        newIntent.putExtras(extras);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

    public void onCreate(){
        super.onCreate();
        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
        register();
    }
    
    protected static void saveToLog(Bundle extras, Context context){
    	customersDB =  context.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
    	String sMessage = extras.getString("message");
    	String Title = extras.getString("contentTitle");
    	
    	Calendar c = Calendar.getInstance();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String sCreateDate = df.format(c.getTime());
    	
    	customersDB.execSQL("CREATE TABLE IF NOT EXISTS " +
    			TABLE_NAME +
    			" (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
    			" Message VARCHAR," +
    			" Title VARCHAR,"+
    			" CreateDate VARCHAR);");
    	customersDB.execSQL("INSERT INTO " +
    			TABLE_NAME +
    			"(Message,Title,CreateDate) Values ('"+sMessage+"','"+Title+"','"+sCreateDate+"');");
    	
    	customersDB.close();

        postNotification(new Intent(context, WebViewActivity.class), context,extras);
    }

    protected static void postNotification(Intent intentAction, Context context ,Bundle extras){
        final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /*
         * //For debugging
        String s = "";
        for(String key : extras.keySet()){
            String line = String.format("%s=%s\n", key, extras.getString(key));
            s+=line;
        }
        */
        if(extras.getString("alert") != ""){
	        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentAction, Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL);
	        final Notification notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
	                .setContentTitle(extras.getString("contentTitle"))
	                .setContentText(extras.getString("message"))
	                .setContentIntent(pendingIntent)
	                .setAutoCancel(true)
	                .setSound(alarmSound)
	                .getNotification();
	        notification.number = 1;
	
	        mNotificationManager.notify(R.string.notification_number, notification);
        }
    }

    private void register() {
        new AsyncTask<Object, Object, Object>(){
            protected Object doInBackground(final Object... params) {
                String token;
                try {
                    token = gcm.register(getString(R.string.project_number));
                    Manager.setGCMToken(token, MessageReceivingService.this);
                    Intent newIntent = new Intent();
                    newIntent.setClass(MessageReceivingService.this, WebViewActivity.class);
                    newIntent.putExtra("TOKEN",token);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MessageReceivingService.this.startActivity(newIntent);
                    
                    //ShowDialog("Token",token);//For debugging 
                    Log.i("registrationId", token);
                } 
                catch (IOException e) {
                    Log.i("Registration Error", e.getMessage());
                }
                return true;
            }
        }.execute(null, null, null);
    }
    
    
    public void ShowDialog(final String title, final String message) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(MessageReceivingService.this);
			if (title != null) {
				builder.setTitle(title);
			}
			builder.setMessage(message);
			
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}	

    public IBinder onBind(Intent arg0) {
        return null;
    }

}
