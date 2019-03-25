package ua.pt.solapp;

import android.arch.lifecycle.LiveData;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CityRepository {

    private static int FRESH_TIMEOUT_IN_MINUTES = 1;
    private final CityWebservice webservice;
    private final CityDao cityDao;
    private final Executor executor;

    @Inject
    public CityRepository(CityWebservice webservice, CityDao cityDao, Executor executor) {
        this.webservice = webservice;
        this.cityDao = cityDao;
        this.executor = executor;

    }


    public LiveData<City> getCity(String city_name, String day) {
        refreshCity(city_name, day); // try to refresh data if possible from Github Api
        return cityDao.load(city_name, day); // return a LiveData directly from the database.
    }


    public Map<String, String> getAverages(String latitude, String longitude){
        String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};

        Calendar cal = Calendar.getInstance();
        String month = monthName[cal.get(Calendar.MONTH)];
        Map<String, String> temp = new HashMap<>();
        executor.execute(()-> {
            webservice.getRawDataClimateAvg(latitude + "," + longitude, "0b96f6d33502445ab5a01137182812", "json", "1", "no", "no", "yes").enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    executor.execute(() -> {
                        Example data = response.body();
                        try {
                            DecimalFormat df = new DecimalFormat("#.#");
                            temp.put("avg_area", data.getData().getNearestArea().get(0).getAreaName().get(0).getValue());
                            temp.put("avg_region", data.getData().getNearestArea().get(0).getRegion().get(0).getValue());
                            for (Month m : data.getData().getClimateAverages().get(0).getMonth()){
                                if (m.getName().equals(month)){
                                    temp.put("month", month);
                                    temp.put("avg_min_temp", ""+(df.format(Double.parseDouble(m.getAvgMinTemp()))));
                                    temp.put("avg_max_temp", ""+(df.format(Double.parseDouble(m.getAbsMaxTemp()))));
                                    temp.put("avg_day_rainfall", m.getAvgDailyRainfall());
                                }
                            }
                        }catch (Exception e){}

                    });
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {

                }
            });
        });

        return temp;
    }

    private void refreshCity(final String city_name, final String day) {
        executor.execute(() -> {
            boolean cityExists = (cityDao.hasCity(city_name, day, getMaxRefreshTime(new Date())) != null);
            if (!cityExists) {
                System.out.println("DATA REFRESHED");
                webservice.getRawData(day).enqueue(new Callback<RawData>() {
                    @Override
                    public void onResponse(Call<RawData> call, Response<RawData> response) {
                        executor.execute(() -> {
                            RawData data = response.body();
                            for (City city :
                                    data.getData()) {

                                Geocoder gcd = new Geocoder(App.context, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = gcd.getFromLocation(Double.parseDouble(city.getLatitude()), Double.parseDouble(city.getLongitude()), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (addresses.get(0).getLocality() != null && city_name.equals(""+addresses.get(0).getLocality())) {
                                    city.setName(city_name);
                                    city.setLastRefresh(new Date());
                                    city.setDay(day);
                                    city.setWeather(MainActivity.weather_types.get(city.getIdWeatherType()));
                                    city.setWind(MainActivity.wind_types.get(city.getClassWindSpeed()));
                                    cityDao.save(city);
                                }
                            }

                        });
                    }

                    @Override
                    public void onFailure(Call<RawData> call, Throwable t) { }
                });

            }
        });
    }


    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    public Map<String, Double[]> getMarkers() {
        Map<String, Double[]> temp = new HashMap<>();
        executor.execute(() -> {
            webservice.getRawData("0").enqueue(new Callback<RawData>() {
                @Override
                public void onResponse(Call<RawData> call, Response<RawData> response) {
                    executor.execute(() -> {
                        RawData data = response.body();
                        for (City city :
                                data.getData()) {

                            Geocoder gcd = new Geocoder(App.context, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(Double.parseDouble(city.getLatitude()), Double.parseDouble(city.getLongitude()), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addresses.get(0).getLocality() != null) {
                                temp.put(""+addresses.get(0).getLocality(), new Double[]{Double.parseDouble(city.getLatitude()), Double.parseDouble(city.getLongitude())});
                            }
                        }

                    });
                }

                @Override
                public void onFailure(Call<RawData> call, Throwable t) {
                    executor.execute(() -> {
                        for (City city : cityDao.getDistinctCities()){
                            temp.put(city.getName(), new Double[]{Double.parseDouble(city.getLatitude()), Double.parseDouble(city.getLongitude())});
                        }
                    });
                }
            });

        });
        return temp;
    }

    public Map<Integer, String> getWeatherType() {
        Map<Integer, String> temp = new HashMap<>();
        executor.execute(() -> {
            webservice.getRawDataWeather().enqueue(new Callback<RawDataWeather>() {
                @Override
                public void onResponse(Call<RawDataWeather> call, Response<RawDataWeather> response) {
                    executor.execute(() -> {
                        RawDataWeather data = response.body();
                        for (WeatherType weather :
                                data.getData()) {

                            temp.put(weather.getIdWeatherType(), weather.getDescIdWeatherTypeEN());
                        }

                    });
                }

                @Override
                public void onFailure(Call<RawDataWeather> call, Throwable t) {
                    executor.execute(() -> {
                        for (City city : cityDao.getAllCities()){
                            temp.put(city.getIdWeatherType(), city.getWeather());
                        }
                    });
                }
            });

        });
        return temp;
    }

    public Map<Integer, String> getWindType() {
        Map<Integer, String> temp = new HashMap<>();
        executor.execute(() -> {
            webservice.getRawDataWind().enqueue(new Callback<RawDataWind>() {
                @Override
                public void onResponse(Call<RawDataWind> call, Response<RawDataWind> response) {
                    executor.execute(() -> {
                        RawDataWind data = response.body();
                        for (Wind wind :
                                data.getData()) {
                            temp.put(Integer.parseInt(wind.getClassWindSpeed()), wind.getDescClassWindSpeedDailyEN());
                        }

                    });
                }

                @Override
                public void onFailure(Call<RawDataWind> call, Throwable t) {
                    executor.execute(() -> {
                        for (City city : cityDao.getAllCities()){
                            temp.put(city.getClassWindSpeed(), city.getWind());
                        }
                    });
                }
            });

        });
        return temp;
    }
}