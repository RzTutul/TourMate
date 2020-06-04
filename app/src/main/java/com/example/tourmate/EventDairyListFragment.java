package com.example.tourmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourmate.adapter.DairyRVAdapter;
import com.example.tourmate.pojos.DairyPojo;
import com.example.tourmate.viewmodels.DairyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDairyListFragment extends Fragment {


    FloatingActionButton addDairyBtn;
    RecyclerView allDairyRV;
    DairyViewModel dairyViewModel;
    String eventID;
    public EventDairyListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            eventID = bundle.getString("id");

        }


        dairyViewModel = ViewModelProviders.of(this).get(DairyViewModel.class);
        dairyViewModel.getAllDairy(eventID);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_dairy_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addDairyBtn = view.findViewById(R.id.addDairyBtn);
        allDairyRV = view.findViewById(R.id.eventDairyRV);

        addDairyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id",eventID);
                bundle.putString("dairyID",null);
                Navigation.findNavController(view).navigate(R.id.AddeventDairyFragment,bundle);
            }
        });

        dairyViewModel.dairyLD.observe(getActivity(), new Observer<List<DairyPojo>>() {
            @Override
            public void onChanged(List<DairyPojo> dairyPojos) {
                DairyRVAdapter dairyRVAdapter = new DairyRVAdapter(getActivity(),dairyPojos);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                allDairyRV.setLayoutManager(llm);
                allDairyRV.setAdapter(dairyRVAdapter);
            }
        });
    }
}
