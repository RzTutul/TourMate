package com.example.tourmate.viewmodels;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.currentweatherpojo.CurrentWeatherResponseBody;
import com.example.tourmate.forcastweatherpojo.ForcastWeatherResponsBody;
import com.example.tourmate.repos.WeatherRepository;

public class WeatherViewModel extends ViewModel {

    private WeatherRepository weatherRepository;

    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();

    }
    public MutableLiveData<CurrentWeatherResponseBody> getCurrentWeather(Location location,String unit, String apikey)

    {
       return weatherRepository.getCurrentWeatherFromRepos(location,unit,apikey);

    }

        public  MutableLiveData<ForcastWeatherResponsBody> getForcastWeather(Location location,String unit, String apikey)

    {
       return weatherRepository.getForcastweather(location,unit,apikey);

    }



}
