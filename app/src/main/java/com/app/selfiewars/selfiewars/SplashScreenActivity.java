package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class SplashScreenActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRefUser;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRefUser = database.getReference("Users");
        setup();
    }
    public void setup(){
        if(mAuth.getCurrentUser()!= null) {
            myRefUser.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                    if (isReady) {
                        startThread(true);
                    } else {
                        startThread(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            startThread(false);}
    }
    public void startThread(final Boolean isMainActivity){
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(isMainActivity){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    }else {
                        Intent i = new Intent(getApplicationContext(), AuthenticationScreen.class);
                        startActivity(i);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup();
    }
}
