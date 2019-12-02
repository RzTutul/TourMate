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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.adapter.ForcastRVAdapter;
import com.example.tourmate.currentweatherpojo.CurrentWeatherResponseBody;
import com.example.tourmate.forcastweatherpojo.ForcastList;
import com.example.tourmate.forcastweatherpojo.ForcastWeatherResponsBody;
import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.viewmodels.LocationViewModel;
import com.example.tourmate.viewmodels.WeatherViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private TextView lognitudeLatitudeTV,addressTV,tempTV;
    private LocationViewModel locationViewModel;
    private WeatherViewModel weatherViewModel;
    private String unit = "metric";
    private RecyclerView forcastRV;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Location currentlocation;

    public WeatherFragment() {
        // Required empty public constructor
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lognitudeLatitudeTV = view.findViewById(R.id.lognitudeLatitudeId);
        addressTV = view.findViewById(R.id.addressID);
        tempTV = view.findViewById(R.id.teprature);
        forcastRV = view.findViewById(R.id.forcatRV);

        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        Button buttonExpand = view.findViewById(R.id.button_expand);
        Button buttonCollapse = view.findViewById(R.id.button_collapse);

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        buttonCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                      //  mTextViewState.setText("Collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                      ///  mTextViewState.setText("Dragging...");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                      ///  mTextViewState.setText("Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                      ///  mTextViewState.setText("Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                      ///  mTextViewState.setText("Settling...");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
             //   mTextViewState.setText("Sliding...");
            }
        });


        locationViewModel.locationLD.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                currentlocation = location;
                lognitudeLatitudeTV.setText(location.getLatitude()+", "+
                        location.getLongitude());

                initializeCurrentWeather(location);
                initializeForcastWeather(location);



                try {
                    convertLatLngToStreetAddress(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        weatherViewModel.currentWeatherLD.observe(this, new Observer<CurrentWeatherResponseBody>() {
            @Override
            public void onChanged(CurrentWeatherResponseBody responseBody) {


                double temp = responseBody.getMain().getTemp();
                String city = responseBody.getName();
                String date = EventUtils.getFormattedDate(responseBody.getDt());
                tempTV.setText(String.valueOf(temp)+"°C"+" \n"+city+"\n"+date);
            }
        });

        weatherViewModel.forcastWeatherLD.observe(this, new Observer<ForcastWeatherResponsBody>() {
            @Override
            public void onChanged(ForcastWeatherResponsBody responsBody) {
                List<ForcastList> forecastLists = responsBody.getList();

                Log.i(TAG, "onChanged: "+forecastLists.size());
                ForcastRVAdapter forcastRVAdapter = new ForcastRVAdapter(getActivity(),forecastLists);
                LinearLayoutManager llm  = new LinearLayoutManager(getActivity());
                forcastRV.setLayoutManager(llm);
                forcastRV.setAdapter(forcastRVAdapter);

            }
        });
    }

    private void initializeForcastWeather(Location location) {

        String apikey = getString(R.string.forcastwether_api_key);
        weatherViewModel.getForcastWeather(location,unit,apikey);
    }


    private void initializeCurrentWeather(Location location) {
        String apikey = getString(R.string.wether_api_key);
        weatherViewModel.getCurrentWeather(location,unit,apikey);

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
        else
        {
            locationViewModel.getDeviceCurrentLocation();

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
