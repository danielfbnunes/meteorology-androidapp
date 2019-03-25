package ua.pt.solapp;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RawDataClimateAvg {

    @Override
    public String toString() {
        return "RawDataClimateAvg{" +
                "request=" + request +
                ", nearestArea=" + nearestArea +
                ", climateAverages=" + climateAverages +
                '}';
    }

    @SerializedName("request")
    @Expose
    private List<Request> request = null;
    @SerializedName("nearest_area")
    @Expose
    private List<NearestArea> nearestArea = null;
    @SerializedName("ClimateAverages")
    @Expose
    private List<ClimateAverage> climateAverages = null;

    public List<Request> getRequest() {
        return request;
    }

    public void setRequest(List<Request> request) {
        this.request = request;
    }

    public List<NearestArea> getNearestArea() {
        return nearestArea;
    }

    public void setNearestArea(List<NearestArea> nearestArea) {
        this.nearestArea = nearestArea;
    }

    public List<ClimateAverage> getClimateAverages() {
        return climateAverages;
    }

    public void setClimateAverages(List<ClimateAverage> climateAverages) {
        this.climateAverages = climateAverages;
    }
}

