package ua.pt.solapp;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearestArea {

    @Override
    public String toString() {
        return "NearestArea{" +
                "areaName=" + areaName +
                ", country=" + country +
                ", region=" + region +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", population='" + population + '\'' +
                ", weatherUrl=" + weatherUrl +
                '}';
    }

    @SerializedName("areaName")
    @Expose
    private List<AreaName> areaName = null;
    @SerializedName("country")
    @Expose
    private List<Country> country = null;
    @SerializedName("region")
    @Expose
    private List<Region> region = null;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("population")
    @Expose
    private String population;
    @SerializedName("weatherUrl")
    @Expose
    private List<WeatherUrl> weatherUrl = null;

    public List<AreaName> getAreaName() {
        return areaName;
    }

    public void setAreaName(List<AreaName> areaName) {
        this.areaName = areaName;
    }

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public List<Region> getRegion() {
        return region;
    }

    public void setRegion(List<Region> region) {
        this.region = region;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public List<WeatherUrl> getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(List<WeatherUrl> weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

}
