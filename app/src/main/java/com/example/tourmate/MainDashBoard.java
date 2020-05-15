package com.example.tourmate;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.adapter.ExpenseListRVAdpater;
import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.pojos.EventExpensePojo;
import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.example.tourmate.viewmodels.ExpenseViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainDashBoard extends Fragment {
    private String eventID = null;
    private FloatingActionButton addExpenseBtn;
    private TextView eventName, budgetTV, expenseTV, remainingTV;
    private EventViewModel eventViewModel;
    private ExpenseViewModel expenseViewModel;
    private int totalBudget = 0;
    private CircularProgressBar budgetProgressbar,balanceProgressbar,expenseProgressbar;


    private RecyclerView expenseRV;
    private ExpenseListRVAdpater expenseAdapter;
    private CardView addExpenseCard;

    private LinearLayout addmoreBudgetLL;

    public MainDashBoard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.addmore_budget_menu_layout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addmoreBudget_menu:

                addmoreBudgetDialog();

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        expenseViewModel = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventID = bundle.getString("id");

            eventViewModel.getEventDetails(eventID);
            expenseViewModel.getAllExpense(eventID);

        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_dash_board, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventName = view.findViewById(R.id.eventNameTV);
        budgetTV = view.findViewById(R.id.budgetTV);
        expenseTV = view.findViewById(R.id.expenseTV);
        remainingTV = view.findViewById(R.id.remainingTV);
        expenseRV = view.findViewById(R.id.expenseRV);

        addExpenseBtn = view.findViewById(R.id.addExpenseBtn);

        expenseProgressbar = view.findViewById(R.id.expenseprogressBar);
        addExpenseCard = view.findViewById(R.id.addExpenseCardView);

        addExpenseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseDilog();
            }
        });

        budgetProgressbar = view.findViewById(R.id.budgetProgress);
        balanceProgressbar = view.findViewById(R.id.balancecircularProgressbar);
        addmoreBudgetLL = view.findViewById(R.id.addMoreBudgetLL);


        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseDilog();
            }
        });

        addmoreBudgetLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addmoreBudgetDialog();
            }
        });



        eventViewModel.eventDetailsLD.observe(getActivity(), new Observer<TourMateEventPojo>() {
            @Override
            public void onChanged(TourMateEventPojo eventPojo) {


                try {
                    eventName.setText(eventPojo.getEventName());
                    budgetTV.setText(String.valueOf(eventPojo.getInitialBudget()));
                    totalBudget = eventPojo.getInitialBudget();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        expenseViewModel.expenseListLD.observe(getActivity(), new Observer<List<EventExpensePojo>>() {
            @Override
            public void onChanged(List<EventExpensePojo> eventExpensePojos) {
                int totalEx = 0;
                int remaining = 0;

                for (EventExpensePojo expensePojo : eventExpensePojos) {
                    totalEx += expensePojo.getAmount();
                }
                remaining = totalBudget - totalEx;

                expenseTV.setText(String.valueOf(totalEx));
                remainingTV.setText(String.valueOf(remaining));

                int size = eventExpensePojos.size();
                expenseRV.setVisibility(View.VISIBLE);

                double percentage = (((double) totalEx / (double) totalBudget) * 100.0);

                final double remainingParcent = percentage-100.0;
                expenseProgressbar.setProgressWithAnimation((float) percentage, (long) 1000);
                //expenseProgressbar.setProgress(parcent);
                balanceProgressbar.setProgressWithAnimation((float) remainingParcent, (long) 1000);
                //balanceProgressbar.setProgress(remainingParcent);

                budgetProgressbar.setProgressWithAnimation(100, (long) 1000);


                if (size > 0) {

                    expenseAdapter = new ExpenseListRVAdpater(getActivity(), eventExpensePojos);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    expenseRV.setLayoutManager(llm);
                    expenseRV.setAdapter(expenseAdapter);
                    addExpenseCard.setVisibility(View.GONE);

                } else {

                    addExpenseCard.setVisibility(View.VISIBLE);
                    expenseRV.setVisibility(View.GONE);

                }


                expenseRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        switch (newState) {
                            case RecyclerView.SCROLL_STATE_IDLE:
                                addExpenseBtn.show();
                                break;
                            default:
                                addExpenseBtn.hide();
                                break;
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            }
        });


    }

    private void showExpenseDilog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(" Add Expense");
        builder.setIcon(R.drawable.taka1);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_dialog, null);

        builder.setView(view1);
        final EditText expenseNameET = view1.findViewById(R.id.expenseNameET);
        final EditText expenseAmoutET = view1.findViewById(R.id.expenseAmountET);
        final Button canelbtn = view1.findViewById(R.id.cancelBtn);
        final Button addexpenseBtn = view1.findViewById(R.id.addbtn);


        final AlertDialog dialog = builder.create();
        dialog.show();
        addexpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = expenseNameET.getText().toString();
                String amount = expenseAmoutET.getText().toString();

                if (ename.isEmpty()) {
                    expenseNameET.setError("Provide expense name!");
                } else if (amount.isEmpty()) {
                    expenseAmoutET.setError("Provide expense amount!");
                } else {
                    EventExpensePojo expensePojo = new EventExpensePojo(null, eventID, ename, Integer.parseInt(amount), EventUtils.getDateWithTime());
                    expenseViewModel.saveExpense(expensePojo);
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        canelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void addmoreBudgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add more budget!");

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.addmore_budget_dialog, null);

        builder.setView(view);

        final TextInputEditText amountET = view.findViewById(R.id.addmoreBudgetET);
        Button addbtn = view.findViewById(R.id.addMorebudgetBtn);
        Button cancel = view.findViewById(R.id.cancelBtn);

        final AlertDialog dialog = builder.create();
        dialog.show();
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = amountET.getText().toString();
                if (amount.isEmpty()) {
                    amountET.setError("Enter Amount!");
                } else {
                    int currentBudget = totalBudget + Integer.parseInt(amount);
                    eventViewModel.addMorebudget(eventID, currentBudget);
                    Toast.makeText(getActivity(), "Added Budget!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}
