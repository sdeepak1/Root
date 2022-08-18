package com.example.root;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class joinedcontest extends AppCompatActivity {
    RecyclerView rc_joined;
    tournament_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinedcontest);



        BannerAds();
        //toolbar start here
        Button back = findViewById(R.id.back);
        Button wallet = findViewById(R.id.wallet);
        TextView title = findViewById(R.id.toolbar_title);
        wallet.setVisibility(View.GONE);



        back.setOnClickListener(v -> back());
        //toolbar coding end here

        rc_joined=findViewById(R.id.joinedMatch);
        title.setText(tournament.joinList.get(0).getTournament_Game_Name());
        adapter=new tournament_adapter(tournament.joinList,2);
        LinearLayoutManager layoutManager=new LinearLayoutManager(joinedcontest.this);
        rc_joined.setLayoutManager(layoutManager);
        rc_joined.setAdapter(adapter);

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