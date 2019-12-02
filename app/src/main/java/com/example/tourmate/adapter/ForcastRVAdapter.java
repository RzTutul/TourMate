package com.example.tourmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.forcastweatherpojo.ForcastList;
import com.example.tourmate.helper.EventUtils;

import java.util.List;

public class ForcastRVAdapter  extends RecyclerView.Adapter<ForcastRVAdapter.ForcastViewHolder>{
    private Context context;
    private List<ForcastList> responsBodyList;

    public ForcastRVAdapter(Context context, List<ForcastList> responsBodyList) {
        this.context = context;
        this.responsBodyList = responsBodyList;
    }

    @NonNull
    @Override
    public ForcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.forcast_row,parent,false);

        return new ForcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForcastViewHolder holder, int position) {
        holder.mintemp.setText(String.valueOf(responsBodyList.get(position).getTemp().getMin()));
        holder.maxtemp.setText(String.valueOf(responsBodyList.get(position).getTemp().getMax()));
        holder.date.setText(String.valueOf(EventUtils.getFormattedDate(responsBodyList.get(position).getDt())));
    }

    @Override
    public int getItemCount() {
        return responsBodyList.size();
    }

    public class ForcastViewHolder extends RecyclerView.ViewHolder
    {

        TextView mintemp,maxtemp,date;

        public ForcastViewHolder(@NonNull View itemView) {
            super(itemView);

            maxtemp = itemView.findViewById(R.id.row_forcast_min);
            mintemp = itemView.findViewById(R.id.row_forcast_temp_max);
            date = itemView.findViewById(R.id.row_forcast_date);

        }
    }
}
