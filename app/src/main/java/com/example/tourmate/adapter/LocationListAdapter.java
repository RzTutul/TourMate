package com.example.tourmate.adapter;

import android.content.Context;

import android.location.Address;
import android.location.Geocoder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;

import com.example.tourmate.nearby.Result;
import com.google.android.gms.location.places.GeoDataClient;


import java.io.IOException;

import java.util.List;
import java.util.Locale;



public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationListViewHolder> {


    private Context context;
    private List<Result> results;
    List<Address> addressList = null;
    private GeoDataClient geoDataClient;


    public LocationListAdapter(Context context, List<Result> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public LocationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.map_location_list_row,parent,false);

        return new LocationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationListViewHolder holder, int position) {

        //Log.i(TAG, "onBindViewHolder: "+"https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+results.get(position).getPhotos().get(0).getPhotoReference());
        holder.locationNameTV.setText(results.get(position).getName());
        holder.locationRatingTV.setRating((results.get(position).getRating()));

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addressList = geocoder.getFromLocation(results.get(position).getGeometry().getLocation().getLat(),
                    results.get(position).getGeometry().getLocation().getLng(), 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String address = addressList.get(0).getAddressLine(0);
        holder.locationAddressTV.setText(address);


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class LocationListViewHolder extends RecyclerView.ViewHolder{

        ImageView locationImage;
        TextView locationNameTV,locationAddressTV;
        RatingBar locationRatingTV;

        public LocationListViewHolder(@NonNull View itemView) {
            super(itemView);
            locationImage = itemView.findViewById(R.id.map_place_image);
            locationNameTV = itemView.findViewById(R.id.map_placeName);
            locationRatingTV = itemView.findViewById(R.id.map_place_rating);
            locationAddressTV = itemView.findViewById(R.id.map_locationAddress);
        }
    }
}
