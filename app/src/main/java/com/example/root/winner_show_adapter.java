package com.example.root;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class winner_show_adapter extends RecyclerView.Adapter<winner_show_adapter.viewholder> {

    private final List<Winner_show_model> winner_show_modelList;

    public winner_show_adapter(List<Winner_show_model> winner_show_modelList) {
        this.winner_show_modelList = winner_show_modelList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.winner_show_data,parent,false);
        return new viewholder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(winner_show_modelList.get(position).getTournament_Name(),
                winner_show_modelList.get(position).getTournament_Date(),
                winner_show_modelList.get(position).getTournament_Time(),
                winner_show_modelList.get(position).getTournament_MatchType(),
                winner_show_modelList.get(position).getTournament_MatchMode(),
                winner_show_modelList.get(position).getTournament_WinnerAmount(),
                winner_show_modelList.get(position).getWinnerUserList());

    }

    @Override
    public int getItemCount() {
        return winner_show_modelList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
      TextView TName,TDate,TMatchType,TMatchMode,TWinnerAmount;
      RecyclerView userView;
      CircleImageView Tournament_Winner_Image;




        public viewholder(@NonNull View itemView) {
            super(itemView);
            try {
                TName = itemView.findViewById(R.id.Tournament_Winner_Name);
                TDate = itemView.findViewById(R.id.Tournament_DateAndTime);
                TMatchType = itemView.findViewById(R.id.Winner_Tournament_Showing_MatchType);
                TMatchMode = itemView.findViewById(R.id.Winner_Tournament_Showing_MatchMode);
                TWinnerAmount = itemView.findViewById(R.id.Winner_Tournament_WinnerAmount);
                userView = itemView.findViewById(R.id.WinnerUserList);
                Tournament_Winner_Image = itemView.findViewById(R.id.Winner_Tournament_Image);
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }

        }

        @SuppressLint("SetTextI18n")
        private void setData(String name, String date, String time, String type, String mode, String amount, ArrayList<WinnerUserModel> winnerUserList){
            try {
                TName.setText(name);
                String s = time + " " + date;
                TDate.setText(s);
                TMatchType.setText(type);
                TMatchMode.setText(mode);
                TWinnerAmount.setText(amount + "/Rs");
                winneruseradapter winneruseradapter = new winneruseradapter(winnerUserList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                userView.setLayoutManager(linearLayoutManager);
                userView.setAdapter(winneruseradapter);

                String game_name = winner_screen.game_name;
                switch (game_name) {
                    case "freefire":
                        Tournament_Winner_Image.setImageResource(R.drawable.ic_freefire_icon);
                        break;
                    case "bgmi":
                        Tournament_Winner_Image.setImageResource(R.drawable.ic_bgmi_icon);
                        break;
                    case "cod":
                        Tournament_Winner_Image.setImageResource(R.drawable.cod);
                        break;
                    case "ludoking":
                        Tournament_Winner_Image.setImageResource(R.drawable.ic_dice);
                        break;
                    default:
                        Tournament_Winner_Image.setImageResource(R.drawable.app_icon);
                        break;
                }
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }


        }
    }
}

