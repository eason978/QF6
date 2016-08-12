package com.example.audiopathswitch;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import android.graphics.Color;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

	public int currVolume=0;
	
	AudioRecord record =null;
	AudioTrack track =null;
    AudioManager audiomanager;
    private MediaPlayer mp;

    private static final String TAG="AudioSwitch";
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		setVolumeControlStream(AudioManager.MODE_IN_COMMUNICATION);

        audiomanager= (AudioManager) getSystemService(AUDIO_SERVICE);
        Button buttonSpOn = (Button)findViewById(R.id.speakerOn);
        buttonSpOn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        /*
                        MediaPlayer mp = new MediaPlayer();
                        try {
                            mp.setDataSource("/sdcard/raw/eng_m1");
                            mp.prepare();
                        } catch (IOException e) {
                        }
                        mp.start();
                        */
                        //audiomanager.setParameters("routing=2");
                        //audiomanager.setParameters("routing=2048");
                        audiomanager.setParameters("route-fm=speaker");
                        //audiomanager.setParameters("route-passthrogh=speaker");
                        //audiomanager.setSpeakerphoneOn(true);
                    }
                });

        Button buttonSpOff = (Button)findViewById(R.id.speakerOff);
        buttonSpOff.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.e(TAG,"audioswitch off");
                        audiomanager.setParameters("route-fm=off");
                        //audiomanager.setParameters("route-passthrogh=off");
                    }
                });

        	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			return false;
		}
	};
/*
	public void TurnOnSpeaker(View view) {
		Button speakerOnBtn=(Button) findViewById(R.id.speakerOn);
		am.setSpeakerphoneOn(true);
		speakerOnBtn.setBackgroundColor(Color.CYAN);
	}	
	public void TurnOffSpeaker(View view) {
		Button speakerOnBtn=(Button) findViewById(R.id.speakerOff);
		am.setSpeakerphoneOn(false);
		speakerOnBtn.setBackgroundColor(Color.GRAY);
	}
*/
}
