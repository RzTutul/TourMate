package com.example.tourmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapDashBoardFragment extends Fragment {

    CardView atmCard,hotelCard,restuarantCard,tourPlaceCard,busCard,trainCard,hospitalCard,pharmacyCard;
    public MapDashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_dash_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        atmCard = view.findViewById(R.id.atmCard);
        hotelCard = view.findViewById(R.id.hotelCard);
        restuarantCard = view.findViewById(R.id.restaurantCard);
        tourPlaceCard = view.findViewById(R.id.toursPlaceCard);
        busCard = view.findViewById(R.id.busCard);
        trainCard = view.findViewById(R.id.trainCard);
        hospitalCard = view.findViewById(R.id.hostpitaCard);
        pharmacyCard = view.findViewById(R.id.pharmacyCard);


        Bundle bundle = new Bundle();


        atmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bundle.putString("name","ATMs");
                Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);

            }
        });

         hotelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bundle.putString("name","Hotels");
                Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);

            }
        });

         restuarantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bundle.putString("name","Restaurants");
                Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);
            }
        });

         tourPlaceCard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 bundle.putString("name","Touris");
                 Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);
             }
         });

         busCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bundle.putString("name","Bus Stations");
                Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);

            }
        });

         trainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bundle.putString("name","Train Stations");
                Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);

            }
        });

         hospitalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bundle.putString("name","Hospitals");
                Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);

            }
        });

         pharmacyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bundle.putString("name","Pharmacy");
                Navigation.findNavController(view).navigate(R.id.webViewMapFragment,bundle);

            }
        });




    }
}
