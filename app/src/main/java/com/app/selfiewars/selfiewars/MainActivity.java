package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

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
    private DatabaseReference myRefUser;
    public static UserProperties userProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRefUser = database.getReference("Users");
        if (mAuth.getCurrentUser() != null) {
            myRefUser.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                    if (isReady) {
                        define();
                        setFrameLayout();
                        //getUserPropertiesDataFromFirebase();
                    } else {
                        Intent i = new Intent(getApplicationContext(), AuthenticationScreen.class);
                        startActivity(i);
                        Toast.makeText(MainActivity.this, "isReadyFalse", Toast.LENGTH_SHORT).show();
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

    public void getUserPropertiesDataFromFirebase() {
        myRefUser.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProperties = dataSnapshot.child(getResources().getString(R.string.properties)).getValue(UserProperties.class);
                Log.d("BugControl", "Data Mainactivity e çekildi // Ad: " + userProperties.getDisplayName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void define() {
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        frameLayout = findViewById(R.id.main_frame);
        ımageView = findViewById(R.id.selfiewarsLogo);
        textView = findViewById(R.id.selfiewarsText);
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
    static void showPopupProductInfo(Integer productImageView, String productTitle, Context context){
        final Dialog mydialog = new Dialog(context);
        mydialog.setContentView(R.layout.popup_product_info);
        mydialog.getWindow().getAttributes().windowAnimations = R.style.UptoDown;
        ImageView productimageView;
        TextView productTitleView;

        productimageView = mydialog.findViewById(R.id.popup_product_Info_Image);
        productTitleView = mydialog.findViewById(R.id.popup_product_Info_TitleTextView);

        if(productImageView != null)
            productimageView.setImageResource(productImageView);

        if(productTitle != null)
            productTitleView.setText(productTitle);

        mydialog.setCanceledOnTouchOutside(true);
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
        finish();
        //Toast.makeText(this, "MainACtivity Stoped", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "MainACtivity Başlatıldı", Toast.LENGTH_SHORT).show();
    }
}
