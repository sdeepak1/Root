package com.example.root;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class live_complete_screen extends AppCompatActivity {

    //All Data declare here
    TabLayout live_complete;
    RecyclerView rc_live;
    tournament_adapter adapter;
    int id;  //for get id of tournament tabs to check which game
    String game_name;//get game name
    ShimmerFrameLayout shimmerFrameLayout;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    SwipeRefreshLayout refreshLayout;
    int refreshStage=0;
    int tab_id;







    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_complete_screen);

        //call initialization method
        init();
        //toolbar start here
        Button back = findViewById(R.id.back);
        Button wallet = findViewById(R.id.wallet);
        TextView title = findViewById(R.id.toolbar_title);
        wallet.setVisibility(View.GONE);



        back.setOnClickListener(v -> back());





        //toolbar coding end here


        BannerAds();


        id=getIntent().getIntExtra("id",0);
        //get value from tournament and check here which game to get data from firebase
        if(id==1){
            game_name="freefire";

        }else if(id==2){
            game_name="bgmi";
        }else if (id==3){
            game_name="cod";
        }else if (id==4){
            game_name="ludoking";
        }else{
            StyleableToast.makeText(live_complete_screen.this,"SomeThing Went Wrong When Get previous Screen Name",R.style.styleRedToast).show();
        }

        title.setText(game_name);



        //tab layout coding start here
        live_complete.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_id=tab.getId();
                switch(tab.getId()){
                    case 1:
                    case 2:
                        live();
                        break;


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //tab layout coding  end here

        live();
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red));
            refreshStage=1;
            switch(tab_id){
                case 1:
                case 2:
                    live();
                    break;
            }

        });

    }

    //connect all data type with there ids
    public void init(){
        tab_id=1;
        refreshLayout=findViewById(R.id.liveRefresh);
        rc_live=findViewById(R.id.livematch);
        live_complete=findViewById(R.id.live_compleate);
        live_complete.addTab(live_complete.newTab().setText("Live Matches").setId(1).setIcon(R.drawable.live));
        live_complete.addTab(live_complete.newTab().setText("Complete Matches").setId(2).setIcon(R.drawable.winner));
        live_complete.setTabGravity(TabLayout.GRAVITY_FILL);
        shimmerFrameLayout=findViewById(R.id.liveShimmer);

    }

    //get live matches data from firebase
    private void live() {
        rc_live.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        tournament.list.clear();
        String title="live";
        switch (tab_id){
            case 1:
                title="live";
                break;
            case 2:
                title="complete";
                break;

        }
        final Boolean[] check = {false};
        //Read tournament details data from firebase
        myRef.child("tournament").child(game_name).child(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    int joinNotJoin=0;
                    ArrayList<String> getAllUser=new ArrayList<>();
                    List<showAmountModel> models=new ArrayList<>();
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.child("Tournament_join_Person").getChildren()){
                        getAllUser.add(dataSnapshot2.getKey());
                        if (Objects.equals(dataSnapshot2.getKey(),tournament.userList.get(0).getUserId())) {
                            check[0] = true;
                        }

                    }
                    for(DataSnapshot snapshot1:dataSnapshot1.child("winners").getChildren()){
                        models.add(new showAmountModel(snapshot1.getKey(), Objects.requireNonNull(snapshot1.child("amount").getValue()).toString()));
                    }
                    if(check[0]){
                        joinNotJoin=1;
                    }                    tournament.list.add(new tournament_model(Objects.requireNonNull(dataSnapshot1.child("Tournament_Name").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot1.child("Tournament_ID").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot1.child("Tournament_Game_Name").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot1.child("Tournament_Date").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot1.child("Tournament_Time").getValue()).toString(),
                            Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Winner_Amount").getValue()).toString()),
                            Integer.parseInt( Objects.requireNonNull(dataSnapshot1.child("Tournament_Entry_Fee").getValue()).toString()),
                            Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Type").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Mode").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Map").getValue()).toString(),
                            Integer.parseInt( Objects.requireNonNull(dataSnapshot1.child("Tournament_Limit_Person").getValue()).toString()),
                            Integer.parseInt( Objects.requireNonNull(dataSnapshot1.child("Tournament_Mode_Value").getValue()).toString()),
                            getAllUser,1,"null","null",models,joinNotJoin

                    ));


                }
                adapter=new tournament_adapter(tournament.list,2);
                LinearLayoutManager layoutManager=new LinearLayoutManager(live_complete_screen.this);
                rc_live.setLayoutManager(layoutManager);
                rc_live.setAdapter(adapter);
                visible();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                StyleableToast.makeText(live_complete_screen.this,"SomeThing Went Wrong When Fetch the data from firebase",R.style.styleRedToast).show();

            }
        });

    }

    private  void back(){
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }



    //here visible or Invisible recycler of shimmer layout
    private void visible(){

        if(tournament.list.isEmpty()){
            StyleableToast.makeText(live_complete_screen.this,"Currently we have no any live matches",R.style.styleRedToast).show();

        }
        if(refreshStage==1){
            refreshLayout.setRefreshing(false);
        }
        rc_live.setVisibility(View.VISIBLE);
        shimmerFrameLayout.setVisibility(View.GONE);

    }

    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}