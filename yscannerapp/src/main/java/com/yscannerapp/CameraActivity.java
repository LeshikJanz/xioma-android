package com.yscannerapp;


import com.yscannerapp.core.CameraPreview;
import com.yscannerapp.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Button;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import android.widget.TextView;
import android.graphics.ImageFormat;

/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

public class CameraActivity extends Activity
{
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;
    private Button doneBtn;
    private Button done_cancelBtn;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;
    
    String scanContent="";

    static {
        System.loadLibrary("iconv");
    } 

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera_activity);

        //setRequestedOrientation(ActivityInfo.);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = (TextView)findViewById(R.id.scanText);

        scanButton = (Button)findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (barcodeScanned) {
                        barcodeScanned = false;
                        scanText.setText("Scanning...");
                        mCamera.setPreviewCallback(previewCb);
                        mCamera.startPreview();
                        previewing = true;
                        mCamera.autoFocus(autoFocusCB);
                    }
                }
            });
        
        done_cancelBtn = (Button)findViewById(R.id.done_cancel);
        
        done_cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CameraActivity.this.finish();
			}
		});
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }
    
    public void onResume() {
        super.onResume();  

        // Get the Camera instance as the activity achieves full user focus
        if (mCamera == null) {
        	autoFocusHandler = new Handler();
            mCamera = getCameraInstance();

            /* Instance barcode scanner */
            scanner = new ImageScanner();
            scanner.setConfig(0, Config.X_DENSITY, 3);
            scanner.setConfig(0, Config.Y_DENSITY, 3);

            mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
            FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
            preview.addView(mPreview);
            
            mCamera.setPreviewCallback(previewCb);
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        scanText.setText("barcode result " + sym.getData());
                        scanContent =  sym.getData();
                        barcodeScanned = true;
                        
                        WebViewActivity wva = (WebViewActivity)Manager.getMainActivity();
        				wva.barcodeCode = scanContent;
                        wva.reloadUrl(WebViewActivity.BaseURL+"/#/job-cards");
        				//wva.reloadUrl(WebViewActivity.BaseURL+"/scanedpage.aspx");
        				Intent intent = new Intent(CameraActivity.this,WebViewActivity.class);        
        		        startActivity(intent);
        				CameraActivity.this.finish();
                    }
                }
            }
        };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };
    
    
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CameraActivity.this,WebViewActivity.class);        
        startActivity(intent);
        CameraActivity.this.finish();
        super.onBackPressed();
    }
}
