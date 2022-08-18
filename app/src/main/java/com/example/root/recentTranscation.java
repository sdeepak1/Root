package com.example.root;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class recentTranscation extends AppCompatActivity {

    RecyclerView HistoryView;
    HistoryAdapter adapter;
    List<historyModel> historyModelList=new ArrayList<>();
    TextView noHistory;

    FirebaseFirestore firestore;
    loading_dialog loading_dialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_transcation);


        HistoryView=findViewById(R.id.HistoryView);
        noHistory=findViewById(R.id.noHistory);
        loading_dialog=new loading_dialog(recentTranscation.this);



        BannerAds();

        //toolbar start here
        Button back = findViewById(R.id.back);
        Button wallet = findViewById(R.id.wallet);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("History");

        wallet.setVisibility(View.GONE);




        back.setOnClickListener(v -> back());
        //toolbar coding end here

        loading_dialog.startLoading();
        fetch();
    }

    //fetcj all data from firebase
    public void fetch(){
        try {
            historyModelList.clear();
            firestore = FirebaseFirestore.getInstance();

            firestore.collection("Users").document(tournament.userList.get(0).getUserId())
                    .collection("TRHistory").addSnapshotListener((value, error) -> {
                for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(Objects.requireNonNull(value).getDocuments())) {
                    historyModelList.add(new historyModel(documentSnapshot.getString("Money"),
                            documentSnapshot.getString("Msg"),
                            documentSnapshot.getId(),
                            documentSnapshot.getString("Date"),
                            documentSnapshot.getString("Time"),
                            false));
                }
                adapter = new HistoryAdapter(historyModelList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recentTranscation.this);
                HistoryView.setLayoutManager(linearLayoutManager);
                HistoryView.setAdapter(adapter);
                if (historyModelList.isEmpty()) {
                    noHistory.setVisibility(View.VISIBLE);
                } else {
                    noHistory.setVisibility(View.GONE);
                }
                loading_dialog.endLoading();

            });
        }catch (Exception e){
            StyleableToast.makeText(recentTranscation.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }



    }

    private  void back(){
        Intent intent=new Intent(recentTranscation.this,wallet_screen.class);
        startActivity(intent);
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