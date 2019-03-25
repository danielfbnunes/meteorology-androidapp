package ua.pt.solapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaName {

    @SerializedName("value")
    @Expose
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
