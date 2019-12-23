package com.example.tourmate;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.adapter.ForcastRVAdapter;
import com.example.tourmate.currentweatherpojo.CurrentWeatherResponseBody;
import com.example.tourmate.forcastweatherpojo.ForcastList;
import com.example.tourmate.forcastweatherpojo.ForcastWeatherResponsBody;
import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.viewmodels.LocationViewModel;
import com.example.tourmate.viewmodels.WeatherViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private TextView addressTV, tempTV, dateTV, timeTV, hummidityTV, pressureTV,
            weatherstatus,sunriseTV,sunsetTV;
    private LocationViewModel locationViewModel;
    private WeatherViewModel weatherViewModel;
    private String unit = EventUtils.UNIT_CELCIUS;
    private String tempUnit = EventUtils.UNIT_CELCIUS_SYMBOL;

    private RecyclerView forcastRV;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Location currentlocation;
    private ImageView weatherIcon;
    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        locationViewModel =
                ViewModelProviders.of(getActivity())
                        .get(LocationViewModel.class);

        weatherViewModel =
                ViewModelProviders.of(getActivity())
                        .get(WeatherViewModel.class);


        locationViewModel.getDeviceCurrentLocation();
        isLocationPermissionGranted();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.weather_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_city).getActionView();
        searchView.setQueryHint("search by city");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    convertQueryToLatLng(query);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void convertQueryToLatLng(String query) throws IOException {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = geocoder.getFromLocationName(query, 1);
        if (addressList != null && addressList.size() > 0) {
            double lat = addressList.get(0).getLatitude();
            double lng = addressList.get(0).getLongitude();
            currentlocation.setLatitude(lat);
            currentlocation.setLongitude(lng);
            initializeCurrentWeather(currentlocation);
            initializeForcastWeather(currentlocation);
        } else {
            Toast.makeText(getActivity(), "Enter city name Properly!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_convertToC:
                unit = EventUtils.UNIT_CELCIUS;
                tempUnit = EventUtils.UNIT_CELCIUS_SYMBOL;
                initializeCurrentWeather(currentlocation);
                initializeForcastWeather(currentlocation);
                break;
            case R.id.menu_convertToF:
                unit = EventUtils.UNIT_FAHRENHEIT;
                tempUnit = EventUtils.UNIT_FAHRENHEIT_SYMBOL;
                initializeCurrentWeather(currentlocation);
                initializeForcastWeather(currentlocation);

                break;
          default:
              break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressTV = view.findViewById(R.id.addressID);
        tempTV = view.findViewById(R.id.teprature);
        forcastRV = view.findViewById(R.id.forcatRV);
        dateTV = view.findViewById(R.id.temp_date);
        timeTV = view.findViewById(R.id.temp_time);
        hummidityTV = view.findViewById(R.id.humidity);
        pressureTV = view.findViewById(R.id.pressure);
        weatherstatus = view.findViewById(R.id.weatherStatus);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        sunriseTV = view.findViewById(R.id.sunrise);
        sunsetTV = view.findViewById(R.id.sunset);



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                locationViewModel.getDeviceCurrentLocation();
                Toast.makeText(getActivity(), "on", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {

                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        /*resolvable.startResolutionForResult(getActivity(),
                                123);*/
                        startIntentSenderForResult(resolvable.getResolution().getIntentSender(),
                                123,null, 0, 0,
                                0, null);

                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });



        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        final ImageView buttonExpand = view.findViewById(R.id.button_expand);

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    buttonExpand.setImageResource(R.drawable.ic_arrow_upward_black_24dp);

                }
                else
                {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    buttonExpand.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }


            }
        });

        locationViewModel.locationLD.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                currentlocation = location;
                currentlocation = location;
                initializeCurrentWeather(location);
                initializeForcastWeather(location);
                try {
                    convertLatLngToStreetAddress(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void initializeForcastWeather(Location location) {

        String apikey = getString(R.string.forcastwether_api_key);

        weatherViewModel.getForcastWeather(location, unit, apikey).observe(this, new Observer<ForcastWeatherResponsBody>() {
            @Override
            public void onChanged(ForcastWeatherResponsBody responsBody) {
                List<ForcastList> forecastLists = responsBody.getList();

                ForcastRVAdapter forcastRVAdapter = new ForcastRVAdapter(getActivity(), forecastLists,tempUnit);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                forcastRV.setLayoutManager(llm);
                forcastRV.setAdapter(forcastRVAdapter);

            }
        });
    }


    private void initializeCurrentWeather(Location location) {
        String apikey = getString(R.string.wether_api_key);

        weatherViewModel.getCurrentWeather(location, unit, apikey).observe(this, new Observer<CurrentWeatherResponseBody>() {
            @Override
            public void onChanged(CurrentWeatherResponseBody responseBody) {

                double temp = responseBody.getMain().getTemp();
                String city = responseBody.getName();
                String date = EventUtils.getFormattedDate(responseBody.getDt());
                String time = EventUtils.getTime(responseBody.getDt());
                String sunset = EventUtils.getTime(responseBody.getSys().getSunrise()) ;
                String sunrise =EventUtils.getTime(responseBody.getSys().getSunset());
                int hudmmidity = responseBody.getMain().getHumidity();
                int pressure = responseBody.getMain().getPressure();
                String weatherStat = responseBody.getWeather().get(0).getDescription();

                String icon = responseBody.getWeather().get(0).getIcon();
                Picasso.get().load(EventUtils.WEATHER_CONDITION_ICON_PREFIX+icon+".png")
                        .into(weatherIcon);

                tempTV.setText((Math.round((temp))+EventUtils.DEGREE+tempUnit));
                dateTV.setText(date);
                timeTV.setText(time);
                addressTV.setText(city);
                hummidityTV.setText((hudmmidity) + " %");
                pressureTV.setText((pressure) + " hPa");
                weatherstatus.setText(weatherStat);
                sunriseTV.setText(sunrise);
                sunsetTV.setText(sunset);
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


    private boolean isLocationPermissionGranted() {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
            return false;
        }
        else {
            locationViewModel.getDeviceCurrentLocation();

        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
            locationViewModel.getDeviceCurrentLocation();

        }


        else {
            Toast.makeText(getActivity(), "You Can't see weather", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == 123){
            if (resultCode == Activity.RESULT_OK){
                Toast.makeText(getActivity(), "enabled", Toast.LENGTH_SHORT).show();
                locationViewModel.getLocationUpdate();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "resumed", Toast.LENGTH_SHORT).show();
        locationViewModel.getDeviceCurrentLocation();
    }
}
