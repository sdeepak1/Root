package com.example.root;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class winnerFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public winnerFragment() {
        // Required empty public constructor
    }


    public static winnerFragment newInstance(String param1, String param2) {
        winnerFragment fragment = new winnerFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_winner, container, false);

        RecyclerView winning;
        List<showAmountModel> showAmountModels=new ArrayList<>();

        winneramountshowadapter winneramountshowadapter;

        if(match_details.checkJoin==0){
           winneramountshowadapter=new winneramountshowadapter(tournament.joinList.get(match_details.position).getAllWinnerAmount);

        }else{
            winneramountshowadapter=new winneramountshowadapter(tournament.list.get(match_details.position).getAllWinnerAmount);

        }
        winning=view.findViewById(R.id.showWinning);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        winning.setLayoutManager(layoutManager);
        winning.setAdapter(winneramountshowadapter);


        return view;
    }
}