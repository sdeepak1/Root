package com.example.root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;




import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class winner_screen extends AppCompatActivity {

    RecyclerView winner_view;
    public static List<Winner_show_model> winner_list=new ArrayList<>();
    TextView title;
    winner_show_adapter adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    public static String game_name;
    int value;
    loading_dialog loading_dialog;
    SwipeRefreshLayout refreshLayout;
    int refreshStage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_screen);

        try {
            winner_view = findViewById(R.id.winner_view);
            Intent intent = getIntent();
            value = intent.getIntExtra("id", 0);
            loading_dialog = new loading_dialog(winner_screen.this);
            refreshLayout = findViewById(R.id.winnerRefresh);

            //get value from tournament and check here which game to get data from firebase
            if (value == 1) {
                game_name = "freefire";

            } else if (value == 2) {
                game_name = "bgmi";
            } else if (value == 3) {
                game_name = "cod";
            } else if (value == 4) {
                game_name = "ludoking";
            } else {
                StyleableToast.makeText(winner_screen.this, "SomeThing Went Wrong When Get previous Screen Name", R.style.styleRedToast).show();
            }

            BannerAds();
            //toolbar start here
            Button back = findViewById(R.id.back);
            Button wallet = findViewById(R.id.wallet);
            title = findViewById(R.id.toolbar_title);
            title.setText(game_name);
            wallet.setVisibility(View.GONE);
            back.setOnClickListener(v -> back());


            //toolbar coding end here


            //call function to fetch all game winner
            fetchWinner();
            refreshLayout.setOnRefreshListener(() -> {
                refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red));
                refreshStage = 1;
                fetchWinner();
            });
        }catch (Exception e){
            StyleableToast.makeText(winner_screen.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }

    }

    private void fetchWinner(){
        try {
            loading_dialog.startLoading();

            winner_list.clear();

            myRef.child("winner").child(game_name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ArrayList<WinnerUserModel> getallwinner = new ArrayList<>();
                        for (DataSnapshot snapshot2 : snapshot1.child("Winner").getChildren()) {
                            getallwinner.add(new WinnerUserModel(Objects.requireNonNull(snapshot2.child("Rank").getValue()).toString(),
                                    Objects.requireNonNull(snapshot2.child("Winner_Name").getValue()).toString(),
                                    Objects.requireNonNull(snapshot2.child("Url").getValue()).toString(),
                                    snapshot2.getKey(),
                                    Objects.requireNonNull(snapshot2.child("Win_Amount").getValue()).toString(),
                                    Objects.requireNonNull(snapshot2.child("Game_Id").getValue()).toString()));
                        }

                        winner_list.add(new Winner_show_model(Objects.requireNonNull(snapshot1.child("Tournament_Name").getValue()).toString(),
                                Objects.requireNonNull(snapshot1.child("Date").getValue()).toString(),
                                Objects.requireNonNull(snapshot1.child("Time").getValue()).toString(),
                                Objects.requireNonNull(snapshot1.child("Match_Type").getValue()).toString(),
                                Objects.requireNonNull(snapshot1.child("Match_Mode").getValue()).toString(),
                                Objects.requireNonNull(snapshot1.child("Winner_Amount").getValue()).toString(),
                                snapshot1.getKey(),
                                getallwinner));


                    }

                    adapter = new winner_show_adapter(winner_list);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(winner_screen.this);
                    winner_view.setLayoutManager(layoutManager);
                    winner_view.setAdapter(adapter);
                    if (refreshStage == 1) {
                        refreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(winner_screen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            loading_dialog.endLoading();
        }catch (Exception e){
            StyleableToast.makeText(winner_screen.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }




    }

    private  void back(){
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }
    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


}