package com.dreamwalker.sleeper.Model;

/**
 * Created by 2E313JCP on 2017-11-15.
 */

public class Home {

    String label;
    String data;
    String heartrate;
    String spo2;


    String sound;
    String temp;
    String humi;

    public Home(String label, String data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Home(String label, String heartrate, String spo2, String sound, String temp, String humi) {
        this.heartrate = heartrate;
        this.spo2 = spo2;
        this.sound = sound;
        this.temp = temp;
        this.humi = humi;
        this.label = label;
    }

    public String getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(String heartrate) {
        this.heartrate = heartrate;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumi() {
        return humi;
    }

    public void setHumi(String humi) {
        this.humi = humi;
    }
}
