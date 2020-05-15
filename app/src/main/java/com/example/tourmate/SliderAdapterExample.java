package com.example.tourmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private int[] images;
    private String[] placeName;


    public SliderAdapterExample(Context context,int[] images,String[] placeName) {
        this.context = context;
        this.images = images;
        this.placeName = placeName;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_image_row, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
//        viewHolder.textViewDescription.setText("This is slider item " + position);


                //Picasso.get().load(images[position]).into(viewHolder.imageViewBackground);
        viewHolder.imageViewBackground.setBackgroundResource(images[position]);

                viewHolder.textViewDescription.setText(placeName[position]);





    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return images.length;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.placeImage);
            textViewDescription = itemView.findViewById(R.id.placetitle);
            this.itemView = itemView;
        }
    }

}
