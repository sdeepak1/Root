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

public class winneramountshowadapter extends RecyclerView.Adapter<winneramountshowadapter.viewholder> {
    List<showAmountModel> modelList;

    public winneramountshowadapter(List<showAmountModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.winneritem,parent,false);
        return new viewholder(view);    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(modelList.get(position).getRank(),modelList.get(position).getAmount());
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView rank,amount;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            try {
                rank = itemView.findViewById(R.id.winnerItemRank);
                amount = itemView.findViewById(R.id.winnerItemAmount);
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }
        }

        @SuppressLint("SetTextI18n")
        private void setData(String ranks,String amounts){
            try {
                rank.setText("#" + ranks);
                amount.setText(amounts);
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }
        }
    }
}
