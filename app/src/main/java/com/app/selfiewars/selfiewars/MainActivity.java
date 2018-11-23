package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {

    ImageView ımageView;
    TextView textView;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    HomeFragment homeFragment;
    RankFragment rankFragment;
    SpinFragment spinFragment;
    StoreFragment storeFragment;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRefUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRefUser = database.getReference("Users");
        if(mAuth.getCurrentUser()!= null){
            myRefUser.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                    if(isReady){
                        define();
                        setFrameLayout();
                    }
                    else {
                        Intent i = new Intent(getApplicationContext(),AuthenticationScreen.class);
                        startActivity(i);
                        Toast.makeText(MainActivity.this, "isReadyFalse", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // intentAlma();
        }else {
            Intent i = new Intent(getApplicationContext(),AuthenticationScreen.class);
            startActivity(i);
        }

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

    public void isAuth(){

    }
    public void intentAlma(){
        Intent ıntent = new Intent(this,AuthenticationScreen.class);
        startActivity(ıntent);
    }

    @Override
    public void onBackPressed() {
    }
}
