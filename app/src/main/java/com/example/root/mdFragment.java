package com.example.root;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class mdFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public mdFragment() {
        // Required empty public constructor
    }

    public static mdFragment newInstance(String param1, String param2) {
        mdFragment fragment = new mdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_md, container, false);
        //create variable
        TextView date, time, winner_amount, entry, match_type, map, game_mode, total_join_erson;
        ProgressBar progressBar;
        int position=match_details.position;
        //declare variable
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.Time);
        winner_amount = view.findViewById(R.id.winner_amount);
        entry =view.findViewById(R.id.entry);
        match_type =view.findViewById(R.id.match_type);
        map = view.findViewById(R.id.Map);
        progressBar = view.findViewById(R.id.progress);
        game_mode = view.findViewById(R.id.Game_mode);
        total_join_erson =view.findViewById(R.id.total_join_persion);

        //set data in variable
        if(match_details.checkJoin==0){
            date.setText(tournament.joinList.get(position).getTournament_Date());
            time.setText(tournament.joinList.get(position).getTournament_Time());
            winner_amount.setText(Integer.toString(tournament.joinList.get(position).getTournament_Winner_Amount()));
            entry.setText(Integer.toString(tournament.joinList.get(position).getTournament_Entry_Fee()));
            match_type.setText(tournament.joinList.get(position).getTournament_Match_Type());
            map.setText(tournament.joinList.get(position).getTournament_Match_Map());
            game_mode.setText(tournament.joinList.get(position).getTournament_Match_Mode());
            total_join_erson.setText(tournament.joinList.get(position).getAllJoinUserSize.size() +"/"+ tournament.joinList.get(position).getTournament_Limit_Person());
            progressBar.setProgress(tournament.joinList.get(position).getAllJoinUserSize.size());
            progressBar.setMax(tournament.joinList.get(position).getTournament_Limit_Person());
        }else{
            date.setText(tournament.list.get(position).getTournament_Date());
            time.setText(tournament.list.get(position).getTournament_Time());
            winner_amount.setText(Integer.toString(tournament.list.get(position).getTournament_Winner_Amount()));
            entry.setText(Integer.toString(tournament.list.get(position).getTournament_Entry_Fee()));
            match_type.setText(tournament.list.get(position).getTournament_Match_Type());
            map.setText(tournament.list.get(position).getTournament_Match_Map());
            game_mode.setText(tournament.list.get(position).getTournament_Match_Mode());
            total_join_erson.setText(tournament.list.get(position).getAllJoinUserSize.size() +"/"+ tournament.list.get(position).getTournament_Limit_Person());
            progressBar.setProgress(tournament.list.get(position).getAllJoinUserSize.size());
            progressBar.setMax(tournament.list.get(position).getTournament_Limit_Person());
        }



        return view;
    }
}