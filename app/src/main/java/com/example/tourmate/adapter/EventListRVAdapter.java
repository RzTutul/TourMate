package com.example.tourmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.pojos.TourMateEventPojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventListRVAdapter extends RecyclerView.Adapter<EventListRVAdapter.EventViewHolder>{
    private List<TourMateEventPojo> eventPojos;
    private Context context;

    public EventListRVAdapter(Context context,List<TourMateEventPojo> eventPojos) {
        this.eventPojos = eventPojos;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.event_row,parent,false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        holder.eventName.setText(eventPojos.get(position).getEventName());
        holder.startLocation.setText(eventPojos.get(position).getDeparture());
        holder.destination.setText(eventPojos.get(position).getDestination());
        holder.date.setText(eventPojos.get(position).getDepartureDate());
        holder.budget.setText(String.valueOf(eventPojos.get(position).getInitialBudget()));
        holder.createEventDate.setText((eventPojos.get(position).getCreateEventDate()));

        String goningdate =eventPojos.get(position).getDepartureDate();
        String crrentDate = getCrrentDateTime();
        String[] arrOfStr = goningdate.split("/", 2);
        String[] arrOfStr1 = crrentDate.split("/", 2);



      holder.dateLeft.setText(String.valueOf((Integer.parseInt(arrOfStr[0])-Integer.parseInt(arrOfStr1[0]))+" days Left"));

        holder.rowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    private String getCrrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
