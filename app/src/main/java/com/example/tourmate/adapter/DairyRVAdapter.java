package com.example.tourmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.AddEventDairyFragment;
import com.example.tourmate.R;
import com.example.tourmate.pojos.DairyPojo;
import com.example.tourmate.viewmodels.DairyViewModel;

import java.util.List;

public class DairyRVAdapter extends RecyclerView.Adapter<DairyRVAdapter.DairyViewHolder> {

    Context context;
    List<DairyPojo> dairyPojoList;
    DairyViewModel dairyViewModel;


    public DairyRVAdapter(Context context, List<DairyPojo> dairyPojoList) {
        this.context = context;
        this.dairyPojoList = dairyPojoList;
dairyViewModel =  new DairyViewModel();

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

        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = dairyPojoList.get(position).getTitle();
                String note = dairyPojoList.get(position).getNote();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, title+"\n"+note);
                context.startActivity(Intent.createChooser(shareIntent, "Share Dairy"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return dairyPojoList.size();
    }

    public void removeFromDatabase(DairyPojo dairyPojo) {

        dairyPojoList.remove(dairyPojo);
        dairyViewModel.deleteDairy(dairyPojo.getEventID(),dairyPojo.getDairyID());
    }

    public class DairyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV,noteTV,dateTV;
        Button sharebtn;
        public DairyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            noteTV = itemView.findViewById(R.id.noteTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            sharebtn = itemView.findViewById(R.id.sharebtn);
        }
    }
}
