package com.hyunsu.comengcame;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	
	public CameraPreView(Context context, Camera camera) {
		super(context);
		// TODO Auto-generated constructor stub
		mCamera = camera;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d("CAMERA", "Error setting camera preivew: " + e.getMessage());
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (mHolder.getSurface() == null) {
			return;
		}
		
		try {
			mCamera.stopPreview();
			
		} catch (Exception e) {
			
		}

		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d("CAMERA", "Error starting camera preview: " + e.getMessage());
		}
	
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
}
