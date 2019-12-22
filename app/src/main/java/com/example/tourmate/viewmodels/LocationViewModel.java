package com.example.tourmate.viewmodels;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationViewModel extends AndroidViewModel {

    private FusedLocationProviderClient providerClient;
    private Context context;
    public MutableLiveData<Location> locationLD = new MutableLiveData<>();

    public LocationViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        providerClient = LocationServices.getFusedLocationProviderClient(context);

    }
    public void getDeviceCurrentLocation(){
        providerClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null){
                            return;
                        }
                        locationLD.postValue(location);
                    }
                });
    }


    public MutableLiveData<Location> getLocationUpdate(){
        LocationCallback callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()){
                    if (location != null){
                        locationLD.postValue(location);
                    }
                }
            }
        };

        LocationRequest request = LocationRequest.create();
        request.setInterval(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000L);
        request.setFastestInterval(2000L);
        request.setNumUpdates(1);

        providerClient.requestLocationUpdates(request, callback, null);
        return locationLD;
    }
}
