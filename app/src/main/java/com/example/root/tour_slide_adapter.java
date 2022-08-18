package com.example.root;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class tour_slide_adapter extends SliderViewAdapter<tour_slide_adapter.Holder> {

    int[] images;
    public tour_slide_adapter(int[] images){
        this.images=images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_image_slider_layout,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.image.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public static class Holder extends SliderViewAdapter.ViewHolder{
        ImageView image;
        public Holder(View itemview){
            super(itemview);
            image=itemview.findViewById(R.id.tour_slide_image);
        }
    }
}
