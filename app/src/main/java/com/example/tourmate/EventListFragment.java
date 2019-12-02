package com.example.tourmate;


import android.content.DialogInterface;
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

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tourmate.adapter.EventListRVAdapter;
import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {

private EventViewModel eventViewModel;
private EventListRVAdapter eventListRVAdapter;
private FloatingActionButton addEventBtn;
private RecyclerView eventRV;
private ProgressBar eventProgressBar;
private CardView addeventCard;
    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.event_menu_layout,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.addEventMenu:
                Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.add_Event);
                break;
        }

        return super.onOptionsItemSelected(item);
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
        addEventBtn = view.findViewById(R.id.addEventBtn);
        eventProgressBar = view.findViewById(R.id.eventProgress);

        addeventCard = view.findViewById(R.id.addEventCardView);



        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.action_eventListFragment_to_add_Event);
            }
        });

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



                eventRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        switch (newState) {
                            case RecyclerView.SCROLL_STATE_IDLE:
                                addEventBtn.show();
                                break;
                            default:
                                addEventBtn.hide();
                                break;
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            }
        });
    }

}
