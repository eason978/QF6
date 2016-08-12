package com.quanta.woa.qf6demo.irdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanta.hcbiapi.Ir;
import com.quanta.woa.qf6demo.R;

import java.io.IOException;

/**
 * Created by Terry on 11/20/2015.
 */
public class IrDemoDatabase extends AppCompatActivity {
    private String lastSelectedDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_demo_database);

        /**
         * Code for remote selection
         */
        LinearLayout brandLayout = (LinearLayout) findViewById(R.id.brandLayout);
        for (String s : IrDemoMain.ir.getBrandAV()) {
            Button btnBrand = new Button(this);
            btnBrand.setText(s);
            brandLayout.addView(btnBrand);
            btnBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    LinearLayout deviceLayout = (LinearLayout) findViewById(R.id.deviceLayout);
                    deviceLayout.removeAllViews();
                    for (String s : IrDemoMain.ir.getCodeListAV(button.getText().toString())) {
                        Button btnDevice = new Button(IrDemoDatabase.this);
                        btnDevice.setText(s);
                        deviceLayout.addView(btnDevice);
                        btnDevice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button button = (Button) v;
                                lastSelectedDevice = button.getText().toString();
                                TextView hintRemote = (TextView) findViewById(R.id.hintRemote);
                                hintRemote.setText("Remote " + lastSelectedDevice + " selected");
                            }
                        });
                    }
                }
            });
        }

        /**
         * Code for remote TX based on remote selection
         */
        findViewById(R.id.btnPower).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.POWER);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY0);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY1);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY2);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY3);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY4);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY5);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY6);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY7);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY8);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.KEY9);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnChUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.CH_UP);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnChDn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.CH_DOWN);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnVolUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.VOL_UP);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnVolDn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDevice == null) return;
                try {
                    IrDemoMain.ir.txAV(Ir.Port.PORT0, lastSelectedDevice, Ir.Key.VOL_DOWN);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
