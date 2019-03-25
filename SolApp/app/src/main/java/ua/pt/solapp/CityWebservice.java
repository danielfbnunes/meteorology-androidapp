package ua.pt.solapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CityWebservice {
    @GET("http://api.ipma.pt/open-data/forecast/meteorology/cities/daily/hp-daily-forecast-day{day}.json")
    Call<RawData> getRawData(@Path("day") String day);

    @GET("http://api.ipma.pt/open-data/weather-type-classe.json")
    Call<RawDataWeather> getRawDataWeather();

    @GET("http://api.ipma.pt/open-data/wind-speed-daily-classe.json")
    Call<RawDataWind> getRawDataWind();

    @GET("http://api.worldweatheronline.com/premium/v1/weather.ashx")
    Call<Example> getRawDataClimateAvg(@Query("q") String lat_long, @Query("key") String key, @Query("format") String format, @Query("num_of_days") String num_of_days, @Query("fx") String fx, @Query("cc") String cc, @Query("includelocation") String includelocation);
}
