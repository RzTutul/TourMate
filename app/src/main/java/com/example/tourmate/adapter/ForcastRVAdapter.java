package com.example.tourmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.forcastweatherpojo.ForcastList;
import com.example.tourmate.helper.EventUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForcastRVAdapter  extends RecyclerView.Adapter<ForcastRVAdapter.ForcastViewHolder>{
    private Context context;
    private List<ForcastList> responsBodyList;
    private String tempunit ;

    public ForcastRVAdapter(Context context, List<ForcastList> responsBodyList,String tempunit) {
        this.context = context;
        this.responsBodyList = responsBodyList;
        this.tempunit=tempunit;
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

            holder.mintemp.setText(String.valueOf(Math.round(responsBodyList.get(position).getTemp().getMin()))+EventUtils.DEGREE+tempunit);
            holder.maxtemp.setText((Math.round(responsBodyList.get(position).getTemp().getMax()))+EventUtils.DEGREE+tempunit);

        holder.date.setText((EventUtils.getFormattedDate(responsBodyList.get(position).getDt())));
        holder.weatherStatus.setText(responsBodyList.get(position).getWeather().get(0).getDescription());
        holder.sunriseTV.setText(EventUtils.getTime(responsBodyList.get(position).getSunrise()));
        holder.sunsetTV.setText(EventUtils.getTime(responsBodyList.get(position).getSunset()));
        String icon = responsBodyList.get(position).getWeather().get(0).getIcon();
        Picasso.get().load(EventUtils.WEATHER_CONDITION_ICON_PREFIX+icon+".png")
                .into(holder.weatherIcon);
    }

    @Override
    public int getItemCount() {
        return responsBodyList.size();
    }

    public class ForcastViewHolder extends RecyclerView.ViewHolder
    {

        TextView mintemp,maxtemp,date,weatherStatus,sunriseTV,sunsetTV;
        ImageView weatherIcon;

        public ForcastViewHolder(@NonNull View itemView) {
            super(itemView);

            maxtemp = itemView.findViewById(R.id.row_forcast_temp_max);
            mintemp = itemView.findViewById(R.id.row_forcast_min);
            date = itemView.findViewById(R.id.row_forcast_date);
            weatherIcon = itemView.findViewById(R.id.row_forcast_image);
            weatherStatus = itemView.findViewById(R.id.row_forcast_status);
            sunriseTV = itemView.findViewById(R.id.row_forcast_sunrise);
            sunsetTV = itemView.findViewById(R.id.row_forcast_sunset);

        }
    }
}
