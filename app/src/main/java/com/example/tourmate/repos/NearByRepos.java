package com.example.tourmate.repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.tourmate.helper.RetrofitClient;
import com.example.tourmate.nearby.NearByResponseBody;
import com.example.tourmate.serviceapi.NearByServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NearByRepos {
    private MutableLiveData<NearByResponseBody> nearbyLD = new MutableLiveData<>();

    public MutableLiveData<NearByResponseBody> getResponse(String endUrl)
    {
        NearByServiceApi serviceApi = RetrofitClient.getClientForNearbyPlace()
                .create(NearByServiceApi.class);

        serviceApi.getNearByResponse(endUrl).enqueue(new Callback<NearByResponseBody>() {
            @Override
            public void onResponse(Call<NearByResponseBody> call, Response<NearByResponseBody> response) {
                if (response.isSuccessful()){
                    nearbyLD.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<NearByResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
        return nearbyLD;
    }

}
