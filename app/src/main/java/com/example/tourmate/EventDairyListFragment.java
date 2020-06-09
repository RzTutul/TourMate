package com.example.tourmate;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourmate.adapter.DairyRVAdapter;
import com.example.tourmate.pojos.DairyPojo;
import com.example.tourmate.viewmodels.DairyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


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
                Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.AddeventDairyFragment,bundle);
            }
        });

        dairyViewModel.dairyLD.observe(getActivity(), new Observer<List<DairyPojo>>() {
            @Override
            public void onChanged(List<DairyPojo> dairyPojos) {
                DairyRVAdapter dairyRVAdapter = new DairyRVAdapter(getActivity(),dairyPojos);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                allDairyRV.setLayoutManager(llm);
                allDairyRV.setAdapter(dairyRVAdapter);




                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        //Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Remove swiped item from list and notify the RecyclerView

                        try {


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Want to Delete this dairy note?");
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {

                                        int position = viewHolder.getAdapterPosition();
                                       // dairyPojos.remove(position);
                                        dairyRVAdapter.removeFromDatabase(dairyPojos.get(position));
                                        Snackbar.make(getView(),"Deleted",Snackbar.LENGTH_SHORT).show();
                                        dairyRVAdapter.notifyDataSetChanged();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dairyRVAdapter.notifyDataSetChanged();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        catch (Exception e)
                        {
                        }





                    }
                };

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(allDairyRV);


            }



        });





    }
}
