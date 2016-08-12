package com.example.woa.myapplication;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.quanta.hcbiapi.Audio;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    MediaRecorder   mRecorder =null;
    public String TAG ="Audio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onRadioButtonClicked(View view) throws InvocationTargetException, IllegalAccessException {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.speakerChk:
                if (checked) {
                    Audio.SwitchAudioPath(getApplicationContext(), "speakerChk");
                }
                break;
            case R.id.lineoutChk:
                if (checked) {
                    Audio.SwitchAudioPath(getApplicationContext(), "lineoutChk");
                }
                break;
            case R.id.interMicChk:
                if (checked) {
                    Audio.SwitchAudioPath(getApplicationContext(), "interMicChk");
                }
                break;
            case R.id.lineinChk:
                if (checked) {
                    Audio.SwitchAudioPath(getApplicationContext(), "lineinChk");
                }
                break;
            case R.id.LineInOutChk:
                if (checked) {
                    Audio.SwitchAudioPath(getApplicationContext(), "LineInOutChk");
                }
                break;
            case R.id.defaultrouttingChk:
                if (checked) {
                    Audio.SwitchAudioPath(getApplicationContext(), "defaultrouttingChk");
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
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/sdcard/Download/test.3gpp");
            mRecorder.prepare();
            mRecorder.start();

            Log.i(TAG, "test3");
            isRecording=true;
            recordBtn.setText("Stop");
        }
    }
}
