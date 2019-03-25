package ua.pt.solapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CityDao {

    @Insert(onConflict = REPLACE)
    void save(City city);

    @Query("SELECT * FROM city WHERE day = '0'")
    List<City> getDistinctCities();

    @Query("SELECT * FROM city")
    List<City> getAllCities();

    @Query("SELECT * FROM city WHERE name = :city_name AND day = :day")
    LiveData<City> load(String city_name, String day);

    @Query("SELECT * FROM city WHERE name = :city_name AND day = :day AND lastRefresh > :lastRefreshMax LIMIT 1")
    City hasCity(String city_name, String day, Date lastRefreshMax);


}
