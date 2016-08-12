package com.example.ethernet_demo;

import java.io.IOException;

import com.quanta.hcbiapi.Ethernet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.io.*;
import java.util.Arrays;

import android.content.BroadcastReceiver;
import android.widget.EditText;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;

public class EthernetDemo extends Activity {

	protected static final String TAG = "ETHERNET_demo";
	private EditText mEdit1,mEdit2,mEdit3,mEdit4,mEdit5,mEdit6,mEdit7,mEdit8,mEdit9,mEdit10,mEdit11,mEdit12,mEdit13,mEdit14,mEdit15,mEdit16,mEdit17,mEdit18,mEdit19,mEdit20,mEdit21,mEdit22,mEdit23;
private TextView route_t, text_state0, text_state1;
    private CheckBox checkbox0, checkbox1;
private Ethernet ether;
    private IntentFilter mIntentFilter;
    Button [] button_array;
    private static final int button_num = 10;
    private Switch Switch0, Switch1;
    private boolean SetStateOnly = false;
    private int rtb_counter = 0;
    routetable []RTBS = new routetable[128];

  private enum States {
    CONNECTING,
    CONNECTED,
    SUSPENDED,
    DISCONNECTING,
    DISCONNECTED,
    UNKNOWN
  }

    class routetable {
        public String dest ="";
        public String gw ="";
        public String mask ="";
        public String flag ="";
        public String metric ="";
        public String ref ="";
        public String use ="";
        public String iface ="";

        public routetable() {

        }

        public int GetPrefixLen() {
            int [] mask_num = new int[4];
            int prefixlen = 0;

            String masks [ ] = mask.split("\\.");

            for(int i=0;i<4;i++) {
                mask_num[i] = Integer.valueOf(masks[i]);
                if(mask_num[i]==255){
                    prefixlen = prefixlen+8;
                }else if(mask_num[i]==0){
                    break;
                }else{
                    for(int j=7;j>=0;j--)
                    {
                        if((mask_num[i] & 1<<j)!=0){
                            prefixlen++;
                        }else{
                            break;
                        }
                    }
                    break;
                }
            }

            return prefixlen;
        }
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.android.internal.ethernet.INTERFACE_STATE_CHANGED_ACTION")) {

                Bundle extras = intent.getExtras();
                String net_state = extras.getString("ethernet.networkstate");
                String iface = extras.getString("ethernet.interface");

                Log.d(TAG, "INTERFACE_STATE_CHANGED_ACTION:" + iface + "   networkstate:" + net_state);

                if (iface.equals("eth0")) {
                    text_state0.setText(net_state);
                    State_Switch(Switch0, net_state);
                } else if (iface.equals("eth1")) {
                    text_state1.setText(net_state);
                    State_Switch(Switch1, net_state);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_ethernet_demo);
	    
	    button_array = new Button[button_num];
	    button_array[0] = (Button) findViewById(R.id.Button01);
	    button_array[1] = (Button) findViewById(R.id.Button02);
	    button_array[2] = (Button) findViewById(R.id.Button05);
	    button_array[3] = (Button) findViewById(R.id.Button06);
        button_array[4] = (Button) findViewById(R.id.Button09);
	    button_array[5] = (Button) findViewById(R.id.button);
        button_array[6] = (Button) findViewById(R.id.button2);
        button_array[7] = (Button) findViewById(R.id.button3);
        button_array[8] = (Button) findViewById(R.id.button4);
        button_array[9] = (Button) findViewById(R.id.button5);

 mEdit1   = (EditText)findViewById(R.id.editText);
  mEdit2   = (EditText)findViewById(R.id.editText2);
   mEdit3   = (EditText)findViewById(R.id.editText3);
    mEdit4   = (EditText)findViewById(R.id.editText4);
        mEdit5   = (EditText)findViewById(R.id.editText5);
        mEdit6   = (EditText)findViewById(R.id.editText6);
        mEdit7   = (EditText)findViewById(R.id.editText7);
        mEdit8   = (EditText)findViewById(R.id.editText8);
        mEdit9   = (EditText)findViewById(R.id.editText9);
        mEdit10  = (EditText)findViewById(R.id.editText10);
        mEdit11  = (EditText)findViewById(R.id.editText11);
        mEdit12  = (EditText)findViewById(R.id.editText12);
        mEdit13   = (EditText)findViewById(R.id.editText13);
        mEdit14  = (EditText)findViewById(R.id.editText14);
        mEdit15  = (EditText)findViewById(R.id.editText15);
        mEdit16  = (EditText)findViewById(R.id.editText16);
        mEdit17  = (EditText)findViewById(R.id.editText17);
        mEdit18  = (EditText)findViewById(R.id.editText18);
        mEdit19  = (EditText)findViewById(R.id.editText19);
        mEdit20  = (EditText)findViewById(R.id.editText20);
        mEdit21  = (EditText)findViewById(R.id.editText21);
        mEdit22  = (EditText)findViewById(R.id.editText22);
        mEdit23  = (EditText)findViewById(R.id.editText23);
        route_t=(TextView)findViewById(R.id.textView5);
        text_state0=(TextView)findViewById(R.id.textView6);
        text_state1=(TextView)findViewById(R.id.textView7);
        Switch0 = (Switch) findViewById(R.id.switch0);
        Switch1 = (Switch) findViewById(R.id.switch1);
        checkbox0 = (CheckBox) findViewById(R.id.checkBox0);
        checkbox1 = (CheckBox) findViewById(R.id.checkBox1);

        Switch0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(SetStateOnly==false) {
                    if (isChecked) {
                        Log.e(TAG, "Switch0 isChecked");
                        ether.reconnect_m("eth0");
                    } else {
                        Log.e(TAG, "Switch0 isChecked no");
                        ether.teardown_m("eth0");
                    }
                }
            }
        });

        Switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(SetStateOnly==false) {
                    if (isChecked) {
                        Log.e(TAG, "Switch1 isChecked");
                        ether.reconnect_m("eth1");
                    } else {
                        Log.e(TAG, "Switch1 isChecked no");
                        ether.teardown_m("eth1");
                    }
                }

            }
        });

          ether= new Ethernet(this);

        String eth0_state = ether.getNetworkState("eth0");
        text_state0.setText(eth0_state);
        State_Switch(Switch0, eth0_state);

        String eth1_state = ether.getNetworkState("eth1");
        text_state1.setText(eth1_state);
        State_Switch(Switch1, eth1_state);

	    OnClickListener myButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int index = 0;
				switch (v.getId()) {
				case R.id.Button01:
					index = 0;
					Log.e(TAG, "Button01");
                    if(checkbox0.isChecked()){
                        ether.setinterface_dp("eth0", mEdit17.getText().toString(), Integer.valueOf(mEdit18.getText().toString()), mEdit19.getText().toString());
                    }else {
                        ether.setinterface_d("eth0");
                    }
					break;
				case R.id.Button02:
					index = 1;
					Log.e(TAG, "Button02");
                    if(checkbox1.isChecked()){
                        ether.setinterface_dp("eth1", mEdit20.getText().toString(), Integer.valueOf(mEdit21.getText().toString()), mEdit22.getText().toString());
                    }else {
                        ether.setinterface_d("eth1");
                    }
					break;
				case R.id.Button05:
                    index = 2;
                    Log.e(TAG, "Button05");
                    do_exec("busybox ifconfig -a");
					break;
				case R.id.Button06:
                    index = 3;
                    Log.e(TAG, "Button06");
                    do_exec("busybox route");
					break;
				case R.id.Button09:
                        Log.e(TAG, "Button09");
                         Log.e(TAG, "1:"+mEdit1.getText().toString()+"2:"+Integer.valueOf(mEdit2.getText().toString())+"3:"+mEdit3.getText().toString()+"4:"+mEdit4.getText().toString());
                        ether.addRoutertb(mEdit1.getText().toString(), Integer.valueOf(mEdit2.getText().toString()), mEdit3.getText().toString(), mEdit4.getText().toString());
                        index = 4;
                        break;
                case R.id.button:
                        Log.e(TAG, "button");
                    ether.rmRoutertb(mEdit5.getText().toString(), Integer.valueOf(mEdit6.getText().toString()), mEdit7.getText().toString(), mEdit8.getText().toString());
                        index = 5;
                        break;
                case R.id.button2:
                        Log.e(TAG, "button2");
                        if(checkbox0.isChecked()) {
                            ether.setinterface_sp("eth0", mEdit9.getText().toString(), Integer.valueOf(mEdit10.getText().toString()), mEdit11.getText().toString(), mEdit12.getText().toString(), mEdit17.getText().toString(), Integer.valueOf(mEdit18.getText().toString()), mEdit19.getText().toString());
                        }else {
                            ether.setinterface_s("eth0", mEdit9.getText().toString(), Integer.valueOf(mEdit10.getText().toString()), mEdit11.getText().toString(), mEdit12.getText().toString());
                        }
                        index = 6;
                        break;
                case R.id.button3:
                    Log.e(TAG, "button3");
                    if(checkbox1.isChecked()) {
                        ether.setinterface_sp("eth1", mEdit13.getText().toString(), Integer.valueOf(mEdit14.getText().toString()), mEdit15.getText().toString(), mEdit16.getText().toString(), mEdit20.getText().toString(), Integer.valueOf(mEdit21.getText().toString()), mEdit22.getText().toString());
                    }else {
                        ether.setinterface_s("eth1", mEdit13.getText().toString(), Integer.valueOf(mEdit14.getText().toString()), mEdit15.getText().toString(), mEdit16.getText().toString());
                    }
                        index = 7;
                        break;
                case R.id.button4:
                    RemoveAllRouteTables();
                        index = 8;
                        break;
                case R.id.button5:
                    ether.setDefaultDNS(mEdit23.getText().toString());
                        index = 9;
                        break;
				}

                button_array[index].setBackgroundColor(Color.parseColor("#FFBF00"));
                for (int i=0;i<button_num;i++)
                {
                    if(i!=index)
                        button_array[i].setBackgroundResource(android.R.drawable.btn_default);
                }
				
			}
	    
	    };
	    
	    
	    for(Button button : button_array)
	    {
	    	button.setOnClickListener(myButtonListener);
	    }

		mIntentFilter = new IntentFilter("com.android.internal.ethernet.INTERFACE_STATE_CHANGED_ACTION");
             this.registerReceiver(mReceiver, mIntentFilter);
 
	}

String do_exec(String cmd) {  
        String s = "\n";  
        try {  
            Process p = Runtime.getRuntime().exec(cmd);  
            BufferedReader in = new BufferedReader(  
                                new InputStreamReader(p.getInputStream()));  
            String line = null;  
            while ((line = in.readLine()) != null) {  
                s += line + "\r\n";                 
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
       // text.setText(s);  
       route_t.setText("" + s);
	  // Log.e(TAG, "s = "+s);
	   
        return cmd;       
    }

    void RemoveAllRouteTables(){
        routetable [] CurrentRTBS = getCurrentRTBS();

        if(CurrentRTBS!=null) {
            for (int j = 0; j < CurrentRTBS.length; j++) {
                //Log.e(TAG, "dest:= " + CurrentRTBS[j].dest + "len:" + CurrentRTBS[j].GetPrefixLen() + "gw:" + CurrentRTBS[j].gw + "iface:" + CurrentRTBS[j].iface);
                if (CurrentRTBS[j].dest.equals("0.0.0.0") ||CurrentRTBS[j].gw.equals("0.0.0.0"))
                    continue;

                ether.rmRoutertb(CurrentRTBS[j].dest, CurrentRTBS[j].GetPrefixLen(), CurrentRTBS[j].gw, CurrentRTBS[j].iface);
            }
        }

    }

    routetable []  getCurrentRTBS() {

        for (int j = 0; j < 128; j++) {
            RTBS[j] = new routetable();
        }

        int line_count = 0;

        try {
            Process p = Runtime.getRuntime().exec("busybox route");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;

            while ((line = in.readLine()) != null) {

                String sp [ ] = line.split(" ");

                if (line_count>=2) {
                    int obj_cnt = 0;
                    for (int i = 0; i < sp.length; i++) {
                        if(!sp[i].equals("") ){
                            switch(obj_cnt){
                                case 0:
                                    if(sp[i].equals("default")){
                                        RTBS[line_count - 2].dest ="0.0.0.0";
                                    }else {
                                        RTBS[line_count - 2].dest = sp[i];
                                    }

                                    break;
                                case 1:
                                    if(sp[i].equals("*")){
                                        RTBS[line_count - 2].gw ="0.0.0.0";
                                    }else {
                                        RTBS[line_count - 2].gw = sp[i];
                                    }
                                    break;
                                case 2:
                                    RTBS[line_count-2].mask = sp[i];
                                    break;
                                case 3:
                                    RTBS[line_count-2].flag = sp[i];
                                    break;
                                case 4:
                                    RTBS[line_count-2].metric = sp[i];
                                    break;
                                case 5:
                                    RTBS[line_count-2].ref = sp[i];
                                    break;
                                case 6:
                                    RTBS[line_count-2].use = sp[i];
                                    break;
                                case 7:
                                    RTBS[line_count-2].iface = sp[i];
                                    break;
                                default:
                                    Log.e(TAG, "wrong obj_cnt number");
                                    break;
                            }

                            obj_cnt++;
                        }
                    }
                }

                line_count++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        routetable[] CurrentRTBS;

        if (line_count>=3) {
            rtb_counter = line_count - 2;
            CurrentRTBS = Arrays.copyOf(RTBS, rtb_counter);
        }else{
            CurrentRTBS = null;
        }

        return CurrentRTBS;
    }

    boolean State_Switch(Switch iswitch, String net_state) {

        SetStateOnly = true;

        States currentState = States.valueOf(net_state);

        switch (currentState) {
            case CONNECTING:
                iswitch.setChecked(true);
                iswitch.setEnabled(false);
                Log.d(TAG, "CONNECTING");
                break;
            case CONNECTED:
                iswitch.setChecked(true);
                iswitch.setEnabled(true);
                Log.d(TAG, "CONNECTED");
                break;
            case SUSPENDED:
                iswitch.setChecked(true);
                iswitch.setEnabled(true);
                Log.d(TAG, "SUSPENDED");
                break;
            case DISCONNECTING:
                iswitch.setChecked(true);
                iswitch.setEnabled(false);
                Log.d(TAG, "DISCONNECTING");
                break;
            case DISCONNECTED:
                iswitch.setChecked(false);
                iswitch.setEnabled(true);
                Log.d(TAG, "DISCONNECTED");
                break;
            case UNKNOWN:
                iswitch.setChecked(false);
                iswitch.setEnabled(true);
                Log.d(TAG, "UNKNOWN");
                break;
            default:
        }

        SetStateOnly = false;

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ethernet_demo, menu);
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
