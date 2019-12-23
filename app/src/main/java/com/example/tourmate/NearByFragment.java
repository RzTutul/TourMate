package com.example.tourmate;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.adapter.LocationListAdapter;
import com.example.tourmate.nearby.NearByResponseBody;
import com.example.tourmate.nearby.Result;
import com.example.tourmate.viewmodels.LocationViewModel;
import com.example.tourmate.viewmodels.NearByViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearByFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap googleMap;
    private LocationViewModel locationViewModel;
    private TextView connectionStatusTV;
    private Button searchBtn;
    private String type="";
    private int place_distance;
    private Location currentLocation;
    private NearByViewModel nearByViewModel;
    private Spinner placeTypeSp,distanceSP;
    private BottomSheetBehavior mBottomSheetBehavior;
    private RecyclerView locationListRV;
    private ImageView clopseBtn;

    public NearByFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isLocationPermissionGranted();
        locationViewModel =
                ViewModelProviders.of(getActivity())
                        .get(LocationViewModel.class);

nearByViewModel = ViewModelProviders.of(getActivity()).get(NearByViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_near_by, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button searchNearbyBtn = view.findViewById(R.id.searchBtn);

        placeTypeSp = view.findViewById(R.id.placetypeSP);
        distanceSP = view.findViewById(R.id.placeDistanceSP);
        locationListRV = view.findViewById(R.id.map_locationRV);
        connectionStatusTV = view.findViewById(R.id.connectionStatus);



        if (EventListFragment.InternetConnection.checkConnection(getActivity())) {
            connectionStatusTV.setVisibility(View.GONE);
        }
        else {
            Toast.makeText(getActivity(), "No Connetion", Toast.LENGTH_SHORT).show();
            connectionStatusTV.setVisibility(View.VISIBLE);

        }

        String placename[] = getResources().getStringArray(R.array.place_type);
        String distance[] = getResources().getStringArray(R.array.distance);

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, placename);
        ArrayAdapter<String> distanceAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, distance);

        placeTypeSp.setAdapter(adapter);
        distanceSP.setAdapter(distanceAdapter);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                locationViewModel.getDeviceCurrentLocation();
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




        View bottomSheet = view.findViewById(R.id.bottom_sheet_map);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);







        placeTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String placeTypeName = adapterView.getItemAtPosition(i).toString();
                googleMap.clear();

                if (placeTypeName.equals("restaurant"))
                {
                    googleMap.clear();
                    type ="restaurant";
                 //   getNearbyResponseData();
                    Toast.makeText(getActivity(), "restaurant", Toast.LENGTH_SHORT).show();
                }

                else if (placeTypeName.equals("atm"))
                {
                    googleMap.clear();
                    type ="atm";
                   // getNearbyResponseData();
                    Toast.makeText(getActivity(), "atm", Toast.LENGTH_SHORT).show();

                }

                else if (placeTypeName.equals("tourist_attraction"))
                {
                    googleMap.clear();
                    type ="tourist_attraction";
                   // getNearbyResponseData();
                    Toast.makeText(getActivity(), "tourist_attraction", Toast.LENGTH_SHORT).show();

                }   else if (placeTypeName.equals("bus_station"))
                {
                    googleMap.clear();
                    type ="bus_station";
                   // getNearbyResponseData();
                    Toast.makeText(getActivity(), "bus station", Toast.LENGTH_SHORT).show();

                }
                else if (placeTypeName.equals("train_station"))
                {
                    googleMap.clear();
                    type ="train_station";
                   // getNearbyResponseData();
                    Toast.makeText(getActivity(), "train station", Toast.LENGTH_SHORT).show();

                }


                else
                {
                    googleMap.clear();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        distanceSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String distance = parent.getItemAtPosition(position).toString();

                if (distance.equals("0.5km"))
                {
                    place_distance = 500;
                }
                   else if (distance.equals("1km"))
                {
                    place_distance = 1000;

                }
                  else if (distance.equals("1.5km"))
                {
                    place_distance = 1500;

                }    else if (distance.equals("2km"))
                {
                    place_distance = 2000;

                }    else if (distance.equals("3km"))
                {
                    place_distance = 3000;

                }

                  else
                {

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        searchNearbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleMap.clear();
                getNearbyResponseData();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationViewModel.locationLD.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location == null){

                    return;
                }
                currentLocation = location;


                LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
                if (googleMap != null){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 14f));
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

    private void getNearbyResponseData() {
        String apiKey = getString(R.string.nearby_place_api_key);
        String endUrl =
                String.format("place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s",
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude(),place_distance,
                        type,
                        apiKey);

        nearByViewModel.getResponseFromRepo(endUrl).observe(this, new Observer<NearByResponseBody>() {
            @Override
            public void onChanged(NearByResponseBody nearByResponseBody) {
                List<Result> results = nearByResponseBody.getResults();
                if (results.size()>0)
                {
                    connectionStatusTV.setVisibility(View.GONE);
                    for (Result r : results) {
                        double lat = r.getGeometry().getLocation().getLat();
                        double lng = r.getGeometry().getLocation().getLng();
                        String name = r.getName();
                        LatLng latLng = new LatLng(lat, lng);
                        googleMap.addMarker(new MarkerOptions()
                                .title(name)
                                .position(latLng));
                    }

                    LocationListAdapter locationListAdapter = new LocationListAdapter(getActivity(),results);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    locationListRV.setLayoutManager(llm);
                    locationListRV.setAdapter(locationListAdapter);

                }





            }
        });

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
            Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.nearByFragment);


        }
        else
        {
            Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.eventListFragment);
            Toast.makeText(getActivity(), "Need Permission for use Map", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {


        try {
            this.googleMap = googleMap;
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMyLocationEnabled(true);
            locationViewModel.getDeviceCurrentLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123){
            if (resultCode == Activity.RESULT_OK){
                locationViewModel.getLocationUpdate();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        locationViewModel.getDeviceCurrentLocation();
    }


    public static class InternetConnection {

        /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
        public static boolean checkConnection(Context context)
        {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }
    }
}
