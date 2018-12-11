package com.example.finalproject_weather_clock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class Main2Activity extends AppCompatActivity {

    //Pending intent instance
    private PendingIntent pendingIntent;

    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;

    private String[] arraySpinner,arraySpinner2,arraySpinner3,arraySpinner4;
    private ArrayAdapter adapter,adapter2,adapter3,adapter4;
    private Spinner spinner,spinner2,spinner3,spinner4;
    private String time = "上午",hour = "0",minute = "0",weather = "晴天";
    private Button check,cancel;

    getcontext context= new getcontext();
    private DBHelper database = new DBHelper(context.getAppContext());

    private AlarmManager am;
    PendingIntent pi;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        this.arraySpinner = new String[] {
                "上午","下午"
        };
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                time = arraySpinner[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        this.arraySpinner2 = new String[] {
                "0","1","2","3","4","5","6","7","8","9","10","11"/*,"12"*/
        };
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner2);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                hour = arraySpinner2[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        this.arraySpinner3 = new String[] {
                "0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"
        };
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner3);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                minute = arraySpinner3[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        this.arraySpinner4 = new String[] {
                "晴天","雨天","陰天","無"
        };
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner4);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                weather = arraySpinner4[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        check = (Button)findViewById(R.id.button);
        cancel = (Button)findViewById(R.id.button2);
        check.setOnClickListener(check_btn);
        cancel.setOnClickListener(cancel_btn);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(Main2Activity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(Main2Activity.this, ALARM_REQUEST_CODE, alarmIntent, 0);

    }

    private final View.OnClickListener check_btn = new View.OnClickListener() {
        @Override
        public void onClick(View btn) {
            int now = database.getAllTaskItems().size();
            database.addToDoItem(new Info(String.valueOf(now+1),"on",time.toString(),hour.toString(),minute.toString(),weather.toString()));

            // get a Calendar object with current time
            Calendar cal = Calendar.getInstance();
            int total = 122416000;
            int now_time = (cal.get(Calendar.HOUR_OF_DAY) * 60 * 60) + (cal.get(Calendar.MINUTE) * 60) + cal.get(Calendar.SECOND);
            int time_set;
            if(time.equals("上午")){
                time_set = (Integer.parseInt(hour) * 60 * 60) + (Integer.parseInt(minute) * 60);
            }
            else{
                time_set = ((Integer.parseInt(hour) + 12) * 60 * 60) + (Integer.parseInt(minute) * 60);
            }
            if(now_time >= time_set){
                time_set = total - now_time + time_set;
            }
            else{
                time_set -= now_time;
            }
            cal.add(Calendar.SECOND, time_set);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
            manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

            //Toast.makeText(Main2Activity.this, "Alarm Set for " + String.valueOf(time_set)  + " seconds.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(Main2Activity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            Main2Activity.this.finish();
        }
    };

    private final View.OnClickListener cancel_btn = new View.OnClickListener() {
        @Override
        public void onClick(View btn) {
            Intent intent = new Intent();
            intent.setClass(Main2Activity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            Main2Activity.this.finish();
        }
    };

}
