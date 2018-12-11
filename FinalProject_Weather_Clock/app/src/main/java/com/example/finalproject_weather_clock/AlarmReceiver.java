package com.example.finalproject_weather_clock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by sonu on 09/04/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    //Pending intent instance
    private PendingIntent pendingIntent;

    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;

    getcontext context2= new getcontext();
    private DBHelper database = new DBHelper(context2.getAppContext());
    boolean key = true;
    String weather = database.getToDo_Task("1").getweather();

    @Override
    public void onReceive(final Context context,final Intent intent) {

        String urlString = "http://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001/?Authorization=CWB-335CE2D4-DF78-4636-AFB3-E3AA56F8E5C0";

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(context,
                        "Success!", Toast.LENGTH_LONG).show();
                Log.d("JSONobject response", response.toString());
                JSONObject list = response.optJSONObject("records");
                if(list == null)
                {
                    Toast.makeText(context,"Data error1", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONArray row = list.optJSONArray("location");
                JSONObject row1 = row.optJSONObject(13);
                JSONArray list2 = row1.optJSONArray("weatherElement");
                if(list2 == null)
                {
                    Toast.makeText(context,"Data error2", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONObject climate = list2.optJSONObject(0);
                JSONObject temperature = list2.optJSONObject(2);

                JSONArray climate2 = climate.optJSONArray("time");
                JSONArray temperature2 = temperature.optJSONArray("time");
                if(climate2 == null)
                {
                    Toast.makeText(context,"Data error3", Toast.LENGTH_LONG).show();
                    return;
                }
                if(temperature2 == null)
                {
                    Toast.makeText(context,"Data error4", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject climate3 = climate2.optJSONObject(0);
                JSONObject climate4 = climate3.optJSONObject("parameter");
                String climate5 = climate4.optString("parameterName");

                JSONObject temperature3 = temperature2.optJSONObject(0);
                JSONObject temperature4 = temperature3.optJSONObject("parameter");
                String temperature5 = temperature4.optString("parameterName");
                if(weather.equals("無")){
                    Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

                    //Stop sound service to play sound for alarm
                    context.startService(new Intent(context, AlarmSoundService.class));

                    //This will send a notification message and show notification in notification tray
                    ComponentName comp = new ComponentName(context.getPackageName(),
                            AlarmNotificationService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                }
                else if((weather.equals("晴天")) && (climate5.equals("晴") || climate5.equals("晴天") || climate5.equals("晴時多雲") || climate5.equals("陰轉晴") || climate5.equals("多雲轉晴"))){
                    Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

                    //Stop sound service to play sound for alarm
                    context.startService(new Intent(context, AlarmSoundService.class));

                    //This will send a notification message and show notification in notification tray
                    ComponentName comp = new ComponentName(context.getPackageName(),
                            AlarmNotificationService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                }
                else if((weather.equals("雨天")) && (climate5.equals("多雲時陰短暫雨") || climate5.equals("雨天") )){
                    Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

                    //Stop sound service to play sound for alarm
                    context.startService(new Intent(context, AlarmSoundService.class));

                    //This will send a notification message and show notification in notification tray
                    ComponentName comp = new ComponentName(context.getPackageName(),
                            AlarmNotificationService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                }
                else if((weather.equals("陰天")) && (climate5.equals("多雲") || climate5.equals("陰時多雲") || climate5.equals("陰天") || climate5.equals("晴轉陰") || climate5.equals("晴轉多雲"))){
                    Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

                    //Stop sound service to play sound for alarm
                    context.startService(new Intent(context, AlarmSoundService.class));

                    //This will send a notification message and show notification in notification tray
                    ComponentName comp = new ComponentName(context.getPackageName(),
                            AlarmNotificationService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject error) {
                Toast.makeText(context,
                        "Error: " + statusCode + " " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                // Log error message
                Log.e("Hot Text:", statusCode + " " + e.getMessage());
                Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

                //Stop sound service to play sound for alarm
                context.startService(new Intent(context, AlarmSoundService.class));

                //This will send a notification message and show notification in notification tray
                ComponentName comp = new ComponentName(context.getPackageName(),
                        AlarmNotificationService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
            }

        });
    }


}
