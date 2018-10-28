package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView ımageView;
    TextView textView;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    HomeFragment homeFragment;
    RankFragment rankFragment;
    SpinFragment spinFragment;
    StoreFragment storeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        define();
        setFrameLayout();
       // intentAlma();

    }
    public void setFrameLayout(){
        homeFragment = new HomeFragment();
        rankFragment = new RankFragment();
        spinFragment = new SpinFragment();
        storeFragment = new StoreFragment();
        setFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_rank:
                        setFragment(rankFragment);
                        return true;
                    case R.id.nav_spin:
                        setFragment(spinFragment);
                        return true;
                    case R.id.nav_store:
                        setFragment(storeFragment);
                        return true;
                    default: return true;
                }
            }
        });
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    public void define() {
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        frameLayout=findViewById(R.id.main_frame);
        ımageView = findViewById(R.id.selfiewarsLogo);
        textView = findViewById(R.id.selfiewarsText);
    }

    public void intentAlma(){
        Intent ıntent = new Intent(this,AuthenticationScreen.class);
        startActivity(ıntent);
    }
}
