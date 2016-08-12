package com.example.lcd_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

// -------------------NOTE-------------------//
//  In order to set the display brightness,
// must add "android.permission.WRITE_SETTINGS" in AndroidManifest.xml

public class LCD_Demo extends Activity {

	private Button mButton1, mButton2, mButton3, mButton4;
	private int newBV, currentBV;
	private Timer timer1, timer2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lcd_demo);

		mButton1 = (Button) findViewById(R.id.mButton1);
		mButton2 = (Button) findViewById(R.id.mButton2);
		mButton3 = (Button) findViewById(R.id.mButton3);
		mButton4 = (Button) findViewById(R.id.mButton4);

		// To turn off the panel, and then to query panel state after 50 ms.
		mButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				screenToggle(false);
				queryStateTimer(50); // Give queryStateTimer 50 ms delay to get the right state
			}
		});

		// To turn off the panel after 5000 ms, and then to query panel state after 50 ms.
		mButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				setScreenOffTime(5000);
				queryStateTimer(5050);
			}
		});

		// To reduce the display brightness value by 10 each time.
		mButton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				currentBV = bvGet();
				newBV = currentBV - 10;
				bvSet(newBV);
			}
		});

		// To increase the display brightness value by 10 each time.
		mButton4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				currentBV = bvGet();
				newBV = currentBV + 10;
				bvSet(newBV);
			}
		});
	}

	// To set screen on or off. If on = true, the panel will be turned on; on = false, the panel will be off.
	private void screenToggle(boolean on) {
		Intent intent = new Intent("android.intent.action.SCREEN_TOGGLE");
		intent.putExtra("on", on);
		sendBroadcast(intent);
	}

	// A TimerTask for setScreenOffTime to turn off screen
	public class screenTask extends TimerTask {
		public void run(){
			screenToggle(false);
			timer1.cancel();
		}
	}

	// Set a timer to turn off screen
	public void setScreenOffTime (int time){
		timer1 = new Timer();
		timer1.schedule(new screenTask(), time);
	}

	//  A TimerTask for queryStateTimer  to query current screen state
	public class queryTask extends TimerTask {
		public void run(){
			try{
				Process process = Runtime.getRuntime().exec("getprop runtime.screenstate");
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String screenState = br.readLine();
				br.close();
				Log.i("LCD_DEMO", "screen state = " + screenState);
			} catch (IOException e) {
				e.printStackTrace();
			}
			timer2.cancel();
		}
	}

	// Set a timer to query screen state
	public void queryStateTimer (int time) {
		timer2 = new Timer();
		timer2.schedule(new queryTask(), time);
	}

	// To get current display brightness value/
	public int bvGet() {
		return android.provider.Settings.System.getInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				-1);
	}

	// To set display brightness, The brightness value is between 0 and 255
	public void bvSet(int bright) {

		if (bright > 255) bright = 255;
		else if (bright < 0) bright = 0;

		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				bright);
	}
}