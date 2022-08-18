package com.example.root;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class winneruseradapter extends RecyclerView.Adapter<winneruseradapter.viewholder> {

    private final List<WinnerUserModel> winnerUserModelList;

    public winneruseradapter(List<WinnerUserModel> winnerUserModelList) {
        this.winnerUserModelList = winnerUserModelList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.winner_show_user_data,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.setData(winnerUserModelList.get(position).getRank(),
                winnerUserModelList.get(position).getName(),
                winnerUserModelList.get(position).getGameId(),
                winnerUserModelList.get(position).getUrl(),
                winnerUserModelList.get(position).getAmount());
    }




    @Override
    public int getItemCount() {
        return winnerUserModelList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {

        TextView Rank,Winner_Name,Winner_winAmount,Winner_UserGameId;
        CircleImageView winnerImage;





        public viewholder(@NonNull View itemView) {
            super(itemView);
            try {
                Rank = itemView.findViewById(R.id.Rank);
                Winner_Name = itemView.findViewById(R.id.Winner_Name);
                Winner_winAmount = itemView.findViewById(R.id.Winner_winAmount);
                Winner_UserGameId = itemView.findViewById(R.id.winner_UserGameId);
                winnerImage = itemView.findViewById(R.id.Winner_Image);
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }

        }

        @SuppressLint("SetTextI18n")
        private void setData(String rank, String name, String gameId, String url, String amount){
            try {
                Rank.setText("Rank# " + rank);
                Winner_Name.setText(name);
                Winner_UserGameId.setText(gameId);
                Glide.with(itemView.getContext()).load(url).into(winnerImage);
                Winner_winAmount.setText("Won " + amount + "/Rs");
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }

        }
    }
}

