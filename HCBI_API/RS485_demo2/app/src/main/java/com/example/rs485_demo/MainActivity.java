package com.example.rs485_demo;

import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.quanta.hcbiapi.*;
import com.quanta.hcbiapi.Adam4055.*;
import com.example.rs485_demo.Modbus;
import com.example.rs485_demo.ModbusMessage;

public class MainActivity extends Activity {

	private ModbusMessage request;
	private byte[] data_tx ;
	protected static final String TAG = "RS485_demo2";
	Button [] button_array;
	Adam4055 adam4055_0;
	Adam4055 adam4055_1;
	CountDownTimer mCountDownTimer0;
	CountDownTimer mCountDownTimer1;
	private static final int MSG_RECEIVE_TIMEOUT = 1;
	private static boolean check_rcv_data0 = false;
	private static boolean check_rcv_data1 = false;
	private static byte []rcv_data0 = new byte [256];
	private static byte []rcv_data1 = new byte [256];;
	private static int data_index0 = 0;
	private static int data_index1 = 0;
	private static byte current_send_id0 = 0;
	private static byte current_send_id1 = 0;
	private static byte current_send_func0 = 0;
	private static byte current_send_func1 = 0;
	private static TextView textView;
	private static final int PORT_0 = 0;
	private static final int PORT_1 = 1;
	private static final int BUTTON_NUM = 20;
	private static final int TIMOUT_MSEC = 20;

	/* Table of CRC values for high-order byte */
	private final static int table_crc_hi[] = {
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
			0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
			0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
			0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
			0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
			0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
			0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
			0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
			0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
			0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
			0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
			0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
			0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
			0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
	};

	/* Table of CRC values for low-order byte */
	private final static int table_crc_lo[] = {
			0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06,
			0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD,
			0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09,
			0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A,
			0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,
			0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
			0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3,
			0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,
			0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A,
			0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29,
			0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED,
			0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
			0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,
			0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
			0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F,
			0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,
			0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E,
			0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
			0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71,
			0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92,
			0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C,
			0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B,
			0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,
			0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
			0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,
			0x43, 0x83, 0x41, 0x81, 0x80, 0x40
	};

	class SubReq {
		private int file_num;
		private int start_addr;
		private int reg_count;
		private int[] data;

		public SubReq(int file_num, int start_addr, int reg_count) {
			this.file_num = file_num;
			this.start_addr = start_addr;
			this.reg_count = reg_count;
		}

		public SubReq(int file_num, int start_addr, int reg_count, int[] data) {
			this.file_num = file_num;
			this.start_addr = start_addr;
			this.reg_count = reg_count;
			this.data = data;
		}
		public int file_num() { return file_num; }
		public int start_addr() { return start_addr; }
		public int reg_count() { return reg_count; }
		public int[] getdatas() { return data; }
		public int getdata(int i) { return data[i]; }
	}

	public class Adam4055_thread0 extends Thread
	{
		private boolean isRunning = true;

		public void run()
		{
			byte []data = new byte [1];

			while(isRunning) {
				int ret;
				try {
					mCountDownTimer0.start();

					ret = adam4055_0.read(data);

					mCountDownTimer0.cancel();

					if (check_rcv_data0 == true) {
						rcv_data0[data_index0] = data[0];
						data_index0++;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		public void stopThread()
		{
			this.isRunning = false;
		}
	}

	public class Adam4055_thread1 extends Thread
	{
		private boolean isRunning = true;

		public void run()
		{
			byte []data = new byte [1];

			while(isRunning) {
				int ret;
				try {
					mCountDownTimer1.start();

					ret = adam4055_1.read(data);

					mCountDownTimer1.cancel();
					Log.e(TAG, "data" + data[0]);
					if (check_rcv_data1 == true) {
						rcv_data1[data_index1] = data[0];
						data_index1++;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		public void stopThread()
		{
			this.isRunning = false;
		}
	}

	private Handler mHandler0 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//Log.w("TAG", "Got message " + msg.what);
			switch (msg.what) {
				case MSG_RECEIVE_TIMEOUT:

					mCountDownTimer0.cancel();

					if (check_rcv_data0 == true)
					{
						boolean ret;
						ret = CheckReceivedData(PORT_0, data_index0, rcv_data0);

						StopReceive(PORT_0);
					}

					break;
				default:
					StopReceive(PORT_0);
					mCountDownTimer0.cancel();
					break;
			}

		}
	};

	private Handler mHandler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//Log.w("TAG", "Got message " + msg.what);
			switch (msg.what) {
				case MSG_RECEIVE_TIMEOUT:

					mCountDownTimer1.cancel();

					if (check_rcv_data1 == true)
					{
						boolean ret;
						ret = CheckReceivedData(PORT_1, data_index1, rcv_data1);

						StopReceive(PORT_1);
					}

					break;
				default:
					StopReceive(PORT_1);
					mCountDownTimer1.cancel();
					break;
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			adam4055_0 = new Adam4055(Rs485.Port.PORT0,BaudRate.BPS9600);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			adam4055_1 = new Adam4055(Rs485.Port.PORT1,BaudRate.BPS9600);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		button_array = new Button[BUTTON_NUM];
		button_array[0] = (Button) findViewById(R.id.Button01);
		button_array[1] = (Button) findViewById(R.id.Button02);
		button_array[2] = (Button) findViewById(R.id.Button03);
		button_array[3] = (Button) findViewById(R.id.Button04);
		button_array[4] = (Button) findViewById(R.id.Button05);
		button_array[5] = (Button) findViewById(R.id.Button06);
		button_array[6] = (Button) findViewById(R.id.Button07);
		button_array[7] = (Button) findViewById(R.id.Button08);
		button_array[8] = (Button) findViewById(R.id.Button09);
		button_array[9] = (Button) findViewById(R.id.Button10);
		button_array[10] = (Button) findViewById(R.id.Button11);
		button_array[11] = (Button) findViewById(R.id.Button12);
		button_array[12] = (Button) findViewById(R.id.Button13);
		button_array[13] = (Button) findViewById(R.id.Button14);
		button_array[14] = (Button) findViewById(R.id.Button15);
		button_array[15] = (Button) findViewById(R.id.Button16);
		button_array[16] = (Button) findViewById(R.id.Button17);
		button_array[17] = (Button) findViewById(R.id.Button18);
		button_array[18] = (Button) findViewById(R.id.Button19);
		button_array[19] = (Button) findViewById(R.id.Button20);

		String grey = "#ffffff";

		for(int i = 1; i <= 19; i+=2)
		{
			button_array[i].setTextColor(Color.parseColor(grey));
		}

		textView = (TextView) findViewById(R.id.textView9);
		textView.setMovementMethod(new ScrollingMovementMethod());

		mCountDownTimer0 = new CountDownTimer(TIMOUT_MSEC,TIMOUT_MSEC) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				int mcount = (int) (millisUntilFinished / 1);
			}

			@Override
			public void onFinish() {
				mHandler0.sendEmptyMessage(MSG_RECEIVE_TIMEOUT);
			}
		};

		mCountDownTimer1 = new CountDownTimer(TIMOUT_MSEC,TIMOUT_MSEC) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				//int mcount = (int) (millisUntilFinished / 1);
			}

			@Override
			public void onFinish() {
				mHandler1.sendEmptyMessage(MSG_RECEIVE_TIMEOUT);
			}
		};

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
					case R.id.Button09:
						index = 8;
						break;
					case R.id.Button10:
						index = 9;
						break;
					case R.id.Button11:
						index = 10;
						break;
					case R.id.Button12:
						index = 11;
						break;
					case R.id.Button13:
						index = 12;
						break;
					case R.id.Button14:
						index = 13;
						break;
					case R.id.Button15:
						index = 14;
						break;
					case R.id.Button16:
						index = 15;
						break;
					case R.id.Button17:
						index = 16;
						break;
					case R.id.Button18:
						index = 17;
						break;
					case R.id.Button19:
						index = 18;
						break;
					case R.id.Button20:
						index = 19;
						break;
				}


				button_array[index].setTextColor(Color.parseColor("#ffffff"));
				if(index%2==0)
					button_array[index+1].setTextColor(Color.parseColor("#000000"));
				else
					button_array[index-1].setTextColor(Color.parseColor("#000000"));

				if(index <=15) {
					Gpo gpo[] = {Gpo.GPO0, Gpo.GPO1, Gpo.GPO2, Gpo.GPO3, Gpo.GPO4, Gpo.GPO5, Gpo.GPO6, Gpo.GPO7};
					adam4055_0.setGpo(gpo[index / 2], index % 2 == 0 ? Level.HIGH : Level.LOW);
				}


				/* ======================================TEST================================================*/
				if (index == 16) {
					byte[] values = {0x10,0x11,0x12,0x13,0x14,0x15};

					sendRaw(values,PORT_0);
					ReadyToReceive(PORT_0);
				}

				if(index == 17){
					int[] values1 = {0x1234,0x5678};
					int[] values2 = {0x8888,0x9999};

					SubReq[] subreqs = {new SubReq(8,100,2,values1),new SubReq(9,101,2,values2)};

					F21_WriteGenegalReference(123, subreqs, PORT_1);
					ReadyToReceive(PORT_1);
				}

				if (index == 18)
				{
					F5_ForceSingleCoil(0x10, 0x20, 1, PORT_0);
					ReadyToReceive(PORT_0);
				}

				if (index == 19) {
					int[] values = {97,97,97,98,99,100,101,102, 103};

					F16_PresetMultipleRegs(123, 0x123, values.length, values, PORT_0);
					ReadyToReceive(PORT_0);

				}
				/* ======================================TEST================================================*/
			}

		};

		for(int i = 0; i < BUTTON_NUM; i++)
		{
			button_array[i].setOnClickListener(myButtonListener);
		}

		Adam4055_thread0 Thread0 = new Adam4055_thread0();
		Thread0.start();

		Adam4055_thread1 Thread1 = new Adam4055_thread1();
		Thread1.start();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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


	public boolean sendRaw(byte[] raw_data, int port) {

		data_tx = new byte[raw_data.length];
		data_tx = Arrays.copyOf(raw_data, raw_data.length);

		if(port == 0) {
			new Thread(new Runnable() {
				public void run() {
					try {
						adam4055_0.write(data_tx);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}else if(port ==1){
			new Thread(new Runnable() {
				public void run() {
					try {
						adam4055_1.write(data_tx);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}

		return true;
	}

	public boolean frame_init (ModbusMessage request,
							   byte function,
							   int slave_addr,
							   int start_addr,
							   int port)
			throws IllegalArgumentException{
		if (slave_addr > Modbus.UINT8_MAX) {
			throw new IllegalArgumentException("Unit ID is out of range of 8 bit UINT");
		}
		if (slave_addr < Modbus.UINT8_MIN) {
			throw new IllegalArgumentException("Unit ID is out of range of 8 bit UINT");
		}

		if (start_addr > Modbus.UINT16_MAX) {
			throw new IllegalArgumentException("Reference number is out of range of 16 bit UINT");
		}

		if (start_addr < Modbus.UINT16_MIN) {
			throw new IllegalArgumentException("Reference number is out of range of 16 bit UINT");
		}

		if(port==0) {
			current_send_id0 = (byte) ((slave_addr >> 0) & 0xFF);
			current_send_func0 = function;
		}else if(port==1){
			current_send_id1 = (byte) ((slave_addr >> 0) & 0xFF);
			current_send_func1 = function;
		}

		request.buff[0] = (byte) ((slave_addr >> 0) & 0xFF);
		request.buff[1] = function;
		request.buff[2] = (byte) ((start_addr >> 8) & 0xFF);
		request.buff[3] = (byte) ((start_addr >> 0) & 0xFF);

		return true;
	}

	public boolean frame_init (ModbusMessage request,
							   byte function,
							   int slave_addr,
							   int port)
			throws IllegalArgumentException{
		if (slave_addr > Modbus.UINT8_MAX) {
			throw new IllegalArgumentException("Unit ID is out of range of 8 bit UINT");
		}
		if (slave_addr < Modbus.UINT8_MIN) {
			throw new IllegalArgumentException("Unit ID is out of range of 8 bit UINT");
		}

		if(port==0) {
			current_send_id0 = (byte) ((slave_addr >> 0) & 0xFF);
			current_send_func0 = function;
		}else if(port==1){
			current_send_id1 = (byte) ((slave_addr >> 0) & 0xFF);
			current_send_func1 = function;
		}

		request.buff[0] = (byte) ((slave_addr >> 0) & 0xFF);
		request.buff[1] = function;

		return true;
	}


	public boolean sendFrame(ModbusMessage msg, int port) {
		int i;
		int length;
		int crc;

		crc = crc16(msg.buff,msg.length);

		msg.buff[msg.length++] = (byte) (crc >>8);
		msg.buff[msg.length++] = (byte) ( crc & 0x00FF);

		length = msg.length;
		data_tx = new byte[length];

		data_tx = Arrays.copyOf(msg.buff, length);


		for(i=0;i<length;i++)
		{
			Log.e("TAG", "data_tx["+i+"]:"+data_tx[i]+" hex: "+ Integer.toHexString(((int)data_tx[i])&0xff));
		}

		if (Modbus.debug >= 4) {
			Log.e("TAG", "Modbus: Sending Frame");
			Log.e("TAG", "Frame Length: " + msg.length);
		}

		if(port == 0) {
			new Thread(new Runnable() {
				public void run() {
					try {
						adam4055_0.write(data_tx);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}else if(port ==1){
			new Thread(new Runnable() {
				public void run() {
					try {
						adam4055_1.write(data_tx);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}

		return true;
	}

	static int crc16(byte[] buffer, int buffer_length)
	{
		int crc_hi = 0xFF; /* high CRC byte initialized */
		int crc_lo = 0xFF; /* low CRC byte initialized */
		int i; /* will index into CRC lookup */
		int index = 0;
    /* pass through message buffer */
		while (buffer_length!=0) {
			i =crc_hi^ ((int)(buffer[index]& 0xFF)); /* calculate the CRC  */

			crc_hi =crc_lo ^ ( table_crc_hi[i]);
			crc_lo = table_crc_lo[i];

			index++;
			buffer_length--;
		}

		return (crc_hi << 8 | crc_lo);
	}

	public void ReadyToReceive(int port){

		if(port == 0) {
			check_rcv_data0 = true;
			data_index0 = 0;
		}else if(port ==1 ){
			check_rcv_data1 = true;
			data_index1 = 0;
		}
	}

	public void StopReceive(int port){

		if (port ==0) {
			check_rcv_data0 = false;
			data_index0 = 0;
			current_send_id0 = 0;
			current_send_func0 = 0;
		}else if(port ==1){
			check_rcv_data1 = false;
			data_index1 = 0;
			current_send_id1 = 0;
			current_send_func1 = 0;
		}
	}

	public boolean CheckReceivedData(int port, int data_index, byte []rcv_data) {

		if(data_index <2)
		{
			Log.e("TAG", "Data length error!!  port="+port);
			return false;
		}

		textView.append("PORT"+port+": ");
		int i = 0;
		for (i = 0; i < data_index; i++) {
			textView.append(Integer.toHexString(((int) rcv_data[i]) & 0xff) + " ");
		}


		if(port == PORT_0) {
			if(current_send_id0 != rcv_data[0] || current_send_func0 != rcv_data[1])
			{
				textView.append("\n***Received ID or function error for PORT0***\n");
				return false;
			}
		}else if(port == PORT_1){
			if(current_send_id1 != rcv_data[0] || current_send_func1 != rcv_data[1])
			{
				textView.append("\n***Received ID or function error for PORT1***\n");
				return false;
			}
		}

		byte[] data_rx = Arrays.copyOf(rcv_data, data_index - 2);

		int crc = crc16(data_rx, data_index - 2);

		if (rcv_data[data_index - 2] == (byte) (crc >> 8) && rcv_data[data_index - 1] == (byte) (crc & 0x00FF)) {
			Log.w("TAG", "Receive data CRC check pass!! ");
			textView.append(" CRC pass \n");
		} else {
			Log.e("TAG", "Receive data CRC check fail!!");
			textView.append(" CRC fail \n");
			return false;
		}

		return true;
	}



	public boolean F1_ReadCoilStatus(int slave_addr,
									 int start_addr,
									 int length,
									 int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_COIL_STATUS, slave_addr, start_addr, port);

		request.buff[4] = (byte) ((length >> 8) & 0xFF);
		request.buff[5] = (byte) ((length >> 0) & 0xFF);
		request.length = 6;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F2_ReadInputStatus(int slave_addr,
									  int start_addr,
									  int length,
									  int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_INPUT_STATUS, slave_addr, start_addr, port);

		request.buff[4] = (byte) ((length >> 8) & 0xFF);
		request.buff[5] = (byte) ((length >> 0) & 0xFF);
		request.length = 6;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F3_ReadHoldingRegisters(int slave_addr,
										   int start_addr,
										   int length,
										   int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_HOLDING_REGISTERS, slave_addr, start_addr, port);

		request.buff[4] = (byte) ((length >> 8) & 0xFF);
		request.buff[5] = (byte) ((length >> 0) & 0xFF);
		request.length = 6;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F4_ReadInputRegisters(int slave_addr,
										 int start_addr,
										 int length,
										 int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_INPUT_REGISTERS, slave_addr, start_addr, port);

		request.buff[4] = (byte) ((length >> 8) & 0xFF);
		request.buff[5] = (byte) ((length >> 0) & 0xFF);
		request.length = 6;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F5_ForceSingleCoil(int slave_addr,
									  int start_addr,
									  int on,
									  int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.FORCE_SINGLE_COIL, slave_addr, start_addr, port);


		int pdu_index = 4;

		request.buff[pdu_index] = (byte)( (on ==0)?0x00:0xFF);
		request.buff[pdu_index+1] = (byte) 0x00;

		request.length = pdu_index + 2;


		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}


		return true;
	}

	public boolean F6_PresetSingleRegister(int slave_addr,
										   int start_addr,
										   int value,
										   int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.PRESET_SINGLE_REGISTER, slave_addr, start_addr, port);


		int pdu_index = 4;

		request.buff[pdu_index] = (byte) ((value >> 8) & 0xFF);
		request.buff[pdu_index+1] = (byte) ((value >> 0) & 0xFF);

		request.length = pdu_index + 2;


		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}


		return true;
	}

	public boolean F7_ReadExceptionStatus(int slave_addr,
										  int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_EXCEPTION_STATUS, slave_addr, port);

		int pdu_index = 2;

		request.length = pdu_index;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}


		return true;
	}

	public boolean F8_DiagnosticsQuery(int slave_addr,
									   int subfunction,
									   int data,
									   int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.DIAGNOSTICS_QUERY, slave_addr, port);

		request.buff[2] = (byte) ((subfunction >> 8) & 0xFF);
		request.buff[3] = (byte) ((subfunction >> 0) & 0xFF);
		request.buff[4] = (byte) ((data >> 8) & 0xFF);
		request.buff[5] = (byte) ((data >> 0) & 0xFF);

		request.length = 6;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F11_FetchCommEventCounter(int slave_addr,
											 int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.FETCH_COMM_EVENT_COUNTER, slave_addr, port);

		int pdu_index = 2;

		request.length = pdu_index;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}


		return true;
	}

	public boolean F12_FetchCommEventCounter(int slave_addr,
											 int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.FETCH_COMM_EVENT_LOG, slave_addr, port);

		int pdu_index = 2;

		request.length = pdu_index;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}


		return true;
	}

	public boolean F15_ForceMultipleCoils(int slave_addr,
										  int start_addr,
										  int length, //bit length
										  byte[] values,
										  int port)
			throws IllegalArgumentException {


		request = new ModbusMessage();

		frame_init(request,  Modbus.FORCE_MULTIPLE_COILS, slave_addr, start_addr, port);

		request.buff[4] = (byte) ((length>> 8) & 0xFF);
		request.buff[5] = (byte) ((length>> 0) & 0xFF);
		request.buff[6] = (byte) (length/8 +1);

		int pdu_index = 7;

		for (int i=0; i<(length/8 +1); i++) {
			request.buff[pdu_index+i] = values[i];
		}

		request.length =pdu_index + (length/8 +1);

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F16_PresetMultipleRegs(int slave_addr,
										  int start_addr,
										  int length,  //word length
										  int[] values, //word data
										  int port)
			throws IllegalArgumentException {


		request = new ModbusMessage();

		frame_init(request,  Modbus.PRESET_MULTIPLE_REGISTERS, slave_addr, start_addr, port);

		request.buff[4] = (byte) ((length >> 8) & 0xFF);
		request.buff[5] = (byte) ((length >> 0) & 0xFF);
		request.buff[6] = (byte) (((2*length) >> 0) & 0xFF);

		int pdu_index = 7;

		for (int i=0; i<length; i++) {
			request.buff[pdu_index+(2*i)] = (byte) ((values[i] >> 8) & 0xFF);
			request.buff[pdu_index+1+(2*i)] = (byte) ((values[i] >> 0) & 0xFF);
		}

		request.length =pdu_index + 2*length;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F17_ReportSlaveID(int slave_addr,
									 int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.REPORT_SLAVE_ID, slave_addr, port);

		int pdu_index = 2;

		request.length = pdu_index;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F20_ReadGenegalReference(int slave_addr,
											SubReq[] subreqs,
											int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_GENERAL_REFERENCE, slave_addr, port);

		request.buff[2] = (byte) (subreqs.length * 7);  //byte count

		int pdu_index = 3;

		for(int i=0;i<subreqs.length;i++){
			request.buff[3+i*7] = 6;
			request.buff[4+i*7] = (byte) ((subreqs[i].file_num() >> 8) & 0xFF);
			request.buff[5+i*7] = (byte) ((subreqs[i].file_num() >> 0) & 0xFF);
			request.buff[6+i*7] = (byte) ((subreqs[i].start_addr() >> 8) & 0xFF);
			request.buff[7+i*7] = (byte) ((subreqs[i].start_addr() >> 0) & 0xFF);
			request.buff[8+i*7] = (byte) ((subreqs[i].reg_count() >> 8) & 0xFF);
			request.buff[9+i*7] = (byte) ((subreqs[i].reg_count() >> 0) & 0xFF);
		}

		request.length =pdu_index + 7*subreqs.length;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F21_WriteGenegalReference(int slave_addr,
											 SubReq[] subreqs,
											 int port)
			throws IllegalArgumentException {
		int j;

		request = new ModbusMessage();

		frame_init(request,  Modbus.WRITE_GENERAL_REFERENCE, slave_addr, port);

		request.buff[2] = (byte) (subreqs.length * 7);  //byte count

		int pdu_index = 3;

		for(int i=0;i<subreqs.length;i++){
			request.buff[2] += 2*subreqs[i].reg_count();
			request.buff[pdu_index] = 6;
			request.buff[pdu_index+1] = (byte) ((subreqs[i].file_num() >> 8) & 0xFF);
			request.buff[pdu_index+2] = (byte) ((subreqs[i].file_num() >> 0) & 0xFF);
			request.buff[pdu_index+3] = (byte) ((subreqs[i].start_addr() >> 8) & 0xFF);
			request.buff[pdu_index+4] = (byte) ((subreqs[i].start_addr() >> 0) & 0xFF);
			request.buff[pdu_index+5] = (byte) ((subreqs[i].reg_count() >> 8) & 0xFF);
			request.buff[pdu_index+6] = (byte) ((subreqs[i].reg_count() >> 0) & 0xFF);
			for(j=0;j<subreqs[i].reg_count();j++){
				request.buff[pdu_index+7+j*2] = (byte) ((subreqs[i].getdata(j) >> 8) & 0xFF);
				request.buff[pdu_index+8+j*2] = (byte) ((subreqs[i].getdata(j) >> 0) & 0xFF);
			}
			pdu_index += 7 + j*2;
		}

		request.length =pdu_index;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F22_MaskWrite4XRegister(int slave_addr,
										   int start_addr,
										   int and_mask,
										   int or_mask,
										   int port)
			throws IllegalArgumentException {


		request = new ModbusMessage();

		frame_init(request,  Modbus.MASK_WRITE_4X_REGISTER, slave_addr, start_addr, port);

		request.buff[4] = (byte) ((and_mask >> 8) & 0xFF);
		request.buff[5] = (byte) ((and_mask >> 0) & 0xFF);
		request.buff[6] = (byte) ((or_mask >> 8) & 0xFF);
		request.buff[7] = (byte) ((or_mask >> 0) & 0xFF);

		request.length =8;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F23_ReadWrite4XRegisters(int slave_addr,
											int read_addr,
											int read_num,
											int write_addr,
											int []write_value,
											int port)
			throws IllegalArgumentException {
		int i;

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_WRITE_4X_REGISTERS, slave_addr, port);

		request.buff[2] = (byte) ((read_addr >> 8) & 0xFF);
		request.buff[3] = (byte) ((read_addr >> 0) & 0xFF);
		request.buff[4] = (byte) ((read_num >> 8) & 0xFF);
		request.buff[5] = (byte) ((read_num >> 0) & 0xFF);
		request.buff[6] = (byte) ((write_addr >> 8) & 0xFF);
		request.buff[7] = (byte) ((write_addr >> 0) & 0xFF);
		request.buff[8] = (byte) ((write_value.length >> 8) & 0xFF);
		request.buff[9] = (byte) ((write_value.length>> 0) & 0xFF);
		request.buff[10] = (byte) (((write_value.length*2)>> 0) & 0xFF);

		for(i=0;i<write_value.length;i++){
			request.buff[11+2*i] = (byte) ((write_value[i] >> 8) & 0xFF);
			request.buff[12+2*i] = (byte) ((write_value[i] >> 0) & 0xFF);
		}

		request.length =11+2*i;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}

	public boolean F24_ReadFIFOQueue(int slave_addr,
									 int start_addr,
									 int length,
									 int port)
			throws IllegalArgumentException {

		request = new ModbusMessage();

		frame_init(request,  Modbus.READ_FIFO_QUEUE, slave_addr, start_addr, port);

		request.length = 4;

		if (!sendFrame(request, port)) {
			if (Modbus.debug >= 2) {
				Log.e("TAG", "ModbusMaster: sendFrame failed!");
			}
			return false;
		}

		return true;
	}


}
