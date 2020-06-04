package com.example.tourmate.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.pojos.DairyPojo;

import java.util.List;

public class DairyRVAdapter extends RecyclerView.Adapter<DairyRVAdapter.DairyViewHolder> {

    Context context;
    List<DairyPojo> dairyPojoList;

    public DairyRVAdapter(Context context, List<DairyPojo> dairyPojoList) {
        this.context = context;
        this.dairyPojoList = dairyPojoList;
    }

    @NonNull
    @Override
    public DairyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflatere = LayoutInflater.from(context);

        View view = inflatere.inflate(R.layout.dairy_row_layout,parent,false);

        return new DairyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DairyViewHolder holder, int position) {

        holder.titleTV.setText(dairyPojoList.get(position).getTitle());
        holder.noteTV.setText(dairyPojoList.get(position).getNote());
        holder.dateTV.setText(dairyPojoList.get(position).getDate());

        Bundle bundle = new Bundle();
        bundle.putString("dairyID",dairyPojoList.get(position).getDairyID());
        bundle.putString("id",dairyPojoList.get(position).getEventID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(holder.itemView).navigate(R.id.AddeventDairyFragment,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dairyPojoList.size();
    }

    public class DairyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV,noteTV,dateTV;
        public DairyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            noteTV = itemView.findViewById(R.id.noteTV);
            dateTV = itemView.findViewById(R.id.dateTV);
        }
    }
}
