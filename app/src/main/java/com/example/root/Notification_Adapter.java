package com.example.root;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;


public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.viewholder> {

    private final List<Notification_Model> notification_models;

    public Notification_Adapter(List<Notification_Model> notification_models) {
        this.notification_models = notification_models;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_bg,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(notification_models.get(position).getContent(),
                notification_models.get(position).getDate());
    }



    @Override
    public int getItemCount() {
        return notification_models.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
       TextView content,date;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            try {
                content = itemView.findViewById(R.id.Main_Content);
                date = itemView.findViewById(R.id.NotiDate);
            }catch (Exception e) {
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
            }



        }

        @SuppressLint("SetTextI18n")
        private void setData(String msg,String Date){
            try {
                content.setText(msg);
                date.setText("Date " + Date);
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }


        }
    }


}


