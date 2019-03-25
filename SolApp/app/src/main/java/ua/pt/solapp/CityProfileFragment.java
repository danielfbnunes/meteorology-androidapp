package ua.pt.solapp;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class CityProfileFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CityProfileViewModel viewModel;


    @BindView(R.id.fragment_city_profile_description) TextView description;
    @BindView(R.id.fragment_city_profile_name) TextView name;
    @BindView(R.id.fragment_city_profile_tMin) TextView tMin;
    @BindView(R.id.fragment_city_profile_tMax) TextView tMax;
    @BindView(R.id.fragment_city_profile_wind) TextView wind;
    @BindView(R.id.fragment_city_profile_rain) TextView rain;
    @BindView(R.id.show_averages) Button btn_averages;
    @BindView(R.id.fragment_city_profile_imageView) ImageView imageView;
    @BindView(R.id.last_time_updated) TextView last_update;

    public CityProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel();

        if (MainActivity.markers == null) {
            MainActivity.markers = getMarkers();
            MainActivity.weather_types = getWeatherType();
            MainActivity.wind_types = getWindType();
        }

        if (!MainActivity.markers.isEmpty() && MainActivity.averages == null) {
            Map<String, Map<String, String>> temp = new HashMap<>();
            for (String s : MainActivity.markers.keySet()) {
                temp.put(s, getAverages("" + MainActivity.markers.get(s)[0], "" + MainActivity.markers.get(s)[1]));
            }
            MainActivity.averages = temp;
        }

        if(!MainActivity.markers.isEmpty() && MainActivity.markers.size() == 24 && MainActivity.FIRST_FETCH){
            for (String s : MainActivity.markers.keySet()){
                saveCities(s, "0");
                saveCities(s, "1");
                saveCities(s, "2");
            }
            MainActivity.FIRST_FETCH = false;
        }

    }


    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CityProfileViewModel.class);
        viewModel.init(this.getArguments().getString("city"));
        viewModel.getCity(this.getArguments().getString("day")).observe(this, city -> updateUI(city));
    }

    public void saveCities(String city, String day){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CityProfileViewModel.class);
        viewModel.saveCity(city, day);
    }

    public Map<String, Double[]> getMarkers(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CityProfileViewModel.class);
        return viewModel.getMarkers();
    }

    public Map<Integer, String> getWeatherType(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CityProfileViewModel.class);
        return viewModel.getWeatherType();
    }

    public Map<Integer, String> getWindType(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CityProfileViewModel.class);
        return viewModel.getWindType();
    }

    public Map<String, String> getAverages(String latitude, String longitude){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CityProfileViewModel.class);
        return viewModel.getAverages(latitude, longitude);
    }


    private void updateUI(@Nullable City city){
        if (city != null){
            getActivity().setTitle(""+city.getName());
            this.description.setText(MainActivity.weather_types.get(city.getIdWeatherType()));
            this.name.setText(""+city.getName());
            this.tMin.setText(""+city.getTMin() + " ºC");
            this.tMax.setText(""+city.getTMax() + " ºC");
            this.wind.setText(MainActivity.wind_types.get(city.getClassWindSpeed()) + " (" + city.getPredWindDir() + ")");
            this.rain.setText(""+city.getPrecipitaProb() + " %");
            String url;
            if (isNetworkAvailable()){
                //light rain
                if (city.getIdWeatherType() == 6 || city.getIdWeatherType() == 7 || city.getIdWeatherType() == 9 || city.getIdWeatherType() == 10 || city.getIdWeatherType() == 12 || city.getIdWeatherType() == 13 || city.getIdWeatherType() == 15) url = "https://cdn2.iconfinder.com/data/icons/weather-and-forecast-free/32/Weather_Weather_Forecast_Cloud_Snowing_Cloud_Climate-512.png";
                    //heavy rain
                else if (city.getIdWeatherType() == 8 || city.getIdWeatherType() == 11 || city.getIdWeatherType() == 14) url = "https://cdn2.iconfinder.com/data/icons/weather-and-forecast-free/32/Weather_Weather_Forecast_Heavy_Rain_Cloud_Climate-512.png";
                    //sunny
                else if (city.getIdWeatherType() == 1 ) url = "https://cdn2.iconfinder.com/data/icons/weather-and-forecast-free/32/Weather_Weather_Forecast_Hot_Sun_Day-512.png";
                    //clouds
                else if (city.getIdWeatherType() == 4 || city.getIdWeatherType() == 5 || city.getIdWeatherType() == 24 || city.getIdWeatherType() == 25 || city.getIdWeatherType() == 27) url = "https://cdn2.iconfinder.com/data/icons/weather-and-forecast-free/32/Weather_Weather_Forecast_Cloudy_Season_Cloud-512.png";
                    //partly cloudy
                else if (city.getIdWeatherType() == 2 || city.getIdWeatherType() == 3) url = "https://cdn2.iconfinder.com/data/icons/weather-and-forecast-free/32/Weather_Weather_Forecast_Sunny_Sun_Cloudy-512.png";
                    //mist
                else if (city.getIdWeatherType() == 16 || city.getIdWeatherType() == 17 || city.getIdWeatherType() == 26) url = "https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_49-512.png";
                    //snow
                else if (city.getIdWeatherType() == 18 || city.getIdWeatherType() == 21 || city.getIdWeatherType() == 22) url = "https://cdn2.iconfinder.com/data/icons/weather-and-forecast-free/32/Weather_Weather_Forecast_Flake_Flakes_Snowflake-512.png";
                    //storm
                else if (city.getIdWeatherType() == 19 || city.getIdWeatherType() == 20 || city.getIdWeatherType() == 23) url = "https://cdn2.iconfinder.com/data/icons/weather-and-forecast-free/32/Weather_Weather_Forecast_Lightning_Cloud_Storm-512.png";
                else url = "https://cdn4.iconfinder.com/data/icons/evil-icons-user-interface/64/close2-512.png";
                Glide.with(this).load(url).into(imageView);
            }else{
                //light rain
                if (city.getIdWeatherType() == 6 || city.getIdWeatherType() == 7 || city.getIdWeatherType() == 9 || city.getIdWeatherType() == 10 || city.getIdWeatherType() == 12 || city.getIdWeatherType() == 13 || city.getIdWeatherType() == 15) imageView.setImageResource(R.drawable.light_rain);
                    //heavy rain
                else if (city.getIdWeatherType() == 8 || city.getIdWeatherType() == 11 || city.getIdWeatherType() == 14) imageView.setImageResource(R.drawable.heavy_rain);
                    //sunny
                else if (city.getIdWeatherType() == 1 ) imageView.setImageResource(R.drawable.sunny);
                    //clouds
                else if (city.getIdWeatherType() == 4 || city.getIdWeatherType() == 5 || city.getIdWeatherType() == 24 || city.getIdWeatherType() == 25 || city.getIdWeatherType() == 27) imageView.setImageResource(R.drawable.clouds);
                    //partly cloudy
                else if (city.getIdWeatherType() == 2 || city.getIdWeatherType() == 3) imageView.setImageResource(R.drawable.partly_cloudy);
                    //mist
                else if (city.getIdWeatherType() == 16 || city.getIdWeatherType() == 17 || city.getIdWeatherType() == 26) imageView.setImageResource(R.drawable.mist);
                    //snow
                else if (city.getIdWeatherType() == 18 || city.getIdWeatherType() == 21 || city.getIdWeatherType() == 22) imageView.setImageResource(R.drawable.snow);
                    //storm
                else if (city.getIdWeatherType() == 19 || city.getIdWeatherType() == 20 || city.getIdWeatherType() == 23) imageView.setImageResource(R.drawable.storm);
                else imageView.setImageResource(R.drawable.no_info);
            }

            this.last_update.setText("Last update: "+city.getLastRefresh().toString());
            if(MainActivity.averages != null) {
                btn_averages.setEnabled(true);
                btn_averages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNetworkAvailable()){
                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("AVERAGES (" + MainActivity.averages.get(city.getName()).get("month").toUpperCase() + ")").
                                        setMessage("\nInfo from: " + MainActivity.averages.get(city.getName()).get("avg_area") + " (" + MainActivity.averages.get(city.getName()).get("avg_region") + ")\n\n" +
                                                "Average min temperature: " + MainActivity.averages.get(city.getName()).get("avg_min_temp") + " ºC\n\n" +
                                                "Average max temperature: " + MainActivity.averages.get(city.getName()).get("avg_max_temp") + " ºC\n\n" +
                                                "Average day rainfall: " + MainActivity.averages.get(city.getName()).get("avg_day_rainfall") + " mm\n");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }catch (Exception e){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("ERROR !").
                                        setMessage("\nCouldn't fetch data, try again please\n");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("ERROR !").
                                    setMessage("\nPlease connect to the Internet to access this feature\n");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    }
                });
            }else{
                btn_averages.setEnabled(false);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}