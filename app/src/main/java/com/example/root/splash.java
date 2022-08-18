package com.example.root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class splash extends AppCompatActivity {

    TextView version;
    String code;

    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    File file;


    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        version=findViewById(R.id.version);








        version.setText("Version: "+getVersionCode());
        Check();
    }


    //check internet connection and android app version for update
    public void Check() {
        try {

            ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = manager.getActiveNetworkInfo();

            if (null != network) {


                if (network.getType() == ConnectivityManager.TYPE_WIFI || network.getType() == ConnectivityManager.TYPE_MOBILE) {

                    int version_code = getVersionCode();

                    FirebaseDatabase.getInstance().getReference().child("AppData").child("Update").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            code = Objects.requireNonNull(snapshot.child("version").getValue()).toString();
                            //load mobile ads
                            MobileAds.initialize(splash.this, initializationStatus -> {
                            });
                            if (Integer.parseInt(code) > version_code) {
                                show();
                            } else {
                                //if user already login so jump direct to tournament screen
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    Intent intent = new Intent(splash.this, tournament.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent1 = new Intent(splash.this, MainActivity.class);
                                    startActivity(intent1);
                                    finish();
                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(splash.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }else {
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);


                    dialog1.setMessage("Please Enable Your Internet");
                    dialog1.setCancelable(false);

                    dialog1.setPositiveButton("Retry", (dialog11, which) -> {


                        dialog11.cancel();
                        Check();
                    }).setNegativeButton("Exit App", (dialog, which) -> finish());
                    dialog1.create();
                    dialog1.show();


                }


            }
        catch (Exception e){
            StyleableToast.makeText(splash.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }
    }



    public int  getVersionCode() {
        PackageInfo packageInfo=null;
        try {
            packageInfo=getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("My Log","NameNotFoundException"+e.getMessage());
        }
        return Objects.requireNonNull(packageInfo).versionCode;

    }




    public void show() {
        try {
            final Dialog dialog = new Dialog(splash.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.update_layout);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.BOTTOM);


            Button update = dialog.findViewById(R.id.btn_okay);
            Button cancel = dialog.findViewById(R.id.btn_cancel);
            dialog.show();

            update.setOnClickListener(view -> {
                dialog.dismiss();
                ActivityCompat.requestPermissions(splash.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!getPackageManager().canRequestPackageInstalls()) {
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), 1234);
                    } else {
                        StyleableToast.makeText(splash.this,"something went wrong",R.style.styleRedToast).show();
                    }
                }
                downlaod();
            });

            cancel.setOnClickListener(view -> finish());

        }catch (Exception e){
            StyleableToast.makeText(splash.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }





    }
    /**
     * Showing Dialog
     * */

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == progress_bar_type) {
            pDialog = new ProgressDialog(this);
            pDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
            pDialog.setTitle("Downloading Root.apk. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
            return pDialog;
        }
        return null;
    }

    public void downlaod(){

        file=new File(getExternalFilesDir(null),"root.apk");
        // File url to download
        String file_url = "https://firebasestorage.googleapis.com/v0/b/ebook-e048e.appspot.com/o/root.apk?alt=media&token=d10c401a-6d6a-4b89-b391-90daa66b7fa6";
        new DownloadFileFromURL().execute(file_url);

    }
    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                FileOutputStream output=new FileOutputStream(file);

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            Toast.makeText(splash.this, "Downloading successfully", Toast.LENGTH_SHORT).show();
            install();
        }

    }

    public void install(){
       file=new File(getExternalFilesDir(null),"root.apk");
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(getApplicationContext(), new File(String.valueOf(file))), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
               StyleableToast.makeText(splash.this,e.getMessage(),R.style.styleRedToast).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"installing",Toast.LENGTH_LONG).show();
        }
    }
    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }

    }
}
