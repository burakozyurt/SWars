package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class SplashScreenActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRefUser;
    private ViewPager viewPagerOnBoard;
    private LinearLayout dotsLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button previousBtn;
    private Button nextBtn;
    private ImageView selfiewarsImageView;
    private LottieAnimationView selfiewarsLottie;
    private int mCurrentPage;
    private Animation smalltoBig;

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
        selfiewarsImageView = findViewById(R.id.selfiewarsLogo);
        selfiewarsLottie = findViewById(R.id.selfiewarsText);
        viewPagerOnBoard = findViewById(R.id.slide_viewPager);
        dotsLayout = findViewById(R.id.slide_linearLayout);
        nextBtn = findViewById(R.id.slide_NextButton);
        previousBtn = findViewById(R.id.slide_previousButton);
        setup();
    }
    public void addDotsIndicator(int position){
        mDots =  new TextView[4];
        dotsLayout.removeAllViews();
        for (int i = 0; i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.textcolorblack));
            dotsLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextSize(40);
            if(position == 1){
                mDots[position].setTextColor(getResources().getColor(R.color.slide1color));
            }else if(position == 2){
                mDots[position].setTextColor(getResources().getColor(R.color.slide2color));
            }else if(position == 3){
                mDots[position].setTextColor(getResources().getColor(R.color.slide3color));
            }else {
                mDots[position].setTextColor(getResources().getColor(R.color.textcolorblack));
            }

        }
    }
    public void createSharedPreference(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isChecked",true); //boolean değer ekleniyor
        editor.commit(); //Kayıt

    }
    public void setupViewPager(){
        sliderAdapter = new SliderAdapter(SplashScreenActivity.this);
        viewPagerOnBoard.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        viewPagerOnBoard.setVisibility(View.VISIBLE);
        dotsLayout.setVisibility(View.VISIBLE);
        smalltoBig = AnimationUtils.loadAnimation(this,R.anim.updownmove);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                    addDotsIndicator(i);
                    /*if (viewPagerOnBoard.getAnimation() != null){
                        viewPagerOnBoard.getAnimation().cancel();
                    }
                    viewPagerOnBoard.startAnimation(smalltoBig);*/
                    mCurrentPage = i;
                    if(i==0){
                        /*if (viewPagerOnBoard.getAnimation() != null){
                            viewPagerOnBoard.getAnimation().reset();
                            viewPagerOnBoard.getAnimation().cancel();
                        }*/
                        nextBtn.setEnabled(true);
                        previousBtn.setEnabled(false);
                        previousBtn.setVisibility(View.GONE);
                        nextBtn.setVisibility(View.VISIBLE);
                        nextBtn.setText("İleri");
                        previousBtn.setText("");
                    }else if(i == mDots.length-1){
                        previousBtn.setEnabled(true);
                        previousBtn.setVisibility(View.VISIBLE);
                        nextBtn.setText("Bitir");
                        previousBtn.setText("Geri");
                    }else {
                        nextBtn.setEnabled(true);
                        previousBtn.setEnabled(true);
                        nextBtn.setText("İleri");
                        previousBtn.setText("Geri");
                        previousBtn.setVisibility(View.VISIBLE);
                        nextBtn.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };
        viewPagerOnBoard.addOnPageChangeListener(onPageChangeListener);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentPage != mDots.length - 1){
                    viewPagerOnBoard.setCurrentItem(mCurrentPage + 1);
                }else {
                    createSharedPreference();
                    Intent intent = new Intent(getApplicationContext(),AuthenticationScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
        if(mCurrentPage==0){
            nextBtn.setEnabled(true);
            previousBtn.setEnabled(false);
            previousBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setText("İleri");
            previousBtn.setText("");
        }
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentPage != 0){
                    viewPagerOnBoard.setCurrentItem(mCurrentPage - 1);
                }
            }
        });
    }
    public void setup(){
        if(mAuth.getCurrentUser()!= null) {
            myRefUser.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                    if (dataSnapshot.exists() && isReady) {
                        startThread(true);
                    } else{
                        startThread(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            startThread(false);
        }
    }
    public void startThread(final Boolean isMainActivity){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final Boolean savedChecked = sharedPref.getBoolean("isChecked",false);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    if (savedChecked)
                    sleep(1000);
                    else {
                        sleep(1500);
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(savedChecked == true){
                                if(isMainActivity){
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                }else {
                                    Intent i = new Intent(getApplicationContext(), AuthenticationScreen.class);
                                    startActivity(i);
                                }
                            }else {
                                selfiewarsImageView.setVisibility(View.GONE);
                                selfiewarsLottie.setVisibility(View.GONE);
                                setupViewPager();
                            }
                        }
                    });


                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setup();
    }
}
