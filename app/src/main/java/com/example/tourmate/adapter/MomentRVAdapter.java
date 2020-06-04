package com.example.tourmate.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.image_donwload_manager.DirectoryHelper;
import com.example.tourmate.image_donwload_manager.DownloadImageService;
import com.example.tourmate.R;
import com.example.tourmate.pojos.MomentPojo;
import com.example.tourmate.viewmodels.MomentViewModel;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class MomentRVAdapter extends RecyclerView.Adapter<MomentRVAdapter.MomentViewHolder>{
    private Context context;
    private List<MomentPojo> momentPojos;
    private MomentViewModel momentViewModel = new MomentViewModel();
    private AlertDialog dialog;



    public MomentRVAdapter(Context context, List<MomentPojo> momentPojos) {

        Collections.reverse(momentPojos);
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
    public void onBindViewHolder(@NonNull MomentViewHolder holder,final int position) {
        Picasso.get().load(momentPojos.get(position).getDownloadUrl()).fit().into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MomentPojo momentPojo = momentPojos.get(position);

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Image");
                LayoutInflater inflater = LayoutInflater.from(context);
                View view =inflater.inflate(R.layout.single_image_dialog,null);

                builder.setView(view);

                ImageView image = view.findViewById(R.id.imageView);
                ImageView deletebtn = view.findViewById(R.id.deleteImagebtn);
                ImageView donwloadbtn= view.findViewById(R.id.downlaodbtn);

                Picasso.get().load(momentPojos.get(position).getDownloadUrl()).fit().into(image);

               dialog = builder.create();
                dialog.show();

                deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("Do you want delete this Picture?");
                        builder1.setIcon(R.drawable.ic_delete_black_24dp);


                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                momentViewModel.deleteImage(momentPojo);
                                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();

                            }
                        });
                        builder1.setNegativeButton("No",null);

                     AlertDialog dialog1 = builder1.create();
                        dialog1.show();

                        dialog.dismiss();

                    }
                });

                donwloadbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startService(DownloadImageService.getDownloadService(context, momentPojo.getDownloadUrl(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));

                        dialog.dismiss();
                    }
                });


            }
        });
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
