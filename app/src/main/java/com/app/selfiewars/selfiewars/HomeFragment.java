package com.app.selfiewars.selfiewars;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateTimeInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView displayNameTextView;
    private TextView ageTextView;
    private TextView rankTextView;
    private TextView scoreTextView;
    private TextView healthTextView;
    private TextView fiftyFiftyTextView;
    private TextView doubleDipTextView;
    private TextView diamondTextView;
    private static TextView righOfGameEndTimeTextView;
    private TextView usernameTextView;
    private TextView rightOfGameTextView;
    private CircleImageView profileImageView;
    private ImageView rightOfGameDiamondImageView;
    private String photoUrl;
    private FirebaseDatabase mdatabase;
    private DatabaseReference myUserRef;
    private DatabaseReference myRigtOfGameRef;
    private FirebaseAuth mAuth;
    private Picasso mPicasso;
    private Button guessitButton;
    private Wildcards wildcards;
    private Integer diamondToken;
    private Integer rightOfGame;
    private ProgressDialog mProgressDialog;
    private ImageView settingsIcon;
    private static CountDownTimer countDownTimer;
    private Boolean isStartedCountDownTimerRightOfGame;
    private static Long guessItMilisecond;
    private Long nowTimestamp;
    private Integer guessItAdsCount;
    private LottieAnimationView lottieAds;
    private static final SimpleDateFormat sdf  = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");;

    public HomeFragment() {
        // Required empty public constructor
        mPicasso = Picasso.get();
        mPicasso.setIndicatorsEnabled(true);
        rightOfGame = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        myUserRef = mdatabase.getReference("Users/" + mAuth.getUid());
        myRigtOfGameRef = mdatabase.getReference("RightOfGame/" + mAuth.getUid());
        getTimeStampControl();
        define(rootview);
        //getUserData();
        onClick_GuessIt();
        mCheckInforInServer("Users/" + mAuth.getUid());
        mGetInforInServer("RightOfGame/" + mAuth.getUid());
        settingsIconClicked();
        return rootview;
    }



    private void define(View rootview){
        displayNameTextView = rootview.findViewById(R.id.home_profile_display_name_textView);
        ageTextView = rootview.findViewById(R.id.home_profile_age_textView);
        rankTextView = rootview.findViewById(R.id.home_profile_rank_textView);
        scoreTextView = rootview.findViewById(R.id.home_profile_score_textView);
        healthTextView = rootview.findViewById(R.id.home_profile_health_textView);
        fiftyFiftyTextView = rootview.findViewById(R.id.home_profile_fiftyfifty_textView);
        doubleDipTextView = rootview.findViewById(R.id.home_profile_double_dip_textView);
        diamondTextView = rootview.findViewById(R.id.home_profile_diamond_textView);
        usernameTextView = rootview.findViewById(R.id.home_profile_username_textView);
        profileImageView = rootview.findViewById(R.id.home_profile_circleImageView);
        guessitButton = rootview.findViewById(R.id.home_profile_guessit_button);
        rightOfGameTextView = rootview.findViewById(R.id.home_profile_rightOfGame_textView);
        lottieAds = rootview.findViewById(R.id.lottieAds);
        rightOfGameDiamondImageView = rootview.findViewById(R.id.rightOfGameDiamond);
        settingsIcon = rootview.findViewById(R.id.settingsIcon);
        righOfGameEndTimeTextView = rootview.findViewById(R.id.rightofgame_endtime_textView);
        isStartedCountDownTimerRightOfGame =false;
    }
    private void getUserData(){
        myUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                if(isReady){
                    UserProperties properties = dataSnapshot.child(getResources().getString(R.string.properties)).getValue(UserProperties.class);
                    displayNameTextView.setText(properties.getDisplayName());
                    usernameTextView.setText(properties.getUserName());
                    ageTextView.setText(String.valueOf(properties.getAge()));
                    photoUrl = properties.getPhotoUrl();
                    mPicasso.load(photoUrl).resize(400,400).into(profileImageView);
                    if(dataSnapshot.hasChild(getResources().getString(R.string.token))){
                        diamondToken =dataSnapshot.child(getResources().getString(R.string.token)).child((getResources().getString(R.string.diamondValue))).getValue(Integer.class);
                        diamondTextView.setText(""+diamondToken);}
                    if(dataSnapshot.hasChild(getResources().getString(R.string.wildcards))) {
                        wildcards = dataSnapshot.child(getResources().getString(R.string.wildcards)).getValue(Wildcards.class);
                        doubleDipTextView.setText(""+wildcards.getDoubleDipValue());
                        healthTextView.setText(""+wildcards.getHealthValue());
                        fiftyFiftyTextView.setText(""+wildcards.getFiftyFiftyValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void onClick_GuessIt(){
        guessitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRigtOfGameRef.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer i = mutableData.getValue(Integer.class);
                        if(i == 0){
                            return null;
                        }else {
                            i--;
                            mutableData.setValue(i);
                            return Transaction.success(mutableData);
                        }
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if(b){
                            if(rightOfGame == 9){
                                myUserRef.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        myUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Long nowTimestamp = dataSnapshot.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).getValue(Long.class);
                                                myUserRef.child(getResources().getString(R.string.timestamp)).child("guessItMilisecond").setValue(nowTimestamp + 86400000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent i = new Intent(getActivity(),GuessItActivity.class);
                                                        startActivity(i);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });

                            }else {
                                Intent i = new Intent(getActivity(),GuessItActivity.class);
                                startActivity(i);
                            }

                        }else {
                            if(rightOfGame == 0 && guessItAdsCount > 0){
                                rightOfGameDiamondImageView.setVisibility(View.GONE);
                                lottieAds.setVisibility(View.VISIBLE);

                            }else if(rightOfGame == 0 && guessItAdsCount == 0){
                                lottieAds.setVisibility(View.GONE);
                                rightOfGameDiamondImageView.setVisibility(View.VISIBLE);

                            }
                        }

                    }
                });


            }
        });
    }
    private void onClickListener_LottieAds(){
        lottieAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Reklam Yükleniyor", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mCheckInforInServer(String child) {
        new Database().mReadDataRealTime(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(getContext());
                    mProgressDialog.setMessage(getResources().getString(R.string.loadingMessage));
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                }

                mProgressDialog.show();
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE
                try {
                    Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                    if(isReady){
                        UserProperties properties = dataSnapshot.child(getResources().getString(R.string.properties)).getValue(UserProperties.class);
                        displayNameTextView.setText(properties.getDisplayName());
                        usernameTextView.setText(properties.getUserName());
                        ageTextView.setText(String.valueOf(properties.getAge()));
                        photoUrl = properties.getPhotoUrl();
                        mPicasso.load(photoUrl).resize(400,400).into(profileImageView);
                        if(dataSnapshot.hasChild(getResources().getString(R.string.token))){
                            diamondToken =dataSnapshot.child(getResources().getString(R.string.token)).child((getResources().getString(R.string.diamondValue))).getValue(Integer.class);
                            diamondTextView.setText(""+diamondToken);}
                        if(dataSnapshot.hasChild(getResources().getString(R.string.wildcards))) {
                            wildcards = dataSnapshot.child(getResources().getString(R.string.wildcards)).getValue(Wildcards.class);
                            doubleDipTextView.setText(""+wildcards.getDoubleDipValue());
                            healthTextView.setText(""+wildcards.getHealthValue());
                            fiftyFiftyTextView.setText(""+wildcards.getFiftyFiftyValue());
                        }

                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }
    public void settingsIconClicked(){
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    private void mGetInforInServer(String child) {
        new Database().mReadDataRealTime(child, new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot data) {
                rightOfGame = data.getValue(Integer.class);
                rightOfGameTextView.setText(""+rightOfGame);

            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }
    public static String getTimeDate(long timeStamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timeStamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }

    private void startOrRefresCountTime(Long myDate){
        try {

            if(countDownTimer !=null){
                countDownTimer.cancel();
                countDownTimer = new CountDownTimer((Math.abs(guessItMilisecond-myDate)),1000) {
                    @Override
                    public void onTick(long l) {
                        righOfGameEndTimeTextView.setText(getResources().getString(R.string.Endtime) + TimeUnit.MILLISECONDS.toHours(l) +getResources().getString(R.string.hours)
                                + (TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))) + getResources().getString(R.string.minute)
                                +(TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))+getResources().getString(R.string.second) );
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();}
            else {
                countDownTimer = new CountDownTimer((Math.abs(guessItMilisecond-myDate)),1000) {
                    @Override
                    public void onTick(long l) {
                        righOfGameEndTimeTextView.setText(getResources().getString(R.string.Endtime) + TimeUnit.MILLISECONDS.toHours(l) +getResources().getString(R.string.hours)
                                + (TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))) + getResources().getString(R.string.minute)
                                +(TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))+getResources().getString(R.string.second) );
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer !=null){
            //Toast.makeText(getContext(),"Sayaç Bekletildi" , Toast.LENGTH_SHORT).show();
            countDownTimer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(countDownTimer !=null){
            getTimeStampControl();
          //  Toast.makeText(getContext(),"Sayaç Devam Ediyor" , Toast.LENGTH_SHORT).show();
        }
    }

    private void getTimeStampControl(){
        myUserRef.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRigtOfGameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Integer controlrightOfGame = dataSnapshot.getValue(Integer.class);
                        myUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(getResources().getString(R.string.nowtimestamp)).hasChild(getResources().getString(R.string.timestamp))){
                                    nowTimestamp = dataSnapshot.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).getValue(Long.class);
                                    guessItAdsCount = dataSnapshot.child(getResources().getString(R.string.timestamp)).child("guessItAdsCount").getValue(Integer.class);
                                    guessItMilisecond = (dataSnapshot.child(getResources().getString(R.string.timestamp)).child("guessItMilisecond").getValue(Long.class));
                                    if(nowTimestamp < guessItMilisecond){
                                        if(controlrightOfGame == 0){
                                            righOfGameEndTimeTextView.setVisibility(View.VISIBLE);
                                            startOrRefresCountTime(nowTimestamp);
                                            if(guessItAdsCount > 0){
                                                lottieAds.setVisibility(View.VISIBLE);
                                                rightOfGameDiamondImageView.setVisibility(View.GONE);
                                            }else {
                                                lottieAds.setVisibility(View.GONE);
                                                rightOfGameDiamondImageView.setVisibility(View.VISIBLE);
                                            }
                                        }else {
                                            righOfGameEndTimeTextView.setVisibility(View.INVISIBLE);
                                            rightOfGameDiamondImageView.setVisibility(View.GONE);
                                            lottieAds.setVisibility(View.GONE);
                                        }
                                    }if(nowTimestamp > guessItMilisecond){
                                        righOfGameEndTimeTextView.setVisibility(View.INVISIBLE);
                                        if(controlrightOfGame < 10){
                                            myRigtOfGameRef.setValue(10).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        /*
        * Şu anki zamanla kıyasla
eğer future ise
rightofgame bak
rightofgame 0 ise ekrana göster
değilse kaybet
past ise
righofgame bak 10 dan küçük ise
righofgame 10 yap
10 ise
ekranda gösterme*/

    }


}
