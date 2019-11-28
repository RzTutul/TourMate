package com.example.tourmate;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.viewmodels.LocationViewModel;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private TextView lognitudeLatitudeTV,addressTV;
    private LocationViewModel locationViewModel;
    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        locationViewModel =
                ViewModelProviders.of(getActivity())
                        .get(LocationViewModel.class);
        locationViewModel.getDeviceCurrentLocation();
        isLocationPermissionGranted();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lognitudeLatitudeTV = view.findViewById(R.id.lognitudeLatitudeId);
        addressTV = view.findViewById(R.id.addressID);
        locationViewModel.locationLD.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                lognitudeLatitudeTV.setText(location.getLatitude()+", "+
                        location.getLongitude());
                try {
                    convertLatLngToStreetAddress(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void convertLatLngToStreetAddress(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                location.getLongitude(), 1);
        String address = addressList.get(0).getAddressLine(0);
        addressTV.setText(address);

    }


    private boolean isLocationPermissionGranted(){
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            locationViewModel.getDeviceCurrentLocation();
        }
        else
        {
            Toast.makeText(getActivity(), "You Can't see weather", Toast.LENGTH_SHORT).show();
        }
    }


}
