package com.example.tourmate.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.AddEventFrag;
import com.example.tourmate.MainActivity;
import com.example.tourmate.R;
import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.pojos.EventExpensePojo;
import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.example.tourmate.viewmodels.ExpenseViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventListRVAdapter extends RecyclerView.Adapter<EventListRVAdapter.EventViewHolder>{
    private List<TourMateEventPojo> eventPojos;
    private Context context;
    private EventViewModel eventViewModel = new EventViewModel();
     Bundle bundle;
    public EventListRVAdapter(Context context,List<TourMateEventPojo> eventPojos) {

        Collections.reverse(eventPojos);
        this.eventPojos = eventPojos;
        this.context = context;

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.event_row,parent,false);
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.layout_animation);
        view.startAnimation(shake);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int position) {

        holder.eventName.setText(eventPojos.get(position).getEventName());
        holder.startLocation.setText(eventPojos.get(position).getDeparture());
        holder.destination.setText(eventPojos.get(position).getDestination());
        holder.date.setText(eventPojos.get(position).getDepartureDate());
        holder.budget.setText(String.valueOf(eventPojos.get(position).getInitialBudget()));
        holder.createEventDate.setText((eventPojos.get(position).getCreateEventDate()));

        String goingDate =(eventPojos.get(position).getDepartureDate());


        long diffrentDate = EventUtils.getDefferentBetweenTwoDate(EventUtils.getCrrentDate(),goingDate);
        if (diffrentDate==0)
        {
            holder.dateLeft.setText("Have a safe Journey!");

        }
        else if (diffrentDate<0)
        {
            holder.dateLeft.setText("Tour Finished!");
        }

        else
        {
            holder.dateLeft.setText(String.valueOf(EventUtils.getDefferentBetweenTwoDate(EventUtils.getCrrentDate(),goingDate))+" Days Left");

        }


        holder.rowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String eventID = eventPojos.get(position).getEventID();
                final TourMateEventPojo eventPojo = eventPojos.get(position);

                 bundle = new Bundle();
                bundle.putString("id",eventID);


                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.row_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.detailMenu:
                                Navigation.findNavController(holder.itemView).navigate(R.id.mainDashBoard,bundle);
                                break;
                            case R.id.editMenu:
                             AddEventFrag.eventID = eventID;
                                Navigation.findNavController(holder.itemView).navigate(R.id.addEventFrag);
                                break;
                            case R.id.deleteMenu:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Delete this entire Event?");
                                builder.setMessage("Remember: Once Delete of the event it cannot be undone!");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            eventViewModel.DeleteEvent(eventPojo);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Navigation.findNavController(holder.itemView).navigate(R.id.eventListFragment
                                        );
                                    }
                                });
                                builder.setNegativeButton("Cancel",null);

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                break;

                        }

                        return false;
                    }
                });


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventID = eventPojos.get(position).getEventID();
                final TourMateEventPojo eventPojo = eventPojos.get(position);

                final Bundle bundle = new Bundle();
                bundle.putString("id",eventID);
                MainActivity.eventID = eventID;
                Navigation.findNavController(holder.itemView).navigate(R.id.mainDashBoard,bundle);




            }
        });



    }

    @Override
    public int getItemCount() {
        return eventPojos.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName,startLocation,destination,date,budget,createEventDate,dateLeft;
        TextView rowMenu;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.row_eventName);
            startLocation = itemView.findViewById(R.id.row_startLocation);
            destination = itemView.findViewById(R.id.row_destinaiton);
            date = itemView.findViewById(R.id.row_date);
            budget = itemView.findViewById(R.id.row_buget);
            createEventDate = itemView.findViewById(R.id.row_createEventdate);
            dateLeft = itemView.findViewById(R.id.row_daysLeft);

            rowMenu = itemView.findViewById(R.id.row_menu);

        }
    }




}
