package com.example.root;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loading_dialog {

    private final Activity activity;
        private AlertDialog dialog;

        loading_dialog(Activity myActivity){
            this.activity=myActivity;
        }

        @SuppressLint("InflateParams")
        //function for start loading
        void startLoading(){
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);

            LayoutInflater inflater=activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.loading_dialog,null));
            builder.setCancelable(false);

            dialog=builder.create();
            dialog.show();
        }
        //function for end loading
        void endLoading(){
            dialog.dismiss();
        }
}
