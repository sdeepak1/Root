package com.example.root;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class SlideAdapter extends SliderViewAdapter<SlideAdapter.Holder> {

    int[] images;
    public SlideAdapter(int[] images){
        this.images=images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ragister_slide_item,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public static class Holder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public Holder(View itemview){
            super(itemview);
            imageView=itemview.findViewById(R.id.imageview);
        }
    }
}
