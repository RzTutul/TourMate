package com.example.tourmate.viewmodels;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.currentweatherpojo.CurrentWeatherResponseBody;
import com.example.tourmate.forcastweatherpojo.ForcastWeatherResponsBody;
import com.example.tourmate.repos.WeatherRepository;

public class WeatherViewModel extends ViewModel {
    public MutableLiveData<CurrentWeatherResponseBody> currentWeatherLD = new MutableLiveData<>();
    public MutableLiveData<ForcastWeatherResponsBody> forcastWeatherLD = new MutableLiveData<>();
    private WeatherRepository weatherRepository;

    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();

    }
    public void getCurrentWeather(Location location,String unit, String apikey)

    {
       currentWeatherLD = weatherRepository.getCurrentWeatherFromRepos(location,unit,apikey);

    }

        public void getForcastWeather(Location location,String unit, String apikey)

    {
       forcastWeatherLD = weatherRepository.getForcastweather(location,unit,apikey);

    }



}
