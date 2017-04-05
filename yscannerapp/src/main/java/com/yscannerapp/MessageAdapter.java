package com.yscannerapp;

import java.util.Vector;

import com.yscannerapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	
	
	public class ItemData {
		public int index;
		String title;
		String message;
		String date;
		
		public ItemData(String title, String message, String date) {
			this.title=title;
			this.message=message;
			this.date=date;
		}
	}
	
	protected LayoutInflater mInflater;
	protected Vector<ItemData> mInvrDataVector;
	private Context mContext;

	public MessageAdapter(Context context,final Vector<ItemData> data) {
		mInvrDataVector = data;
		mInflater = LayoutInflater.from(context);
		mContext = context;		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mInvrDataVector.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mInvrDataVector.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setNewData(Vector<ItemData> data) {
		mInvrDataVector=data;
	}
		
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary
		// calls
		// to findViewById() on each row.
		ViewHolder holder=null;
		
				
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.message_adapter, null);	
			holder = new ViewHolder();
							
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		convertView.setTag(holder);
		initHolder(holder,position,convertView);

		convertView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return convertView;
	}
	
	private void initHolder(ViewHolder holder,int position,View convertView) {
		holder.itemData=mInvrDataVector.elementAt(position);
		holder.mLlRoot = (LinearLayout) convertView.findViewById(R.id.llRoot);
		
		holder.mTxtTitle = (TextView) convertView.findViewById(R.id.msgTitle);
		holder.mTxtContent = (TextView) convertView.findViewById(R.id.msgContent);					
		holder.mTxtDate = (TextView) convertView.findViewById(R.id.msgDate);
		
						
		ItemData itemData = holder.itemData;	
		holder.mTxtTitle.setText(itemData.title);
		holder.mTxtContent.setText(itemData.message);
		holder.mTxtDate.setText(itemData.date);
		holder.mLlRoot.setTag(itemData);
	}



	class ViewHolder {
		LinearLayout mLlRoot;
		TextView mTxtTitle;
		TextView mTxtContent;
		TextView mTxtDate;
		ItemData itemData;
	}

}
