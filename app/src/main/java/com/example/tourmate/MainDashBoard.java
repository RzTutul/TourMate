package com.example.tourmate;


import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.adapter.ExpenseListRVAdpater;
import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.pojos.EventExpensePojo;
import com.example.tourmate.pojos.ExpenseAmountData;
import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.example.tourmate.viewmodels.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainDashBoard extends Fragment {
    private static final int STORAGE_CODE = 123;
    private String eventID = null;
    private FloatingActionButton addExpenseBtn;
    private TextView eventName, budgetTV, expenseTV, remainingTV;
    private EventViewModel eventViewModel;
    private ExpenseViewModel expenseViewModel;
    private int totalBudget = 0;
    private CircularProgressBar budgetProgressbar, balanceProgressbar, expenseProgressbar;


    private RecyclerView expenseRV;
    private ExpenseListRVAdpater expenseAdapter;
    private CardView addExpenseCard;


    private LinearLayout addmoreBudgetLL, expenseCardLL;

    String exCatagories;
    int foodAmount, transportAmount, hotelAmount, otherAmount;

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
            case R.id.expenseDetails_menu:
                showExpenseGraph();
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

        expenseCardLL = view.findViewById(R.id.expenseCardLL);
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

        expenseCardLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseGraph();
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

                final double remainingParcent = percentage - 100.0;
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



    private void showExpenseGraph() {
        expenseViewModel.expenseListLD.observe(getActivity(), new Observer<List<EventExpensePojo>>() {
            @Override
            public void onChanged(List<EventExpensePojo> eventExpensePojos) {

                foodAmount = 0;
                transportAmount = 0;
                hotelAmount = 0;
                otherAmount = 0;
                for (EventExpensePojo eventExpensePojo : eventExpensePojos) {
                    if (eventExpensePojo.getE_catagories().equals("Food")) {
                        foodAmount = foodAmount + eventExpensePojo.getAmount();
                        Log.i(TAG, "foodAmount:  " + eventExpensePojo.getE_catagories() + foodAmount);


                    } else if (eventExpensePojo.getE_catagories().equals("Transport")) {
                        transportAmount = transportAmount + eventExpensePojo.getAmount();
                        Log.i(TAG, "transport:  " + transportAmount);

                    } else if (eventExpensePojo.getE_catagories().equals("Hotel")) {
                        hotelAmount = hotelAmount + eventExpensePojo.getAmount();
                    } else {
                        otherAmount = otherAmount + eventExpensePojo.getAmount();
                    }
                }


            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.expense_graph_layout, null);

        builder.setView(view);

        TextView foodTV = view.findViewById(R.id.foodamoutTV);
        TextView transportTV = view.findViewById(R.id.transportamoutTV);
        TextView hotelTV = view.findViewById(R.id.hotelamoutTV);
        TextView otherTV = view.findViewById(R.id.otheramoutTV);
        TextView budgetTV = view.findViewById(R.id.budgetTV);

        foodTV.setText(String.valueOf(foodAmount));
        transportTV.setText(String.valueOf(transportAmount));
        hotelTV.setText(String.valueOf(hotelAmount));
        otherTV.setText(String.valueOf(otherAmount));
        budgetTV.setText(String.valueOf(totalBudget));

        PieChart mPieChart = (PieChart) view.findViewById(R.id.piechart);

        double foodamount = ((double) (totalBudget - foodAmount) / (double) (totalBudget)) * 100.0;
        double transportamount = ((double) (totalBudget - transportAmount) / (double) totalBudget) * 100.0;
        double hotelamount = ((double) (totalBudget - hotelAmount) / (double) totalBudget) * 100.0;
        double otheramount = ((double) (totalBudget - otherAmount) / (double) totalBudget) * 100.0;

        Log.i(TAG, "showExpenseGraph: " + totalBudget + " " + (int) foodamount + " " +(int) transportamount + " " +(int) hotelamount + " " +(int) hotelamount + " " +(int) otheramount);
        mPieChart.addPieSlice(new PieModel("Food", (100 - ((int) foodamount)), Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Transport",  (100 - ((int) transportamount)), Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Hotel",  (100 - ((int) hotelamount)), Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Other",  (100 - ((int) otheramount)), Color.parseColor("#FED70E")));
        mPieChart.addPieSlice(new PieModel("",  0, Color.parseColor("#C70039")));

        mPieChart.startAnimation();

        final AlertDialog dialog = builder.create();
        dialog.show();
    }







    private void showExpenseDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(" Add Expense");
        builder.setIcon(R.drawable.taka1);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_dialog, null);

        builder.setView(view1);
        final EditText expenseNameET = view1.findViewById(R.id.expenseNameET);
        final EditText expenseAmoutET = view1.findViewById(R.id.expenseAmountET);
        final Spinner expenseCatagoriesSP = view1.findViewById(R.id.expenseCatagories);

        final Button canelbtn = view1.findViewById(R.id.cancelBtn);
        final Button addexpenseBtn = view1.findViewById(R.id.addbtn);
        String[] catagories = {"Select Catagories", "Food", "Transport", "Hotel", "Other"};


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catagories);

        expenseCatagoriesSP.setAdapter(arrayAdapter);


        expenseCatagoriesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exCatagories = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                    EventExpensePojo expensePojo = new EventExpensePojo(null, eventID, ename, Integer.parseInt(amount), exCatagories, EventUtils.getDateWithTime());
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
