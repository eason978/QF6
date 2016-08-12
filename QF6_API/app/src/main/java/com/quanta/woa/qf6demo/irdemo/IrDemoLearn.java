package com.quanta.woa.qf6demo.irdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.quanta.hcbiapi.Ir;
import com.quanta.woa.qf6demo.R;

import java.io.IOException;

/**
 * Created by Terry on 11/20/2015.
 */
public class IrDemoLearn extends AppCompatActivity {
    int keyId = 0;
    static TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_demo_learn);
        status = (TextView) findViewById(R.id.status);
        final RadioButton b0 = (RadioButton) findViewById(R.id.radioButton0);
        final RadioButton b1 = (RadioButton) findViewById(R.id.radioButton1);
        final RadioButton b2 = (RadioButton) findViewById(R.id.radioButton2);
        final RadioButton b3 = (RadioButton) findViewById(R.id.radioButton3);
        final RadioButton b4 = (RadioButton) findViewById(R.id.radioButton4);
        b0.setChecked(true);
        b1.setChecked(false);
        b2.setChecked(false);
        b3.setChecked(false);
        b4.setChecked(false);
        final Button learn = (Button) findViewById(R.id.btnLearn);
        final Button send = (Button) findViewById(R.id.btnSend);
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b0.setChecked(true);
                b1.setChecked(false);
                b2.setChecked(false);
                b3.setChecked(false);
                b4.setChecked(false);
                keyId = 0;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b0.setChecked(false);
                b1.setChecked(true);
                b2.setChecked(false);
                b3.setChecked(false);
                b4.setChecked(false);
                keyId = 1;
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b0.setChecked(false);
                b1.setChecked(false);
                b2.setChecked(true);
                b3.setChecked(false);
                b4.setChecked(false);
                keyId = 2;
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b0.setChecked(false);
                b1.setChecked(false);
                b2.setChecked(false);
                b3.setChecked(true);
                b4.setChecked(false);
                keyId = 3;
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b0.setChecked(false);
                b1.setChecked(false);
                b2.setChecked(false);
                b3.setChecked(false);
                b4.setChecked(true);
                keyId = 4;
            }
        });

        /**
         * Learn button implementation
         */
        final LearnHandler learnHandler = new LearnHandler();
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                learn.setClickable(false);
                status.setText("BUSY...");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            if (IrDemoMain.ir.learnShort(keyId)) {
                                learnHandler.sendEmptyMessage(0);
                            } else {
                                learnHandler.sendEmptyMessage(1);
                            }
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        learn.setClickable(true);
                    }
                }.start();
            }
        });

        /**
         * Send button implementation
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setClickable(false);
                try {
                    if (IrDemoMain.ir.txLearn(Ir.Port.PORT0, keyId)) {
                        status.setText("SD SUCCESS");
                    } else {
                        status.setText("SD FAILED");
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                send.setClickable(true);
            }
        });
    }

    class LearnHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    status.setText("LN SUCCESS");
                    break;
                case 1:
                    status.setText("LN FAILED");
                    break;
            }
        }
    }
}
