package com.yscannerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ExternalReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		if (intent != null) {
			//on receive message
			if ("com.google.android.c2dm.intent.RECEIVE".equals(intent.getAction())) {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					if (!WebViewActivity.inBackground) {
						//MessageReceivingService.sendToApp(extras, context);
						MessageReceivingService.saveToLog(extras, context);
					} else {
						MessageReceivingService.saveToLog(extras, context);
					}
				}
			}
		}
	}
}

