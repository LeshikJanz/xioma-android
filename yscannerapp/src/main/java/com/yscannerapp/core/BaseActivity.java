package com.yscannerapp.core;

import java.util.Locale;
import java.util.Stack;

import com.yscannerapp.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	private final String TAG = "BaseActivity";
	private ProgressDialog mPD;

	protected Activity mActivity;
	protected Context mContext;

	protected boolean mHasTitle = true;
	protected boolean mRequestProgress = false;
	protected boolean mFullScreen = false;
	
	protected boolean mTouch = false;
	private boolean isWaitShowing;
	protected static boolean isVisibale = false;
	
	private static Context currentContext;
	
	public static Stack<BaseActivity> activityStack = new Stack<BaseActivity>();  
	
	public static void setCurrentContext(Context context) {
		currentContext=context;
	}

	public static Context getCurrentContext() {
		return currentContext;
	}

	public static Stack<BaseActivity> getActivityStack() {
		return activityStack;
	}
	
	public String getMyResources(int id) {
		return getMyResources(this,id);
	}
	
	
	public static String getMyResources(Context context, int id) {		
		Resources res = context.getResources();
//		DisplayMetrics dm = res.getDisplayMetrics();
//		Configuration config = res.getConfiguration();
//		Locale locale = new Locale(IVRManager.getLocale());
//		Locale.setDefault(locale);
//		config.locale = locale;
//		res.updateConfiguration(config, dm);
		String s = res.getString(id);
		return s;
	}
	
	
	public static void showWait(ProgressDialog progressDialog,String txt) {
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(txt);
		progressDialog.show();
	}
	
	public static void hideWait(ProgressDialog progressDialog) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}
	
	
	/*public void showWait(final String txt) {
		if (!isWaitShowing) {
			isWaitShowing=true;
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					try {
						mPD = new ProgressDialog(BaseActivity.this,R.style.customProgressStyle);
						mPD.setCancelable(false);
						mPD.setCanceledOnTouchOutside(false);
						mPD.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
						mPD.show();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});
		}
	}*/

	public void showProgressWait(final String txt,final int max) {
		if (!isWaitShowing) {
			isWaitShowing=true;
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					try {
						mPD = new ProgressDialog(BaseActivity.this);
						mPD.setCancelable(false);
						mPD.setCanceledOnTouchOutside(false);
						mPD.setIndeterminate(false);
						mPD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						mPD.setCancelable(true);
						mPD.setMax(max);
						mPD.setMessage(txt);
						mPD.show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public void setProgressValue(final int val) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					try {
						mPD.setProgress(val);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
	}	
	
	public void showWaitSmall(String txt) {
		mPD = new ProgressDialog(this);
		mPD.setCancelable(false);
		mPD.setCanceledOnTouchOutside(false);
		mPD.setMessage(txt);
		mPD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		mPD.show();
	}
	/*
	public void showWait() {
		try {
			showWait(BaseActivity.getMyResources(BaseActivity.getCurrentContext(),R.string.please_wait));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public void hideWait() {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					try {
							isWaitShowing=false;
							if (mPD != null && mPD.isShowing()) {
								mPD.cancel();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			});
	}
	
    Animation slideInLeft = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f);
    Animation slideOutLeft = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		mActivity = this;
		mContext = this;
		isVisibale=true;	
		
		setCurrentContext(this);
		//Manager.setCurrentActivity(this);
		
       // this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		
		
//		if(mTouch){
//			Window window = this.getWindow();
//			window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
//		}
		

		if (!mHasTitle) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		if (mRequestProgress) {
			getWindow().requestFeature(Window.FEATURE_PROGRESS);
		}

		if (mFullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		int content_laout_id = getLayoutXML();
		content_laout_id = content_laout_id;
		if (content_laout_id != -1) {
			setContentView(content_laout_id);
		}
	}
	

	public void ShowSimpleDialog(final String title, final String message) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				try {
					AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
					if (title != null) {
						builder.setTitle(title);
					}
					builder.setMessage(message);
					builder.setPositiveButton("OK", null);
					builder.create().show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void ShowDialog(final String title, final String message, final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				if (title != null) {
					builder.setTitle(title);
				}
				builder.setMessage(message);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
		});
	}	
	
	public abstract int getLayoutXML();


	public static void toastMessage(final Context ctx, final String message,int duration) {
		Toast toast = Toast.makeText(ctx, message, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void toastMessage(final String message,int duration) {
		Toast toast = Toast.makeText(this,message, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	@Override
	protected void onResume() {
		setVisibale(true);
		activityStack.push(this);
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		setVisibale(false);
		activityStack.pop();
		super.onResume();
	}
	
	public static boolean isVisibale() {
		return isVisibale;
	}

	public static void setVisibale(boolean visible) {
		isVisibale=visible;
	}
	
	@Override
	public void finish() {
	    super.finish();
        //this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
}

