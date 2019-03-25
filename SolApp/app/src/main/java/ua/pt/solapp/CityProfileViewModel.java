package ua.pt.solapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Map;

import javax.inject.Inject;

public class CityProfileViewModel extends ViewModel {

    private LiveData<City> city_day_one;
    private LiveData<City> city_day_two;
    private LiveData<City> city_day_three;

    private CityRepository cityRepo;

    @Inject
    public CityProfileViewModel(CityRepository cityRepo) {
        this.cityRepo = cityRepo;
    }


    public void init(String city_name) {
        if (this.city_day_one != null && this.city_day_two != null && this.city_day_three != null) {
            return;
        }
        this.city_day_one = cityRepo.getCity(city_name, "0");
        this.city_day_two = cityRepo.getCity(city_name, "1");
        this.city_day_three = cityRepo.getCity(city_name, "2");
    }

    public LiveData<City> getCity(String day) {
        switch (day){
            case "0":
                return this.city_day_one;
            case "1":
                return this.city_day_two;
            case "2":
                return this.city_day_three;
        }
        return null;
    }

    public void saveCity(String name, String day){ cityRepo.getCity(name, day);}

    public Map<String, Double[]> getMarkers(){
        return cityRepo.getMarkers();
    }

    public Map<Integer, String> getWeatherType(){
        return cityRepo.getWeatherType();
    }

    public Map<Integer, String> getWindType(){
        return cityRepo.getWindType();
    }

    public Map<String, String> getAverages(String latitude, String longitude){
        return cityRepo.getAverages(latitude, longitude);
    }

}