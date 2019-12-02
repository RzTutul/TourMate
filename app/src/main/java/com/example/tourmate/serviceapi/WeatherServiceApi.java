package com.example.tourmate.serviceapi;

import com.example.tourmate.currentweatherpojo.CurrentWeatherResponseBody;
import com.example.tourmate.forcastweatherpojo.ForcastWeatherResponsBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceApi {
    @GET()
    Call<CurrentWeatherResponseBody> getCurrentWeather(@Url String endUrl);

    @GET
    Call<ForcastWeatherResponsBody> getForecastWeather(@Url String endUrl);

}
