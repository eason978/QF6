package com.example.camera_demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView;
	public MediaRecorder mrec = new MediaRecorder();
	private String TAG = "Camera_demo";
	private Camera mCamera;
	private Bitmap bmp;
	private String Img_path;
	private boolean recording = false;
	private int current_camera = 0;
    private int surface_width;
	private int surface_height;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	private Button Capture;
	private Button Record;
	private Button Switch;

	@Override
	public void onResume() {
		super.onResume();

		Intent intent = new Intent();
		intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
		intent.putExtra("state", current_camera+1); //InternalMic:1 LineIn:2
		sendBroadcast(intent);

		recording = false;
		setContentView(R.layout.activity_main);

		Log.i(TAG, "onResume");

		mCamera = Camera.open(current_camera);
		//Camera.Parameters parameters = mCamera.getParameters();

		//parameters.setPreviewSize(720, 576);
		//parameters.setPictureFormat(ImageFormat.JPEG);
		//parameters.setPictureSize(720, 576);
		//mCamera.setParameters(parameters);

		surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		Record = (Button) findViewById(R.id.Record);
		Record.setBackgroundColor(Color.GREEN);
		Capture = (Button) findViewById(R.id.Capture);
		Switch = (Button) findViewById(R.id.Switch);

		Capture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Capture.setVisibility(Button.INVISIBLE);
				Record.setVisibility(Button.INVISIBLE);
				Switch.setVisibility(Button.INVISIBLE);
				mCamera.takePicture(shutterCallback, null, jpegCallback);
			}
		});

		Switch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Switch.setVisibility(Button.INVISIBLE);
				Record.setVisibility(Button.INVISIBLE);
				Capture.setVisibility(Button.INVISIBLE);
				try {
					switchCamera();
				} catch (Exception e) {
					String message = e.getMessage();
					Log.i(null, "Problem Start" + message);
				}
			}
		});

		Record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(recording==false) {
					recording=true;
					Switch.setVisibility(Button.INVISIBLE);
					Capture.setVisibility(Button.INVISIBLE);
					Record.setBackgroundColor(Color.RED);
					try {
						startRecording();
					} catch (Exception e) {
						String message = e.getMessage();
						Log.i(null, "Problem Start" + message);
						mrec.release();
					}
				}else if(recording==true){
					mrec.stop();
					mrec.release();
					mrec = null;
					recording=false;
					Record.setBackgroundColor(Color.GREEN);
					Switch.setVisibility(Button.VISIBLE);
					Capture.setVisibility(Button.VISIBLE);
					Toast.makeText(MainActivity.this," file is saved", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.i(TAG, "onPause");
		recording=false;

		if (null != mrec){
		///mrec.stop();
		mrec.release();
		mrec = null;
	    }
		// Release camera so other applications can use it.
		if (null != mCamera) {
				try {
					mCamera.stopPreview();
				} catch (Exception e) {
					Log.e(TAG, "Failed to start preview");
				}

			mCamera.release();
			mCamera = null;
		}

		Intent intent = new Intent();
		intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
		intent.putExtra("state", 1); //InternalMic:1 LineIn:2
		sendBroadcast(intent);
	}


	private void sleep(int milisecond){
		try {
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
		@Override
		public void onShutter() {

		}
	};
	private PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {

			Img_path = "Pictures";

			sleep(100);
			try {
				bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (bmp != null) {
					if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
						Toast.makeText(MainActivity.this,
								"please insert SD Card", Toast.LENGTH_LONG).show();
					} else {
						try {
							final File f = new File("/sdcard", Img_path);
							Log.i(TAG,"Image Path is: " + f.getAbsolutePath());
							if (!f.exists()) {
								f.mkdir();
							}

							final String filename = (String) new SimpleDateFormat("yyyyMMdd_HH-mm-ss-SSS", Locale.TAIWAN)
									.format(new Date()) + ".jpg";
							FileOutputStream bos = new FileOutputStream(new File(f, filename));
							bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
							bos.flush();
							bos.close();
							Toast.makeText(MainActivity.this, filename + " is saved", Toast.LENGTH_LONG).show();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mCamera.startPreview();

			Switch.setVisibility(Button.VISIBLE);
			Record.setVisibility(Button.VISIBLE);
			Capture.setVisibility(Button.VISIBLE);
		}
	};

	protected void startRecording() throws IOException
	{
		mrec = new MediaRecorder();  // Works well
		mCamera.unlock();

		mrec.setCamera(mCamera);

		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);

		mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setVideoSize(720, 576);

		final String filename = (String) new SimpleDateFormat("yyyyMMdd_HH-mm-ss-SSS", Locale.TAIWAN)
				.format(new Date()) + ".3gp";
		mrec.setOutputFile("/sdcard/Movies/"+filename);

		mrec.prepare();
		mrec.start();
	}

	protected void stopRecording() {
		mrec.stop();
		mrec.release();
		mCamera.release();
	}

	private void releaseMediaRecorder(){
		if (mrec != null) {
			mrec.reset();   // clear recorder configuration
			mrec.release(); // release the recorder object
			mrec = null;
			mCamera.lock();           // lock camera for later use
		}
	}

	private void releaseCamera(){
		if (mCamera != null){
			mCamera.release();        // release the camera for other applications
			mCamera = null;
		}
	}

	// Determine the right size for the preview
	private Camera.Size findBestSize(Camera.Parameters parameters, int width, int height) {

		List<Camera.Size> supportedSizes = parameters.getSupportedPreviewSizes();
		Camera.Size bestSize = supportedSizes.remove(0);

		for (Camera.Size size : supportedSizes) {
			if ((size.width * size.height) > (bestSize.width * bestSize.height)) {
				bestSize = size;
			}
		}

		return bestSize;
	}

	protected void switchCamera() throws IOException
	{
		if (null != mrec) {
			//mrec.stop();
			mrec.release();
			mrec = null;
		}

		if (null != mCamera) {
			mCamera.stopPreview();
			mCamera.release();
		}

		if(current_camera==0) {
			current_camera = 1;

			Intent intent = new Intent();
			intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
			intent.putExtra("state", 2); //InternalMic:1 LineIn:2
			sendBroadcast(intent);

			mCamera = Camera.open(current_camera);

			Camera.Parameters parameters = mCamera.getParameters();

			parameters.setPreviewSize(720, 576);
			parameters.setPictureFormat(ImageFormat.JPEG);
			parameters.setPictureSize(720, 576);
			mCamera.setParameters(parameters);

		}else if(current_camera==1){
			current_camera = 0;

			Intent intent = new Intent();
			intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
			intent.putExtra("state", 1); //InternalMic:1 LineIn:2
			sendBroadcast(intent);

			mCamera = Camera.open(current_camera);

			Camera.Parameters parameters = mCamera.getParameters();
			Camera.Size bestSize = findBestSize(parameters, surface_width, surface_height);

			parameters.setPreviewSize(bestSize.width, bestSize.height);
			parameters.setPictureFormat(ImageFormat.JPEG);
			parameters.setPictureSize(1024, 768);
			mCamera.setParameters(parameters);

		}
		//alvin hsu
		try {
			mCamera.setPreviewDisplay(surfaceHolder);
		} catch (IOException e) {
			Log.e(TAG, "Failed to set preview display");
		}

		mCamera.startPreview();

		Record.setVisibility(Button.VISIBLE);
		Capture.setVisibility(Button.VISIBLE);
		Switch.setVisibility(Button.VISIBLE);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {

		surface_width = width;
		surface_height = height;

		mCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera != null){
			Camera.Parameters params = mCamera.getParameters();
			mCamera.setParameters(params);
            //alvin hsu
			try {
				mCamera.setPreviewDisplay(surfaceHolder);
			} catch (IOException e) {
				Log.e(TAG, "Failed to set preview display");
			}

		}
		else {
			Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surfaceDestroyed");
		if (null != mrec) {
			//mrec.stop();
			mrec.release();
			mrec = null;
		}

		if (null != mCamera) {
			mCamera.stopPreview();
			mCamera.release();
		}


	}


}