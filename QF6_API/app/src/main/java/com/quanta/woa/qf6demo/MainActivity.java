package com.quanta.woa.qf6demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.quanta.woa.qf6demo.irdemo.IrDemoMain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] demoStrings = {"IR Demo"};

        final ListView listView = (ListView) findViewById(R.id.listView);
        LargeArrayAdapter<String> arrayAdapter =
                new LargeArrayAdapter<>(this, android.R.layout.simple_list_item_1, demoStrings);
        listView.setAdapter(arrayAdapter);
        listView.setSelector(android.R.drawable.list_selector_background);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), IrDemoMain.class));
                        break;
                }
            }
        });
    }

    class LargeArrayAdapter<T> extends ArrayAdapter<T> {

        public LargeArrayAdapter(Context context, int resource, T[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) super.getView(position, convertView, parent);
            textView.setTextSize(25);
            return textView;
        }
    }

}
