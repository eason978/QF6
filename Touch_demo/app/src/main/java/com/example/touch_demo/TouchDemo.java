package com.example.touch_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.quanta.hcbiapi.Touch;

public class TouchDemo extends Activity {

	Button mButton1, mButton2;
	Touch touch = new Touch();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_demo);

		mButton1 = (Button) findViewById(R.id.Button1);
		mButton2 = (Button) findViewById(R.id.Button2);

		mButton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				touch.touchOn(); // to enable touch
			}
		});

		mButton2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				touch.touchOff(); // tp disable touch
			}
		});

	}

}
