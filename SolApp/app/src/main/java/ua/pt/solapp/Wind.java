package ua.pt.solapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("descClassWindSpeedDailyEN")
    @Expose
    private String descClassWindSpeedDailyEN;
    @SerializedName("descClassWindSpeedDailyPT")
    @Expose
    private String descClassWindSpeedDailyPT;
    @SerializedName("classWindSpeed")
    @Expose
    private String classWindSpeed;

    public String getDescClassWindSpeedDailyEN() {
        return descClassWindSpeedDailyEN;
    }

    public void setDescClassWindSpeedDailyEN(String descClassWindSpeedDailyEN) {
        this.descClassWindSpeedDailyEN = descClassWindSpeedDailyEN;
    }

    public String getDescClassWindSpeedDailyPT() {
        return descClassWindSpeedDailyPT;
    }

    public void setDescClassWindSpeedDailyPT(String descClassWindSpeedDailyPT) {
        this.descClassWindSpeedDailyPT = descClassWindSpeedDailyPT;
    }

    public String getClassWindSpeed() {
        return classWindSpeed;
    }

    public void setClassWindSpeed(String classWindSpeed) {
        this.classWindSpeed = classWindSpeed;
    }

}