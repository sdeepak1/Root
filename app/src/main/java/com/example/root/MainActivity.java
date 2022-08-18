package com.example.root;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity {

    SliderView sliderView;
    int[] rag_image={R.drawable.rag_game,R.drawable.rag_ff,R.drawable.rag_bgmi,
            R.drawable.rag_ludoking,R.drawable.rag_codd,R.drawable.rag_fff,R.drawable.rag_cod};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sliderView=findViewById(R.id.imageSlider);
        SlideAdapter slideAdapter=new SlideAdapter(rag_image);
        sliderView.setSliderAdapter(slideAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
    }

    //if user not login to jump login screen first
    public void Change(View view){
        Intent intent=new Intent(MainActivity.this,login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}