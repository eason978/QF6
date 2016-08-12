package com.example.ir_demo;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanta.hcbiapi.*;
import com.quanta.hcbiapi.Ir.*;

import java.io.*;
import java.util.ArrayList;

public class IrDemo extends Activity{
	private static final String TAG = "IrDemo";
	Ir ir;
	Button learningButton;
	Button sendLearningButton;
	TextView text;
	boolean learning;
	ArrayList brandButtonList;
	Button lastBrandButton;
	ArrayList codeNumButtonList;
	LinearLayout brandLayout;
	LinearLayout codeNumLayout;
	TextView rxKeyText;
	
	
	void testGetBrandAV() {
		for(String s : ir.getBrandAV()) {
			Log.e(TAG, s);
		}		
	}
	void testGetBrandAC() {
		for(String s : ir.getBrandAC()) {
			Log.e(TAG, s);
		}		
	}
	
	void testGetCodeListAV() {
		for(String code : ir.getCodeListAV("SONY")) {
			Log.e(TAG, ""+code);
		}
	}
	
	void testGetCodeListAC() {
		for(String code : ir.getCodeListAC("TOSHIBA")) {
			Log.e(TAG, ""+code);
		}
	}
	
	void test() {
		try {
			testGetBrandAV();
			testGetCodeListAV();
			ir.txAV(Port.PORT0, "46", Key.POWER);
			testGetBrandAC();
			testGetCodeListAC();
			ir.txAC(Port.PORT0, "56", Power.ON, Mode.COOL, Temp.TEMP_25, Fan.AUTO);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_demo);
                
        rxKeyText = (TextView) findViewById(R.id.textView6);
    	final Handler rxHandler = new Handler() {
    		public void handleMessage(Message msg) {
    			RxKey rxKey = (RxKey) msg.obj;
    			rxKeyText.setText(rxKey.name());
    		}
    	};
        
        try {
        	ir = new Ir(getApplicationContext(), rxHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        //test();
        
        

        final OnClickListener codeNumButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button button = (Button) codeNumButtonList.get(v.getId());
				String codeNum = (String) button.getText();
	            TextView textView = (TextView) findViewById(R.id.textView4);
	            textView.setText("send power key for " + codeNum);
	            try {
					ir.txAV(Port.PORT0, codeNum, Key.POWER);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        };
        
        codeNumLayout = (LinearLayout) findViewById(R.id.linearLayout2);
        
        //========================brandButtonListener
        OnClickListener brandButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button button = (Button) brandButtonList.get(v.getId());
				button.getBackground().setColorFilter(0xFF00FF00, android.graphics.PorterDuff.Mode.MULTIPLY);
				if(lastBrandButton != null)
					lastBrandButton.getBackground().setColorFilter(null);
				lastBrandButton = button;
				
				int j = 0;
	            codeNumLayout.removeAllViews();
	            codeNumButtonList = new ArrayList();
		        for(String s : ir.getCodeListAV((String) button.getText())) {
		        	
		            Button btnTag = new Button(getApplicationContext());
		            btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		            btnTag.setText(s);
		            btnTag.setId(j++ );
		                        
		            codeNumLayout.addView(btnTag);
		            btnTag.setOnClickListener(codeNumButtonListener);
		            codeNumButtonList.add(btnTag);
		        }
				
			}
        };
        
        brandLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        brandButtonList = new ArrayList();
        int j = 0;
        for(String s : ir.getBrandAV()) {
        	
            Button btnTag = new Button(this);
            btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            btnTag.setText(s);
            btnTag.setId(j++ );
            brandLayout.addView(btnTag);
            btnTag.setOnClickListener(brandButtonListener);
            brandButtonList.add(btnTag);
        }
        
        learning = false; 
        learningButton = (Button) findViewById(R.id.button1); 
        sendLearningButton = (Button) findViewById(R.id.button2);
        text = (TextView) findViewById(R.id.textView1); 
        
    	final Handler mHandler = new Handler() {
    		public void handleMessage(Message msg) {
    			boolean ret = (Boolean) msg.obj;
    			text.setText(ret ? "learning success" : "learning fail");
    		}
    	};
        
        OnClickListener myButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button1:
					if(learning)
						break;
					learning = true;
					text.setText("learning...");

			        new Thread(new Runnable() {
			        	public void run() {
			        		boolean ret;
			        		try {
								ret = ir.learnShort(0);
				                Message msg = Message.obtain();
				                msg.obj = ret;
				                mHandler.sendMessage(msg);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ret = false;
							}catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        		learning = false;
			        	}
			        }).start();
					
					break;
				case R.id.button2:
					try {
						ir.txLearn(Ir.Port.PORT0, 0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
        };
        learningButton.setOnClickListener(myButtonListener);
        sendLearningButton.setOnClickListener(myButtonListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ir_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
