package com.quanta.woa.qf6demo.irdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.quanta.hcbiapi.Ir;
import com.quanta.woa.qf6demo.R;

import java.io.IOException;

/**
 * Created by Terry on 11/20/2015.
 */
public class IrDemoMain extends AppCompatActivity {
    static public Ir ir;
    static private TextView rxKeyText;
    static private ToggleButton toggleButton;

    static class RxHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Ir.RxKey rxKey = (Ir.RxKey) msg.obj;
            rxKeyText.setText(rxKey.name());
        }
    }

    static class PowerToggleListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (((ToggleButton)v).isChecked()) {
                if (ir != null) try {
                    ir.resume();
                    rxKeyText.setText(R.string.rx_mode_unknown_input);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ir.pause();
                    rxKeyText.setText(R.string.rx_mode_off_hint);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_demo_main);

        /**
         * RX-mode handling
         */
        rxKeyText = (TextView) findViewById(R.id.textView8);
        try {
            ir = new Ir(getApplicationContext(), new RxHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * IR power toggle
         */
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new PowerToggleListener());
        toggleButton.setChecked(true);

        /**
         * Enter database mode
         */
        findViewById(R.id.database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), IrDemoDatabase.class));
            }
        });

        /**
         * Enter learning mode
         */
        findViewById(R.id.learning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), IrDemoLearn.class));
            }
        });

    }

}
