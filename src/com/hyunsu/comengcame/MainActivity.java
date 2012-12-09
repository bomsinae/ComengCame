package com.hyunsu.comengcame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hyunsu.comengcame.R;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final String TAG = "CAMERA";
	
	private Camera mCamera;
	private CameraPreView mPreview;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		mCamera = getCameraInstance();
		Camera.Parameters params = mCamera.getParameters();
		params.set("iso",  "ISO100");
		
		String newValue = params.get("iso-values");
		//auto,ISO_HJR,ISO100,ISO200,ISO400,ISO800,ISO1600
		
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		mCamera.setParameters(params);
		mPreview = new CameraPreView(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.preview);
		preview.addView(mPreview);
		
		
		preview.setOnClickListener( 
			new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mCamera.autoFocus(mAutofocus);
					
				}
			}
		);
		
		Button captureButton = (Button) findViewById(R.id.shotter);
		captureButton.setOnClickListener(
		    new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            // get an image from the camera
		        	mCamera.takePicture(null, null, mPicture);
		    		Log.d(TAG, "take picture from camera");
 
		            
		        }
		    }
		);
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
	}
	
	private void releaseCamera(){
		if (mCamera != null){
			mCamera.release();
			mCamera = null;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	private AutoFocusCallback mAutofocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if(success) {
				Log.d(TAG, "Autofocus Complete.");
				
			}
		}
	};
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	    	
	        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	        if (pictureFile == null){
	            Log.d(TAG, "Error creating media file, check storage permissions: ");
	            return;
	        }

	        try {
	        	Log.d(TAG, "TakePicture Callback");
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d(TAG, "Error accessing file: " + e.getMessage());
	        }
	    }
	};
	
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

		Log.d(TAG, Environment.getExternalStorageState());
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    Log.d(TAG, mediaStorageDir.toString());
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	    	Log.d(TAG, "Not exists " + mediaStorageDir.toString());
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    Log.d(TAG, "getOutputmediaFile");
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
}
