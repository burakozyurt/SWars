package com.app.selfiewars.selfiewars;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

import java.text.DateFormat;
import java.util.*;
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
    private DatabaseReference myAnnouncementRef;
    private DatabaseReference myScoreRef;
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
    private AnnouncementViewPageAdapter announcementViewPageAdapter;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Handler updateHandler;
    private Timer timer;
    private LinearLayout announcementLinearLayout;
    private boolean isClickGuessitButton = false;

    public HomeFragment() {
        // Required empty public constructor
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
        myAnnouncementRef = mdatabase.getReference("Announcement");
        myScoreRef = mdatabase.getReference(getResources().getString(R.string.Scores));
        updateHandler=new Handler();
        getTimeStampControl();
        define(rootview);
        setViewPager();
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
        viewPager = rootview.findViewById(R.id.announcementViewPager);
        circleIndicator = rootview.findViewById(R.id.announcement_indicator);
        announcementLinearLayout = rootview.findViewById(R.id.linearLayoutAnnouncement);
        isStartedCountDownTimerRightOfGame =false;
    }
    private void setViewPager(){
        announcementViewPageAdapter =  new AnnouncementViewPageAdapter(getContext());
        announcementViewPageAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        viewPager.setAdapter(announcementViewPageAdapter);
        circleIndicator.setViewPager(viewPager);
        timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
                updateHandler.post(new Runnable() {
                    public void run() {
                        if(viewPager.getCurrentItem() == 0){
                            viewPager.setCurrentItem(1);
                        }else if(viewPager.getCurrentItem() == 1){
                            viewPager.setCurrentItem(2);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 5000, 5000);
    }

    private void setViewPageItemClicked(){
        announcementLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Instagrama yönlendir.", Toast.LENGTH_SHORT).show();
                if(viewPager.getCurrentItem() == 2){
                    Toast.makeText(getContext(), "Instagrama yönlendir.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                if(!isClickGuessitButton){
                    isClickGuessitButton = true;
                    myUserRef.child("nowtimestamp").child(getResources().getString(R.string.timestamp)).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            myUserRef.child("nowtimestamp").child(getResources().getString(R.string.timestamp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    nowTimestamp = dataSnapshot.getValue(Long.class);
                                    FirebaseDatabase.getInstance().getReference("EndTime").child("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue(Long.class) > nowTimestamp ){
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
                                                                                        isClickGuessitButton = false;
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
                                                                isClickGuessitButton = false;
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
                                            }else {
                                                MainActivity.showPopUpInfo(null,"Hafta yenilenmemiştir!!",
                                                        "Sistem bu haftanın yapılandırmasını tamamladıktan sonra yeni hafta başlayacaktır. Başladığında bildirim gönderilecektir. \n \n ~Selfie Wars Tavsiyesi~ \n Yeni hafta için joker hazırlığı yapmak sıralamada avantaj sağlayacaktır."
                                                        , getActivity());
                                                isClickGuessitButton = false;
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
                }


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
                        mPicasso.with(getContext()).load(photoUrl).into(profileImageView);
                        getScore();
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
    private void getScore(){
        myScoreRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    scoreTextView.setText("" + dataSnapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        myScoreRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long i = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    String uid;
                    uid = ds.getKey();
                    if(uid.equals(mAuth.getUid())){
                        i = Math.abs(dataSnapshot.getChildrenCount() - i);
                        rankTextView.setText(""+i);
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                }.start();
            }
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

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null){
            timer.cancel();
        }
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


}
