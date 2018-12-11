package com.example.finalproject_weather_clock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private Button create;
    private TextView todayweather;
    private ListView listview;
    private ArrayAdapter<Info> adapter;

    getcontext context= new getcontext();
    private DBHelper database = new DBHelper(context.getAppContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        create = (Button)findViewById(R.id.button);
        todayweather = (TextView)findViewById(R.id.textView);

        create.setOnClickListener(Create);


        listview = (ListView) findViewById(R.id.listview);
        try{
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
            ArrayList<Info> item_list =database.getAllTaskItems();
            for (Info info : item_list) {
                adapter.add(info);
            }
            listview.setAdapter(adapter);
        }
        catch (Exception e){
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Main3Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("NUMBER",String.valueOf(arg3));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                MainActivity.this.finish();
            }
        });
    }

    private void loadData(){
        String urlString = "http://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001/?Authorization=CWB-335CE2D4-DF78-4636-AFB3-E3AA56F8E5C0";

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /*Toast.makeText(getApplicationContext(),
                        "Success!", Toast.LENGTH_LONG).show();*/
                Log.d("JSONobject response", response.toString());
                JSONObject list = response.optJSONObject("records");
                if(list == null)
                {
                    //Toast.makeText(getApplicationContext(),"Data error1", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONArray row = list.optJSONArray("location");
                JSONObject row1 = row.optJSONObject(13);
                JSONArray list2 = row1.optJSONArray("weatherElement");
                if(list2 == null)
                {
                    //Toast.makeText(getApplicationContext(),"Data error2", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONObject climate = list2.optJSONObject(0);
                JSONObject temperature = list2.optJSONObject(2);

                JSONArray climate2 = climate.optJSONArray("time");
                JSONArray temperature2 = temperature.optJSONArray("time");
                if(climate2 == null)
                {
                    //Toast.makeText(getApplicationContext(),"Data error3", Toast.LENGTH_LONG).show();
                    return;
                }
                if(temperature2 == null)
                {
                    //Toast.makeText(getApplicationContext(),"Data error4", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject climate3 = climate2.optJSONObject(0);
                JSONObject climate4 = climate3.optJSONObject("parameter");
                String climate5 = climate4.optString("parameterName");

                JSONObject temperature3 = temperature2.optJSONObject(0);
                JSONObject temperature4 = temperature3.optJSONObject("parameter");
                String temperature5 = temperature4.optString("parameterName");

                todayweather.setText(temperature5 + "度" + "   " + climate5);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject error) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + statusCode + " " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                // Log error message
                Log.e("Hot Text:", statusCode + " " + e.getMessage());
            }

        });
    }


    private final View.OnClickListener Create = new View.OnClickListener() {
        @Override
        public void onClick(View btn) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Main2Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            MainActivity.this.finish();
        }
    };

}
