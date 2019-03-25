package ua.pt.solapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("data")
    @Expose
    private RawDataClimateAvg data;

    public RawDataClimateAvg getData() {
        return data;
    }

    public void setData(RawDataClimateAvg data) {
        this.data = data;
    }

}
