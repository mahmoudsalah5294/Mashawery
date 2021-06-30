package com.mnm.mashawery;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.mnm.mashawery.ui.history.HistoryFragment;

import com.mnm.mashawery.ui.logout.LogoutFragment;

import com.mnm.mashawery.ui.upcomming.UpcomingFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    int counter;
    UpcomingFragment upcomingFragment;
    HistoryFragment historyFragment;
    LogoutFragment logoutFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;

        BottomNavigationView navView = findViewById(R.id.nav_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.cont,new UpcomingFragment(),"Home").addToBackStack(null).commit();


        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.cont,new UpcomingFragment(),"Home").addToBackStack(null).commit();
                return true;
            case R.id.navigation_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.cont,new HistoryFragment(),"History").addToBackStack(null).commit();
                return true;
            case R.id.navigation_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.cont,new LogoutFragment(),"Logout").addToBackStack(null).commit();
                return true;

        }


        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}