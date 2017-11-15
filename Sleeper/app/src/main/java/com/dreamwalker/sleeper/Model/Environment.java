package com.dreamwalker.sleeper.Model;

/**
 * Created by 2E313JCP on 2017-10-26.
 */

public class Environment {

    int envID;
    String temp;
    String humi;
    String gas;
    String fire;
    String dust;
    String envDate;
    String envTime;

    public Environment(String temp, String humi, String gas, String fire, String dust, String envDate, String envTime) {
        this.temp = temp;
        this.humi = humi;
        this.gas = gas;
        this.fire = fire;
        this.dust = dust;
        this.envDate = envDate;
        this.envTime = envTime;
    }

    public Environment(int envID, String temp, String humi, String gas, String fire, String dust, String envDate, String envTime) {
        this.envID = envID;
        this.temp = temp;
        this.humi = humi;
        this.gas = gas;
        this.fire = fire;
        this.dust = dust;
        this.envDate = envDate;
        this.envTime = envTime;
    }

    public int getEnvID() {
        return envID;
    }

    public void setEnvID(int envID) {
        this.envID = envID;
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

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getFire() {
        return fire;
    }

    public void setFire(String fire) {
        this.fire = fire;
    }

    public String getDust() {
        return dust;
    }

    public void setDust(String dust) {
        this.dust = dust;
    }

    public String getEnvDate() {
        return envDate;
    }

    public void setEnvDate(String envDate) {
        this.envDate = envDate;
    }

    public String getEnvTime() {
        return envTime;
    }

    public void setEnvTime(String envTime) {
        this.envTime = envTime;
    }
}
