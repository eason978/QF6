package com.quanta.woa.sysapidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.quanta.hcbiapi.Sys;

import java.io.IOException;

/**
 * IMPORTANT: The code below uses HCBIAPI version 0.5
 */

public class MainActivity extends AppCompatActivity {

    final static String TAG = "System API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * CPU Usage
         */
        TextView textView10 = (TextView) findViewById(R.id.textView10);
        String result = null;
        try {
            result = "CPU Usage: " + Sys.getCpuUsagePercent() + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView10.setText(result);

        /**
         * IFWI version
         */
        TextView textView11 = (TextView) findViewById(R.id.textView11);
        String ifwiVer = null;
        try {
            ifwiVer = "IFWI Version: " + Sys.getIfwiVer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView11.setText(ifwiVer);

        /**
         * Disk Usage
         */
        TextView textView12 = (TextView) findViewById(R.id.textView12);
        String diskInfo =
                "Internal Free: " + Sys.getAvailableBytes(Sys.INTERNAL_PATH) + "\n" +
                        "Internal Used: " + Sys.getUsedBytes(Sys.INTERNAL_PATH) + "\n" +
                        "Internal Total: " + Sys.getTotalBytes(Sys.INTERNAL_PATH) + "\n" +
                        "SDCard Available: " + Sys.getAvailableBytes(Sys.SDCARD_PATH) + "\n" +
                        "SDCard Used: " + Sys.getUsedBytes(Sys.SDCARD_PATH) + "\n" +
                        "SDCard Total: " + Sys.getTotalBytes(Sys.SDCARD_PATH) + "\n" +
                        "USB Free: " + Sys.getAvailableBytes(Sys.USB_PATH) + "\n" +
                        "USB Used: " + Sys.getUsedBytes(Sys.USB_PATH) + "\n" +
                        "USB Total: " + Sys.getTotalBytes(Sys.USB_PATH);
        textView12.setText(diskInfo);

        /**
         * Hardware Monitor Temperature
         */
        TextView textView13 = (TextView) findViewById(R.id.textView13);
        String core0 = "";
        try {
            core0 = "CORE0: " + Sys.getHardwareMonitorReading(Sys.HWMON_CORE0_TEMP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView13.setText(core0);

        TextView textView14 = (TextView) findViewById(R.id.textView14);
        String core1 = "";
        try {
            core1 = "CORE1: " + Sys.getHardwareMonitorReading(Sys.HWMON_CORE1_TEMP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView14.setText(core1);

        TextView textView15 = (TextView) findViewById(R.id.textView15);
        String core2 = "";
        try {
            core2 = "CORE2: " + Sys.getHardwareMonitorReading(Sys.HWMON_CORE2_TEMP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView15.setText(core2);

        TextView textView16 = (TextView) findViewById(R.id.textView16);
        String core3 = "";
        try {
            core3 = "CORE3: " + Sys.getHardwareMonitorReading(Sys.HWMON_CORE3_TEMP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView16.setText(core3);

        TextView textView17 = (TextView) findViewById(R.id.textView17);
        String lm75 = "";
        try {
            lm75 = "LM75: " + Sys.getHardwareMonitorReading(Sys.HWMON_LM75);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView17.setText(lm75);

        TextView textView18 = (TextView) findViewById(R.id.textView18);
        String soc_dts0 = "";
        try {
            soc_dts0 = "SOC_DTS0: " + Sys.getHardwareMonitorReading(Sys.HWMON_SOC_DTS0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView18.setText(soc_dts0);

        TextView textView19 = (TextView) findViewById(R.id.textView19);
        String soc_dts1 = "";
        try {
            soc_dts1 = "SOC_DTS1: " + Sys.getHardwareMonitorReading(Sys.HWMON_SOC_DTS1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView19.setText(soc_dts1);
    }
}
