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

        TextView eventName,startLocation,destination,date,budget;
        TextView rowMenu;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.row_eventName);
            startLocation = itemView.findViewById(R.id.row_startLocation);
            destination = itemView.findViewById(R.id.row_destinaiton);
            date = itemView.findViewById(R.id.row_date);
            budget = itemView.findViewById(R.id.row_buget);
            rowMenu = itemView.findViewById(R.id.row_menu);

        }
    }
}
