package com.example.tourmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tourmate.pojos.EventExpensePojo;
import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.example.tourmate.viewmodels.ExpenseViewModel;

import java.util.List;


public class EventDashBoard extends Fragment {

    private Button expenseBtn,momonentBtn,addmoreBudget;
    private String eventID;

    private TextView eventName, budgetTV,expenseTV,remainingTV;
    private EventViewModel eventViewModel;
    private ExpenseViewModel expenseViewModel;
    private int totalBudget = 0;


    public EventDashBoard() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        expenseViewModel = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            eventID = bundle.getString("id");
            eventViewModel.getEventDetails(eventID);
            expenseViewModel.getAllExpense(eventID);

        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_dash_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      final Bundle bundle = new Bundle();
        bundle.putString("id",eventID);

        expenseBtn = view.findViewById(R.id.eventExpensebtn);
        momonentBtn = view.findViewById(R.id.eventMomentbtn);


        eventName = view.findViewById(R.id.eventNameTV);
        budgetTV = view.findViewById(R.id.budgetTV);
        expenseTV = view.findViewById(R.id.expenseTV);
        remainingTV = view.findViewById(R.id.remainingTV);


        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.mainDashBoard,bundle);
            }
        });

        momonentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.momentGallary,bundle);
            }
        });

        eventViewModel.eventDetailsLD.observe(this, new Observer<TourMateEventPojo>() {
            @Override
            public void onChanged(TourMateEventPojo eventPojo) {

                eventName.setText(eventPojo.getEventName());
                budgetTV.setText("Budget  "+eventPojo.getInitialBudget());
                totalBudget= eventPojo.getInitialBudget();
            }
        });

        expenseViewModel.expenseListLD.observe(this, new Observer<List<EventExpensePojo>>() {
            @Override
            public void onChanged(List<EventExpensePojo> eventExpensePojos) {
                int totalEx = 0;
                int remaining = 0;


                for (EventExpensePojo expensePojo : eventExpensePojos)
                {
                    totalEx += expensePojo.getAmount();
                }
                remaining = totalBudget - totalEx;

                expenseTV.setText("Total expense  "+totalEx);
                remainingTV.setText("Remaining  "+remaining);

            }
        });

    }
}
