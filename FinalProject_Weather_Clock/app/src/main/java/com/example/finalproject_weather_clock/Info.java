package com.example.finalproject_weather_clock;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by 黃筠珊 on 2017/12/24.
 */

public class Info implements Serializable {
    //MEMBER ATTRIBUTES
    private String NUMBER;
    private String ON_OFF;
    private String TIME;
    private String HOUR;
    private String MINUTE;
    private String WEATHER;

    public Info() {
    }

    public Info(String number, String on_off, String time, String hour, String minute, String weather) {
        NUMBER = number;
        ON_OFF = on_off;
        TIME = time;
        HOUR = hour;
        MINUTE = minute;
        WEATHER = weather;
    }

    public String getnumber () { return NUMBER; }
    public void setnumber (String number) { NUMBER = number; }

    public String geton_off () { return ON_OFF; }
    public void seton_off (String on_off) { ON_OFF = on_off; }

    public String gettime () { return TIME; }
    public void settime (String time) { TIME = time; }

    public String gethour () {
        return HOUR;
    }
    public void sethour (String hour) {
        HOUR = hour;
    }

    public String getmin () {
        return MINUTE;
    }
    public void setmin (String minute) {
        MINUTE = minute;
    }

    public String getweather() { return WEATHER; }
    public void setweather (String weather) { WEATHER = weather; }

    @Override
    public String toString() {
        String text = NUMBER.toString() + "  " + ON_OFF.toString() + "  " + TIME.toString() + "  " + HOUR.toString() + "  "  + MINUTE.toString() + "  "  + WEATHER.toString()
                /*+ Float.toString(BMI) + Float.toString(IDOL) + RESULT.toString() +IDOLRESULT.toString() +RECOMMAND.toString()*/;
        return text;
    }
}
