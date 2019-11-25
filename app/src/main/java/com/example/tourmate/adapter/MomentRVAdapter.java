package com.example.tourmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.pojos.MomentPojo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MomentRVAdapter extends RecyclerView.Adapter<MomentRVAdapter.MomentViewHolder>{
    private Context context;
    private List<MomentPojo> momentPojos;

    public MomentRVAdapter(Context context, List<MomentPojo> momentPojos) {
        this.context = context;
        this.momentPojos = momentPojos;
    }

    @NonNull
    @Override
    public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MomentViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.gallery_row, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MomentViewHolder holder, int position) {
        Picasso.get().load(momentPojos.get(position).getDownloadUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return momentPojos.size();
    }

    public class MomentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MomentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gallery_row_moment);

        }
    }
}
