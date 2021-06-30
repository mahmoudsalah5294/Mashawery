package com.mnm.mashawery;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.mnm.mashawery.registration.Login;


import es.dmoral.toasty.Toasty;
import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



                    View splashScreen = new EasySplashScreen(this).withFullScreen().withTargetActivity(Login.class)
                            .withSplashTimeOut(3000).withBackgroundResource(R.drawable.splash).create();
                    setContentView(splashScreen);
                    if (!isNetworkAvaliable()) {
                        Toasty.error(this, "Please check your internet connection", Toast.LENGTH_LONG, true).show();
                    }










        }

    public boolean isNetworkAvaliable() {

        try {


            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;

            if ((connectivityManager != null)) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException e){
            return false;
        }
    }


}
