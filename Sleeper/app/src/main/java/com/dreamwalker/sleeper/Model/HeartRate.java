package com.dreamwalker.sleeper.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2E313JCP on 2017-11-16.
 */

public class HeartRate implements Parcelable {

    String heartrate;
    String minHeartRate;
    String maxHeartRate;
    String avgHeartRate;
    String date;
    String time;

    public HeartRate(String minHeartRate, String maxHeartRate, String avgHeartRate, String date) {
        this.minHeartRate = minHeartRate;
        this.maxHeartRate = maxHeartRate;
        this.avgHeartRate = avgHeartRate;
        this.date = date;
    }

    public HeartRate(String heartrate, String date, String time) {
        this.heartrate = heartrate;
        this.date = date;
        this.time = time;
    }

    // TODO: 2017-11-16 액티비티간 데이터를 전달하기 위한
    public HeartRate(Parcel in){
        this.heartrate = in.readString();
        this.date = in.readString();
        this.time = in.readString();
    }

    public String getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(String heartrate) {
        this.heartrate = heartrate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(String minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public String getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(String maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public String getAvgHeartRate() {
        return avgHeartRate;
    }

    public void setAvgHeartRate(String avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    // TODO: 2017-11-16 액티비티간 데이터를 전달하기 위한
    @Override
    public int describeContents() {
        return 0;
    }

    // TODO: 2017-11-16 액티비티간 데이터를 전달하기 위한
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.heartrate);
        dest.writeString(this.date);
        dest.writeString(this.time);
    }

    // TODO: 2017-11-16 액티비티간 데이터를 전달하기 위한
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public HeartRate createFromParcel(Parcel in) {
            return new HeartRate(in);
        }

        @Override
        public HeartRate[] newArray(int size) {
            // TODO Auto-generated method stub
            return new HeartRate[size];
        }
    };
}
