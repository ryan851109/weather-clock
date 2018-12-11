package com.example.finalproject_weather_clock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class Main4Activity extends AppCompatActivity {
    //Pending intent instance
    private PendingIntent pendingIntent;

    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;

    private TextView[] text = new TextView[4];
    private Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        text[0] = (TextView)findViewById(R.id.textView2);
        text[1] = (TextView)findViewById(R.id.textView4);
        text[2] = (TextView)findViewById(R.id.textView6);
        text[3] = (TextView)findViewById(R.id.textView7);
        close = (Button)findViewById(R.id.button);
        close.setOnClickListener(close_btn);
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.HOUR_OF_DAY) < 12){
            text[0].setText("上午"+String.valueOf(cal.get(Calendar.HOUR))+":"+String.valueOf(cal.get(Calendar.MINUTE)));
        }
        else{
            text[0].setText("下午"+String.valueOf(cal.get(Calendar.HOUR))+":"+String.valueOf(cal.get(Calendar.MINUTE)));
        }
        loadData();
        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(Main4Activity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(Main4Activity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
    }

    private final View.OnClickListener close_btn = new View.OnClickListener() {
        @Override
        public void onClick(View btn) {
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent


            //Stop the Media Player Service to stop sound
            stopService(new Intent(Main4Activity.this, AlarmSoundService.class));

            //remove the notification from notification tray
            NotificationManager notificationManager = (NotificationManager) Main4Activity.this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);

            //Toast.makeText(Main4Activity.this, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

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

                text[1].setText(climate5);
                text[2].setText(temperature5+"度");
                if(Integer.parseInt(temperature5) <= 16){
                    if(climate5.equals("多雲")){
                        text[3].setText("建議多穿一件外套");
                    }
                    else if(climate5.equals("晴時多雲")){
                        text[3].setText("建議多穿一件外套");
                    }
                    else if(climate5.equals("多雲時晴")){
                        text[3].setText("建議多穿一件外套");
                    }
                    else if(climate5.equals("多雲時陰短暫雨")){
                        text[3].setText("天氣濕冷小心著涼，出門建議帶傘");
                    }
                    else if(climate5.equals("陰時多雲")){
                        text[3].setText("建議多穿一件外套");
                    }
                }
                else if(Integer.parseInt(temperature5) <= 20){
                    if(climate5.equals("多雲")){
                        text[3].setText("涼爽的天氣");
                    }
                    else if(climate5.equals("晴時多雲")){
                        text[3].setText("舒適的天氣");
                    }
                    else if(climate5.equals("多雲時晴")){
                        text[3].setText("舒適的天氣");
                    }
                    else if(climate5.equals("多雲時陰短暫雨")){
                        text[3].setText("出門建議帶傘");
                    }
                    else if(climate5.equals("陰時多雲")){
                        text[3].setText("天氣有點寒冷喔!!!");
                    }
                }
                else if(Integer.parseInt(temperature5) <= 28){
                    if(climate5.equals("多雲")){
                        text[3].setText("舒適的天氣");
                    }
                    else if(climate5.equals("晴時多雲")){
                        text[3].setText("舒適的天氣，真適合出遊");
                    }
                    else if(climate5.equals("多雲時晴")){
                        text[3].setText("舒適的天氣，真適合出遊");
                    }
                    else if(climate5.equals("多雲時陰短暫雨")){
                        text[3].setText("出門帶傘小心陣雨");
                    }
                    else if(climate5.equals("陰時多雲")){
                        text[3].setText("舒適的天氣");
                    }

                }
                else{
                    if(climate5.equals("多雲")){
                        text[3].setText("舒適的天氣");
                    }
                    else if(climate5.equals("晴時多雲")){
                        text[3].setText("悶熱的天氣，小心熱壞了");
                    }
                    else if(climate5.equals("多雲時晴")){
                        text[3].setText("悶熱的天氣，小心熱壞了");
                    }
                    else if(climate5.equals("多雲時陰短暫雨")){
                        text[3].setText("出門帶傘小心陣雨");
                    }
                    else if(climate5.equals("陰時多雲")){
                        text[3].setText("舒適的天氣");
                    }

                }
                //////////////////todayweather.setText(temperature5 + "度" + "   " + climate5);
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
}
