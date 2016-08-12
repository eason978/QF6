package com.example.gpio_demo;

import java.io.IOException;

import com.quanta.hcbiapi.Gpio;
import com.quanta.hcbiapi.Gpio.*;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class GpioDemo extends Activity {

	protected static final String TAG = "GPIO_demo";
    Button [] button_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {  
        final Gpio gpio= new Gpio();
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_gpio_demo);
	    
	    button_array = new Button[8];
	    button_array[0] = (Button) findViewById(R.id.Button01);
	    button_array[1] = (Button) findViewById(R.id.Button02);
	    button_array[2] = (Button) findViewById(R.id.Button03);
	    button_array[3] = (Button) findViewById(R.id.Button04);
	    button_array[4] = (Button) findViewById(R.id.Button05);
	    button_array[5] = (Button) findViewById(R.id.Button06);
	    button_array[6] = (Button) findViewById(R.id.Button07);
	    button_array[7] = (Button) findViewById(R.id.Button08);
	
	    String grey = "#ffffff";
	    
		try {
			Level gpo0Level = gpio.getGpo(Gpo.GPO0);
		    Level gpo1Level = gpio.getGpo(Gpo.GPO1);
		    Level gpo2Level = gpio.getGpo(Gpo.GPO2);
		    Level gpo3Level = gpio.getGpo(Gpo.GPO3);
		    button_array[gpo0Level == Level.HIGH ? 0 : 1].setTextColor(Color.parseColor(grey));
		    button_array[gpo1Level == Level.HIGH ? 2 : 3].setTextColor(Color.parseColor(grey));
		    button_array[gpo2Level == Level.HIGH ? 4 : 5].setTextColor(Color.parseColor(grey));
		    button_array[gpo3Level == Level.HIGH ? 6 : 7].setTextColor(Color.parseColor(grey));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     
	    
	    OnClickListener myButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int index = 0;
				switch (v.getId()) {
				case R.id.Button01:
					index = 0;
					break;
				case R.id.Button02:
					index = 1;
					break;
				case R.id.Button03:
					index = 2;
					break;
				case R.id.Button04:
					index = 3;
					break;
				case R.id.Button05:
					index = 4;
					break;
				case R.id.Button06:
					index = 5;
					break;
				case R.id.Button07:
					index = 6;
					break;
				case R.id.Button08:
					index = 7;
					break;
				}
				
				button_array[index].setTextColor(Color.parseColor("#ffffff"));
				if(index%2==0)
					button_array[index+1].setTextColor(Color.parseColor("#000000"));
				else
					button_array[index-1].setTextColor(Color.parseColor("#000000"));
				
				Gpo gpo[] = {Gpo.GPO0, Gpo.GPO1, Gpo.GPO2, Gpo.GPO3};
				try {
					gpio.setGpo(gpo[index/2], index%2 == 0 ? Level.HIGH : Level.LOW);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    
	    };
	    
	    
	    for(Button button : button_array)
	    {
	    	button.setOnClickListener(myButtonListener);
	    }
	    
		
		final TextView []gpiText = new TextView[4];
		gpiText[0] = (TextView) findViewById(R.id.textView10); 
		gpiText[1] = (TextView) findViewById(R.id.textView11); 
		gpiText[2] = (TextView) findViewById(R.id.textView12); 
		gpiText[3] = (TextView) findViewById(R.id.textView13); 
	    
		
		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
	        	Level []level = (Level[]) msg.obj;
	        	
	        	for(int i = 0; i < 4; i++) {
	        		gpiText[i].setText(level[i].toString());
	        	}
			}
		};
	
		
	    new Thread(new Runnable() {
        	Level []level = new Level[4];
	        public void run() {
	        	while(true) {
	                try {
		                Thread.sleep(100);
	                	level[0] = gpio.getGpi(Gpi.GPI0);
	                	level[1] = gpio.getGpi(Gpi.GPI1);
	                	level[2] = gpio.getGpi(Gpi.GPI2);
	                	level[3] = gpio.getGpi(Gpi.GPI3);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	                Message msg = Message.obtain();
	                msg.obj = level;
	                mHandler.sendMessage(msg);
	            
	            }
	    	}
	    }).start();
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gpio_demo, menu);
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
