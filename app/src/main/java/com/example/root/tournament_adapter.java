package com.example.root;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class tournament_adapter extends RecyclerView.Adapter<tournament_adapter.viewholder> {

    private final List<tournament_model> tournament_model_list;
    int mode;




    public tournament_adapter(List<tournament_model> tournament_model_list,int mode) {
        this.tournament_model_list = tournament_model_list;
        this.mode=mode;
    }

    @NonNull
    @Override
    public tournament_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(mode==1){
             view= LayoutInflater.from(parent.getContext()).inflate(R.layout.join_tour_show_bg,parent,false);

        }else{
             view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_showimg_data,parent,false);

        }
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tournament_adapter.viewholder holder, int position) {
        holder.setData(tournament_model_list.get(position).getTournament_Name(),
                tournament_model_list.get(position).getTournament_Match_Type(),
                tournament_model_list.get(position).getTournament_Match_Mode(),
                tournament_model_list.get(position).getTournament_Date(),
                tournament_model_list.get(position).getTournament_Time(),
                tournament_model_list.get(position).getTournament_Winner_Amount(),
                tournament_model_list.get(position).getTournament_Mode_Value(),position,tournament_model_list.get(position).getJoinNotJoin());


    }

    @Override
    public int getItemCount() {
        return tournament_model_list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView Tournament_Showing_Name;
        TextView Tournament_Showing_MatchType;
        TextView Tournament_Showing_MatchMode;
        TextView DateAndTime;
        TextView Tournament_Mode_Value;
        TextView Tournament_WinnerAmount;
        TextView freeOrPaid;
        CircleImageView Tournament_Image;
        TextView symbol;





        public viewholder(@NonNull View itemView) {
            super(itemView);
            try {
                Tournament_Showing_Name = itemView.findViewById(R.id.Tournament_Showing_Name);
                Tournament_Showing_MatchType = itemView.findViewById(R.id.Tournament_Showing_MatchType);
                Tournament_Showing_MatchMode = itemView.findViewById(R.id.Tournament_Showing_MatchMode);
                DateAndTime = itemView.findViewById(R.id.DateAndTime);
                Tournament_Mode_Value = itemView.findViewById(R.id.Tournament_Mode_Value);
                Tournament_WinnerAmount = itemView.findViewById(R.id.Tournament_WinnerAmount);
                Tournament_Image = itemView.findViewById(R.id.Tournament_Image);
                freeOrPaid = itemView.findViewById(R.id.freeOrPaid);
                symbol=itemView.findViewById(R.id.symbol);
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }

        }

        @SuppressLint("SetTextI18n")
        private void setData(String TName, String MatchType, String MatchMode, String Date, String Time, int WinnerAmount, int modevalue, int position,int getJoinNotJoin){
            try {
                Tournament_Showing_Name.setText(TName);
                Tournament_Showing_MatchType.setText(MatchType);
                Tournament_Showing_MatchMode.setText(MatchMode);
                String TimeAndDate = Time + " " + Date;
                DateAndTime.setText(TimeAndDate);
                Tournament_WinnerAmount.setText(Integer.toString(WinnerAmount));

                if (tournament_model_list.get(position).getTournament_Entry_Fee() == 0) {
                    freeOrPaid.setText("*Free Tournament");
                } else {
                    freeOrPaid.setText("*Paid Tournament");
                }

                if (modevalue == 1) {
                    Tournament_Mode_Value.setVisibility(View.GONE);
                    Tournament_Mode_Value.setText("Upcoming");
                    Tournament_Mode_Value.setVisibility(View.VISIBLE);
                    if (tournament_model_list.get(position).getJoin() == 0) {
                        Tournament_Mode_Value.setText("Joined");
                    } else {
                        Tournament_Mode_Value.setText("Upcoming");
                    }
                    symbol.setVisibility(View.GONE);

                } else if (modevalue == 2) {
                    Tournament_Mode_Value.setVisibility(View.GONE);
                    Tournament_Mode_Value.setText("Live");
                    Tournament_Mode_Value.setVisibility(View.VISIBLE);
                    symbol.setVisibility(View.VISIBLE);
                    symbol.setText("*");
                    if(getJoinNotJoin==1){
                        symbol.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                    }else{
                        symbol.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.shadeRed));

                    }
                } else if (modevalue == 3) {
                    Tournament_Mode_Value.setVisibility(View.GONE);
                    Tournament_Mode_Value.setText("Complete");
                    Tournament_Mode_Value.setVisibility(View.VISIBLE);
                    symbol.setVisibility(View.VISIBLE);
                    symbol.setText("*");
                    if(getJoinNotJoin==1){
                        symbol.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                    }else{
                        symbol.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.shadeRed));

                    }

                } else {
                    Toast.makeText(itemView.getContext(), "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                }
                String name = tournament_model_list.get(position).getTournament_Game_Name();

                switch (name) {
                    case "freefire":
                        Tournament_Image.setImageResource(R.drawable.ic_freefire_icon);
                        break;
                    case "bgmi":
                        Tournament_Image.setImageResource(R.drawable.ic_bgmi_icon);
                        break;
                    case "cod":
                        Tournament_Image.setImageResource(R.drawable.cod);
                        break;
                    case "ludoking":
                        Tournament_Image.setImageResource(R.drawable.ic_dice);
                        break;
                    default:
                        Tournament_Image.setImageResource(R.drawable.app_icon);
                        break;
                }

                itemView.setOnClickListener(view -> {
                    if (tournament_model_list.get(position).getJoin() == 0) {
                        Intent intent = new Intent(itemView.getContext(), match_details.class);
                        intent.putExtra("position", position);
                        intent.putExtra("joinCheck", 0);
                        intent.putExtra("name", name);
                        itemView.getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(itemView.getContext(), match_details.class);
                        intent.putExtra("position", position);
                        intent.putExtra("joinCheck", 1);
                        intent.putExtra("name", name);
                        itemView.getContext().startActivity(intent);
                    }


                });
            }catch (Exception e){
                StyleableToast.makeText(itemView.getContext(),"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

            }

        }
    }
}
