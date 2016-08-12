package com.example.android.helloactivity;

import android.app.Activity;
import android.binder.example.IDemoAPI;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * A minimal "Hello, World!" application.
 */
public class HelloActivity extends Activity {
    private IDemoAPI mService;  
    private static final String TAG = "HelloActivity IDemoAPI";

    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            TextView tv = (TextView)findViewById(R.id.show_reply);
            tv.setText(s);
        }
    };
    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get RS485 sercice interface
        getDemoAPIService();

        try {
            mService.openRS485("/dev/ttyUSB0", 0, 9600);
        }
        catch(Exception e) {
            Log.d(TAG,e.toString());
        }

        // Set the layout for this activity.  You can find it
        // in res/layout/hello_activity.xml
        View view = getLayoutInflater().inflate(R.layout.hello_activity, null);
        setContentView(view);

        //Send command
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                doSend();
            }
        });
    }

    private void doSend()
    {
        try
        {
            TextView Tsend_data = (TextView)findViewById(R.id.send_data);
            final String token = "token"+Tsend_data.getText().toString();
            String command = "#01000"+Tsend_data.getText().toString()+"\r";
            mService.sendCommand(token, command, 0, 0);

            new Thread(new Runnable() {
                public void run() {
                    try {
                        //wait 1 second
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        String reply = mService.readReply(token);
                        Message msg = Message.obtain();
                        msg.obj = reply;
                        mHandler.sendMessage(msg);
                    }
                    catch(Exception e) {
                        Log.d(TAG,e.toString());
                    }
                }
            }).start();

        }
        catch(Exception e)
        {
            Log.d(TAG,e.toString());
        }
    }


    static final String SERVICE_NAME="Demo";

    /*
     * Get binder service
     */
    private void getDemoAPIService()
    {
        IBinder binder=null;
        Log.d(TAG,"getDemoAPIService");
        try
        {
            //android.os.ServiceManager is hide class, we can not invoke them from SDK. So we have to use reflect to invoke these classes.
            Object object = new Object();
            Method getService = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            Object obj = getService.invoke(object, new Object[]{new String(SERVICE_NAME)});
            binder = (IBinder)obj;
        }
        catch(Exception e)
        {
            Log.d(TAG, e.toString());
            System.exit(0);
        }

        if(binder != null)
        {
            mService = IDemoAPI.Stub.asInterface(binder);
            Log.d(TAG, "Find binder");
        }
        else
        {
            Log.d(TAG,"Service is null.");
            System.exit(0);
        }
    }
}
