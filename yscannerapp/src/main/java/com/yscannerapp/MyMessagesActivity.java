package com.yscannerapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.yscannerapp.MessageAdapter.ItemData;
import com.yscannerapp.core.BaseActivity;

import java.util.Vector;


public class MyMessagesActivity extends BaseActivity{

	final Activity activity = this;
	private final static String DB_NAME = "MessagesDB";
	private final static String TABLE_NAME = "GCMTable";
    static SQLiteDatabase customersDB = null;
    
    private ListView mIvrListView;
    MessageAdapter listAdapter;
    Button btnResetDb;
    Button closeMsgActivity;
    private Vector<ItemData> AllList = new Vector<ItemData>();
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mIvrListView = (ListView)mActivity.findViewById(R.id.listView);
		
		listAdapter = new MessageAdapter(mContext,AllList);
		mIvrListView.setAdapter(listAdapter);
		
		customersDB =  this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
		customersDB.execSQL("CREATE TABLE IF NOT EXISTS " +
    			TABLE_NAME +
    			" (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
    			" Message VARCHAR," +
    			" Title VARCHAR,"+
    			" CreateDate VARCHAR);");
		
		Cursor c = customersDB.rawQuery("SELECT ID,Message,Title, CreateDate  FROM "	+
    			TABLE_NAME +" ORDER BY ID DESC", null);
    	
		if (c != null ) {
    		if  (c.moveToFirst()) {
    			do {
    				String message = c.getString(c.getColumnIndex("Message"));
       				String createDate = c.getString(c.getColumnIndex("CreateDate"));
       				String title = c.getString(c.getColumnIndex("Title"));
       				AllList.add(listAdapter.new ItemData(title,message, createDate));
    			}while (c.moveToNext());
    		} 
    	}
		
		btnResetDb = (Button) mActivity.findViewById(R.id.resetDb); 
		btnResetDb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (customersDB != null) 
	        		customersDB.execSQL("DELETE FROM " +   TABLE_NAME);
	        		customersDB.close();
	        		
			}
		});
		
		closeMsgActivity = (Button) mActivity.findViewById(R.id.closeMsgActivity); 
		closeMsgActivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyMessagesActivity.this.finish();
			}
		});
	}
	
	@Override
	public int getLayoutXML() {
		return R.layout.my_messages_activity;
	}
}
