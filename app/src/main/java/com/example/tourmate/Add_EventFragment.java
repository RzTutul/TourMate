package com.example.tourmate;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_EventFragment extends Fragment {

    private Button createEvnBtn,departureDateBtn;
    private TextInputEditText eventNameET,startLocationET,destinationET,budgetET;
    private String departureDate;
    private EventViewModel eventViewModel;


    public Add_EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventNameET = view.findViewById(R.id.eventNameET);
        startLocationET = view.findViewById(R.id.startLocationET);
        destinationET = view.findViewById(R.id.destinationET);
        budgetET = view.findViewById(R.id.budgetET);
        createEvnBtn = view.findViewById(R.id.createEventbtn);
        departureDateBtn = view.findViewById(R.id.departureDatebtn);


        createEvnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameET.getText().toString();
                String startLocation = startLocationET.getText().toString();
                String destination = destinationET.getText().toString();
                String budget = budgetET.getText().toString();

                TourMateEventPojo tourMateEventPojo = new TourMateEventPojo(null,eventName,startLocation,destination,Integer.parseInt(budget),departureDate);
                eventViewModel.SaveEvent(tourMateEventPojo);

                Navigation.findNavController(view).navigate(R.id.eventListFragment);

            }
        });

        departureDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateaDilogPicker();
            }
        });



    }

    private void showDateaDilogPicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                dateSetListener, year, month, day);
        dpd.show();


    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            departureDate = new SimpleDateFormat("dd/MM/yyyy")
                    .format(calendar.getTime());
            departureDateBtn.setText(departureDate);


        }
    };
}
