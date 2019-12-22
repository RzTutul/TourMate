package com.example.tourmate.serviceapi;

import com.example.tourmate.currentweatherpojo.CurrentWeatherResponseBody;
import com.example.tourmate.nearby.NearByResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearByServiceApi {

    @GET()
    Call<NearByResponseBody> getNearByResponse(@Url String endUrl);

}
