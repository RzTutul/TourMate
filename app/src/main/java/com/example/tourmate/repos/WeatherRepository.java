package com.example.tourmate.repos;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.example.tourmate.currentweatherpojo.CurrentWeatherResponseBody;
import com.example.tourmate.forcastweatherpojo.ForcastWeatherResponsBody;
import com.example.tourmate.helper.RetrofitClient;
import com.example.tourmate.serviceapi.WeatherServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherRepository {
    public MutableLiveData<CurrentWeatherResponseBody> currentResponseLD =
            new MutableLiveData<>();
    public MutableLiveData<ForcastWeatherResponsBody> forecastResponseLD =
            new MutableLiveData<>();

    public MutableLiveData<CurrentWeatherResponseBody> getCurrentWeatherFromRepos(Location location, String unit, String apiKey)
    {
        WeatherServiceApi serviceApi = RetrofitClient.getClient()
                        .create(WeatherServiceApi.class);

        String endUrl = String.format("data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                location.getLatitude(),
                location.getLongitude(),
                unit, apiKey);

        serviceApi.getCurrentWeather(endUrl)
                .enqueue(new Callback<CurrentWeatherResponseBody>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponseBody> call, Response<CurrentWeatherResponseBody> response) {
                        if (response.isSuccessful()){
                            CurrentWeatherResponseBody responseBody = response.body();
                            currentResponseLD.postValue(responseBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponseBody> call, Throwable t) {

                    }
                });
        return currentResponseLD;



    }



    public MutableLiveData<ForcastWeatherResponsBody> getForcastweather(Location location,String unit,String apikey)
    {
        WeatherServiceApi serviceApi =
                RetrofitClient.getClient()
                        .create(WeatherServiceApi.class);

        String endUrl = String.format("data/2.5/forecast/daily?lat=%f&lon=%f&cnt=7&units=%s&appid=%s",
                location.getLatitude(),
                location.getLongitude(),
                unit, apikey);

        serviceApi.getForecastWeather(endUrl).enqueue(new Callback<ForcastWeatherResponsBody>() {
            @Override
            public void onResponse(Call<ForcastWeatherResponsBody> call, Response<ForcastWeatherResponsBody> response) {
                if (response.isSuccessful()){
                    forecastResponseLD.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ForcastWeatherResponsBody> call, Throwable t) {

            }
        });


        return forecastResponseLD;
    }
}
