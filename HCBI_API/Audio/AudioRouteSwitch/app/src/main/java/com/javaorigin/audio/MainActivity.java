package com.javaorigin.audio;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends Activity {
	AudioManager	am	= null;
    MediaRecorder   mRecorder =null;
	AudioTrack track =null;
    Class audioSystemClass = null;
    Method setForceUse = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	int OutputDevice=0;
    int InputDevice=0;
    int speakerDevice=(1<<0);
    int lineOutDevice=(1<<1);
    int internalMicDevice=(1<<2);
    int lineInDevice=(1<<3);
	boolean isSpeakerPlaying = false;
    boolean isLineOutPlaying = false;
    boolean isInterMicRecording = false;
    boolean isLineInRecording = false;


    public void onRadioButtonClicked(View view) throws InvocationTargetException, IllegalAccessException {
        boolean checked = ((RadioButton) view).isChecked();
        Intent intent = new Intent();
        switch(view.getId()) {
            case R.id.speakerChk:
                if (checked) {
                    OutputDevice = speakerDevice;
                    intent.setAction("android.intent.action.CUSTOMIZE_OUTPUT");
                    intent.putExtra("state",1);
                    sendBroadcast(intent);
                }
                break;
            case R.id.lineoutChk:
                if (checked) {
                    OutputDevice = lineOutDevice;
                    //setForceUse.invoke(null, 1, 2);
                    intent.setAction("android.intent.action.CUSTOMIZE_OUTPUT");
                    intent.putExtra("state",2);
                    sendBroadcast(intent);
                }
                break;
            case R.id.interMicChk:
                if (checked) {
                    InputDevice = internalMicDevice;
                    intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
                    intent.putExtra("state",1);
                    sendBroadcast(intent);
                }
                break;
            case R.id.lineinChk:
                if (checked) {
                    InputDevice = lineInDevice;
                    intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
                    intent.putExtra("state",2);
                    sendBroadcast(intent);
                }
                break;
            case R.id.LineInOutChk:
                if (checked) {
                    InputDevice = lineInDevice;
                    intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
                    intent.putExtra("state",2);
                    sendBroadcast(intent);

                    OutputDevice = lineOutDevice;
                    intent.setAction("android.intent.action.CUSTOMIZE_OUTPUT");
                    intent.putExtra("state",2);
                    sendBroadcast(intent);
                }
                break;
            case R.id.defaultrouttingChk:
                if (checked) {
                    InputDevice = lineInDevice;
                    intent.setAction("android.intent.action.CUSTOMIZE_INPUT");
                    intent.putExtra("state",1);
                    sendBroadcast(intent);

                    OutputDevice = lineOutDevice;
                    intent.setAction("android.intent.action.CUSTOMIZE_OUTPUT");
                    intent.putExtra("state",1);
                    sendBroadcast(intent);
                }
                break;
            default:
                break;
        }
    }

    boolean isRecording=false;

    public void record(View view) throws IOException {
        Button recordBtn=(Button) findViewById(R.id.recordBtn);


        if(isRecording){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            isRecording=false;
            recordBtn.setText("Record");
        }else{
            mRecorder = new MediaRecorder();
            //stopRecording();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/sdcard/Download/test.3gpp");
            mRecorder.prepare();
            mRecorder.start();

            isRecording=true;
            recordBtn.setText("Stop");
        }
    }
}
