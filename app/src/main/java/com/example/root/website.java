package com.example.root;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class website extends AppCompatActivity {

    WebView webView;
    loading_dialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        webView=findViewById(R.id.webview);
        loading_dialog=new loading_dialog(website.this);

        //toolbar start here
        Button back = findViewById(R.id.back);
        Button wallet = findViewById(R.id.wallet);

        TextView title = findViewById(R.id.toolbar_title);
        String name=getIntent().getStringExtra("name");
        title.setText(name);

        wallet.setVisibility(View.GONE);


        back.setOnClickListener(v -> back());
        //toolbar coding end here
        loading_dialog.startLoading();


        String url=getIntent().getStringExtra("url");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        loading_dialog.endLoading();

        BannerAds();

    }


    private  void back(){

        finish();
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            return;
        }
        back();
    }

    public void BannerAds(){
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(String.valueOf(R.string.banner_Ads));
    }
}