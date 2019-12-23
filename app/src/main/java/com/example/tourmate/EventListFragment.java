package com.example.tourmate;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tourmate.adapter.EventListRVAdapter;
import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {

private EventViewModel eventViewModel;
private EventListRVAdapter eventListRVAdapter;

private RecyclerView eventRV;
private ProgressBar eventProgressBar;
private CardView addeventCard;
    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        eventRV = view.findViewById(R.id.eventRV);

        eventProgressBar = view.findViewById(R.id.eventProgress);

        addeventCard = view.findViewById(R.id.addEventCardView);

        if (InternetConnection.checkConnection(getActivity())) {
            // Internet Available...
        } else {
            // Internet Not Available...

        }


        addeventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.action_eventListFragment_to_add_Event);

            }
        });


        eventViewModel.eventListLD.observe(this, new Observer<List<TourMateEventPojo>>() {
            @Override
            public void onChanged(List<TourMateEventPojo> tourMateEventPojos) {

            int size = tourMateEventPojos.size();
                if (size<=0) {
                    addeventCard.setVisibility(View.VISIBLE);
                    eventProgressBar.setVisibility(View.GONE);

                }

                else
                {
                    eventListRVAdapter = new EventListRVAdapter(getActivity(), tourMateEventPojos);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    eventRV.setLayoutManager(llm);
                    eventRV.setAdapter(eventListRVAdapter);
                    eventProgressBar.setVisibility(View.GONE);
                }



            }
        });
    }

    public static class InternetConnection {

        /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
        public static boolean checkConnection(Context context)
        {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
              //  Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

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
