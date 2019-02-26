package com.app.selfiewars.selfiewars;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView ımageView;
    private TextView textView;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private RankFragment rankFragment;
    private SpinFragment spinFragment;
    private StoreFragment storeFragment;
    private FragmentManager fragmentManager;
    private Fragment active;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    public  static DatabaseReference myRefUser;
    public  static DatabaseReference myRefNodeUser;
    public static DatabaseReference myRefEndTime;
    public  static  DatabaseReference myRigtOfGameRef;
    public  static DatabaseReference myScoreRef;
    //public  static Long nowTimeStamp;
    public  static Long endTimeStamp;
    public  static Long rightOFDailyValue;
    public  static Integer myRigtOfGame;
    public  static Integer UserScore = 0;
    public static Long guessItMilisecond;
    public static Integer guessItAdsCount;
    public  static Wildcards wildcards;
    public  static Integer diamondValue;
    public  static boolean accountState;
    public  static DataSnapshot userDataSnapShot;
    public  static DataSnapshot rightOfSpinDataSnapShot;
    public  static DataSnapshot myRightOfGameDataSnapShot;
    public static UserProperties userProperties;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private long cacheExpiration = 0;
    public static String diamondPrice10;
    public static String diamondPrice20;
    public static String diamondPrice50;
    public static String diamondPrice100;
    public static String diamondPrice250;
    public static String diamondPrice500;
    public static boolean remoteDiamondLoad = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRefUser = database.getReference("Users/"+mAuth.getUid());
        myRefNodeUser = database.getReference("Users");
        myRefEndTime = database.getReference("EndTime/timestamp");
        myRigtOfGameRef = database.getReference("RightOfGame/" + mAuth.getUid());
        myScoreRef = database.getReference(getResources().getString(R.string.Scores));
        myScoreRef.keepSynced(true);
        myRefEndTime.keepSynced(true);
        myRefUser.keepSynced(true);
        myRigtOfGameRef.keepSynced(true);
        myRefUser.keepSynced(true);

        if (mAuth.getCurrentUser() != null) {
            myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                    if (isReady) {
                        getRemoteConfig();
                        define();
                        setFrameLayout();
                        myRefEndTime.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("DbWorks","Retrieving: EndTimeStamp");
                                endTimeStamp = dataSnapshot.getValue(Long.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myRigtOfGameRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("DbWorks","Retrieving: Rightofgame ");
                                myRightOfGameDataSnapShot = dataSnapshot;
                                myRigtOfGame = dataSnapshot.getValue(Integer.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myRefUser.child("accountstate").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("DbWorks","Retrieving: AccountState");
                                accountState = dataSnapshot.child("isReady").getValue(Boolean.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myRefUser.child("rightofdailyaward").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("DbWorks","Retrieving: RightOfDailyAward");
                                rightOFDailyValue = dataSnapshot.child("dailyAwardValue").getValue(Long.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myRefUser.child("rightofspin").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("DbWorks","Retrieving: RightOfSpin");
                                rightOfSpinDataSnapShot = dataSnapshot;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myRefUser.child("timestamp").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("DbWorks","Retrieving: GuessIt Timestamp");

                                guessItAdsCount = dataSnapshot.child("guessItAdsCount").getValue(Integer.class);
                                    guessItMilisecond = dataSnapshot.child("guessItMilisecond").getValue(Long.class);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, mAuth.getUid());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mAuth.getCurrentUser().getDisplayName());
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    }else {
                        Intent i = new Intent(getApplicationContext(), AuthenticationScreen.class);
                        startActivity(i);
                       // Toast.makeText(MainActivity.this, "isReadyFalse", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // intentAlma();
        } else {
            Intent i = new Intent(getApplicationContext(), AuthenticationScreen.class);
            startActivity(i);
        }

    }

    private void setFrameLayout() {
        homeFragment = new HomeFragment();
        rankFragment = new RankFragment();
        spinFragment = new SpinFragment();
        storeFragment = new StoreFragment();
        frameLayout.removeAllViews();
        active = homeFragment;
        fragmentManager.beginTransaction().add(R.id.main_frame, homeFragment,"home").addToBackStack(null).commit();
        setFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        if(fragmentManager.findFragmentByTag("home") == homeFragment){
                            setFragment(homeFragment);
                        }else {
                            fragmentManager.beginTransaction().add(R.id.main_frame, homeFragment,"home").addToBackStack(null).commit();
                            setFragment(homeFragment);
                        }
                        return true;
                    case R.id.nav_rank:
                        if(fragmentManager.findFragmentByTag("rank") == rankFragment){
                            setFragment(rankFragment);
                        }else {
                            fragmentManager.beginTransaction().add(R.id.main_frame, rankFragment,"rank").hide(rankFragment).addToBackStack(null).commit();
                            setFragment(rankFragment);
                        }
                        return true;
                    case R.id.nav_spin:
                        if(fragmentManager.findFragmentByTag("spin") == spinFragment){
                            setFragment(spinFragment);
                        }else {
                            fragmentManager.beginTransaction().add(R.id.main_frame, spinFragment,"spin").hide(spinFragment).addToBackStack(null).commit();
                            setFragment(spinFragment);
                        }
                        return true;
                    case R.id.nav_store:
                        if(fragmentManager.findFragmentByTag("store") == storeFragment){
                            setFragment(storeFragment);
                        }else {
                            fragmentManager.beginTransaction().add(R.id.main_frame, storeFragment,"store").hide(storeFragment).addToBackStack(null).commit();
                            setFragment(storeFragment);
                        }
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        /*if(active == spinFragment){
            fragmentManager.beginTransaction().remove(spinFragment).show(fragment).commit();
            active = fragment;
        }else if(fragment == spinFragment){
            fragmentManager.beginTransaction().hide(active).add(R.id.main_frame,spinFragment,"spin").commit();
            active = fragment;
        }else {

        }*/

        fragmentManager.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;

       /* FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();*/
    }
    public void define() {
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        frameLayout = findViewById(R.id.main_frame);
        ımageView = findViewById(R.id.selfiewarsLogo);
        textView = findViewById(R.id.selfiewarsText);
    }
    private void getRemoteConfig(){
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetch(0).addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                   // Toast.makeText(MainActivity.this,"Fetch is Succesfully",Toast.LENGTH_SHORT).show();
                    firebaseRemoteConfig.activateFetched();
                }else
                {
                    //Toast.makeText(MainActivity.this,"Fetch is Failed",Toast.LENGTH_SHORT).show();
                }
                setDiamondValue();
            }
        });
    }
    private void setDiamondValue() {
        diamondPrice10 = firebaseRemoteConfig.getString("diamondPrice10");
        diamondPrice20 = firebaseRemoteConfig.getString("diamondPrice20");
        diamondPrice50 = firebaseRemoteConfig.getString("diamondPrice50");
        diamondPrice100 = firebaseRemoteConfig.getString("diamondPrice100");
        diamondPrice250 = firebaseRemoteConfig.getString("diamondPrice250");
        diamondPrice500 = firebaseRemoteConfig.getString("diamondPrice500");
        remoteDiamondLoad = true;

    }
        public void isAuth() {

    }

    public void intentAlma() {
        Intent ıntent = new Intent(this, AuthenticationScreen.class);
        startActivity(ıntent);
    }

    @Override
    public void onBackPressed() {
    }

    static void showPopUpInfo(Integer ImageViewDraw, String Title, String Description, Context context) {
        final Dialog mydialog = new Dialog(context);
        mydialog.setContentView(R.layout.popup_info);
        mydialog.getWindow().getAttributes().windowAnimations = R.style.UptoDown;
        ImageView ımageView;
        TextView titleView;
        TextView descriptionView;
        Button btnOk;

        ımageView = mydialog.findViewById(R.id.popupInfo_Image);
        titleView = mydialog.findViewById(R.id.popupInfo_TitleTextView);
        descriptionView = mydialog.findViewById(R.id.popupInfo_descriptionTextView);
        btnOk = mydialog.findViewById(R.id.popupInfo_BtnOkey);

        if (ImageViewDraw !=null){
            ımageView.setImageResource(ImageViewDraw);
            ımageView.setVisibility(View.VISIBLE);
        }
        else ımageView.setVisibility(View.GONE);
        if (Title !=null)
            titleView.setText(Title);

        if(Description !=null)
            descriptionView.setText(Description);
        mydialog.setCanceledOnTouchOutside(false);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });
    }
    static void showPopupProductInfo(final String  photoUrl, String productTitle, final Context context){
        final Dialog mydialog = new Dialog(context);
        mydialog.setContentView(R.layout.popup_product_info);
        mydialog.getWindow().getAttributes().windowAnimations = R.style.UptoDown;
        final ImageView productimageView;
        productimageView = mydialog.findViewById(R.id.popup_product_Info_Image);
        if(photoUrl !=null){
            Picasso.with(context).load(photoUrl).resize(500, 500).networkPolicy(NetworkPolicy.OFFLINE).into(productimageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(photoUrl).resize(500, 500).networkPolicy(NetworkPolicy.OFFLINE).into(productimageView);
                }
            });
        }
        mydialog.setCanceledOnTouchOutside(true);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();


    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        //Toast.makeText(this, "MainACtivity Destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Toast.makeText(this, "MainACtivity Stoped", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "MainACtivity Başlatıldı", Toast.LENGTH_SHORT).show();
    }


}
