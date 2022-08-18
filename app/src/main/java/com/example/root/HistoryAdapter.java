package com.example.root;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewholder> {

private final List<historyModel> historyModelList;

    public HistoryAdapter(List<historyModel> historyModelList) {
        this.historyModelList = historyModelList;
    }


    @NonNull
@Override
public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_trans_layout,parent,false);
        return new viewholder(view);
        }

@Override
public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(historyModelList.get(position).getTextData(),
                historyModelList.get(position).getTextDate(),
                historyModelList.get(position).getTextMoney(),
                historyModelList.get(position).getTextId(),
                historyModelList.get(position).getTextTime(),
                historyModelList.get(position).getValue());
        }



@Override
public int getItemCount() {
        return historyModelList.size();
        }

public static class viewholder extends RecyclerView.ViewHolder {
    TextView textMoney,textData,textId,textDate,textTime;
    LinearLayout Details;
    String val;


    public viewholder(@NonNull View itemView) {
        super(itemView);
        try {
            textMoney = itemView.findViewById(R.id.textMoney);
            textData = itemView.findViewById(R.id.textData);
            textDate = itemView.findViewById(R.id.textDate);
            textId = itemView.findViewById(R.id.textId);
            textTime = itemView.findViewById(R.id.textTime);
            Details = itemView.findViewById(R.id.Details);
        }catch (Exception e){
            StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }



    }

    @SuppressLint("SetTextI18n")
    private void setData(String data,String date,String money,String id,String time,final Boolean value) {
        try {
            textData.setText(data);
            textDate.setText(date);
            textMoney.setText(money);
            textId.setText(id);
            textTime.setText(time);
            val = value.toString();

            itemView.setOnClickListener(view -> {


                switch (val) {
                    case "true":
                        Details.setVisibility(View.GONE);
                        val = "false";
                        break;
                    case "false":
                        Details.setVisibility(View.VISIBLE);
                        val = "true";
                        break;
                }
            });
        }catch (Exception e){
            StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }


    }


}
}



