package com.example.root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

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
import java.util.Objects;

public class notification extends AppCompatActivity {
    int value;
    SwipeRefreshLayout refreshLayout;
    int refreshStage=0;
    String game_name;
    ArrayList<Notification_Model> list=new ArrayList<>();
    Notification_Adapter adapter;
    RecyclerView NotiView;
    TextView noNotification;
    loading_dialog loading_dialog;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        refreshLayout=findViewById(R.id.notiSwipe);

        noNotification=findViewById(R.id.noNotification);
        loading_dialog=new loading_dialog(notification.this);
        NotiView=findViewById(R.id.NotiView);

        Intent intent=getIntent();
        value=intent.getIntExtra("id",0);
        if(value==1){
            game_name="freefire";

        }else if(value==2){
            game_name="bgmi";
        }else if (value==3){
            game_name="cod";
        }else if (value==4){
            game_name="ludoking";
        }else{
            StyleableToast.makeText(notification.this,"SomeThing Went Wrong When Get previous Screen Name",R.style.styleRedToast).show();
        }

        BannerAds();




        //toolbar start here
        Button back = findViewById(R.id.back);
        Button wallet = findViewById(R.id.wallet);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText(game_name);


        wallet.setVisibility(View.GONE);

        back.setOnClickListener(v -> back());
        //toolbar coding end here

        fetch();
        //for refresh layout
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red));
            refreshStage=1;
           fetch();
        });

        }

    private  void back(){
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }




    //fetch all notification from database
    public void fetch(){
        try {
            loading_dialog.startLoading();
            list.clear();

            myRef.child("notification").child(game_name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        list.add(new Notification_Model(snapshot1.getKey(),
                                Objects.requireNonNull(snapshot1.child("Message").getValue()).toString(),
                                Objects.requireNonNull(snapshot1.child("Date").getValue()).toString()));
                    }

                    adapter = new Notification_Adapter(list);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(notification.this);
                    NotiView.setLayoutManager(linearLayoutManager);
                    NotiView.setAdapter(adapter);

                    if (list.isEmpty()) {
                        noNotification.setVisibility(View.VISIBLE);
                    } else {
                        noNotification.setVisibility(View.GONE);
                    }
                    loading_dialog.endLoading();
                    if (refreshStage == 1) {
                        refreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    loading_dialog.endLoading();
                    StyleableToast.makeText(notification.this, error.getMessage(), R.style.styleRedToast).show();
                }
            });
        }catch (Exception e){
            StyleableToast.makeText(notification.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }

    }

    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


}