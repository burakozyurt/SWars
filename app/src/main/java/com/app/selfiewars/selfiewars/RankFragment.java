package com.app.selfiewars.selfiewars;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {
    private List<RankingInfoActivity> rankingInfoActivityList;
    private TextView dayValueTextView;
    private TextView hourValueTextView;
    private TextView minValueTextView;
    private CountDownTimer countDownTimer;
    private String uid;
    private static int myrank = 0;

    public RankFragment() {

        rankingInfoActivityList = new ArrayList<>();

      /*  rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        uid = FirebaseAuth.getInstance().getUid();
        rankSetup(view);
        setRankEndTime(view);
        getTimestampForEndTime();
        return view;
    }

    public void rankSetup(final View view) {
        MainActivity.myScoreRef.orderByValue().limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean meexist= false;
                final List<ScoreInfo> scoreInfos = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ScoreInfo scoreInfo = new ScoreInfo();
                    scoreInfo.setScoreValue(ds.getValue(Integer.class));
                    scoreInfo.setUid(ds.getKey());
                    scoreInfos.add(scoreInfo);
                    if (scoreInfo.getUid().equalsIgnoreCase(uid)) {
                        meexist = true;
                        myrank = MainActivity.UserRank;
                    }
                    if (scoreInfos.size() == dataSnapshot.getChildrenCount()) {
                        Collections.reverse(scoreInfos);
                        if (!meexist){
                            ScoreInfo myscoreInfo = new ScoreInfo();
                            myscoreInfo.setScoreValue(MainActivity.UserRank);
                            myscoreInfo.setUid(uid);
                            scoreInfos.add(myscoreInfo);
                            myrank = MainActivity.UserRank;

                        }
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRank);
                        RankFragmentAdapter rankRcycView = new RankFragmentAdapter(getContext(),
                                scoreInfos);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(rankRcycView);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void setRankEndTime(View view) {
        dayValueTextView = view.findViewById(R.id.rank_valueOfDayTextView);
        hourValueTextView = view.findViewById(R.id.rank_valueOfHourTextView);
        minValueTextView = view.findViewById(R.id.rank_valueOfMinuteTextView);


    }
    private void getTimestampForEndTime(){
        MainActivity.myRefUser.child(getResources().getString(R.string.timestamp)).child("timestamp").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                mutableData.setValue(ServerValue.TIMESTAMP);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Long nowtimestamp = dataSnapshot.getValue(Long.class);
                if(nowtimestamp < MainActivity.endTimeStamp) { // Hafta ediyodur.
                    startRefreshCountTime(nowtimestamp, MainActivity.endTimeStamp);
                }
                else {
                    if(countDownTimer !=null)
                        countDownTimer.cancel();
                    dayValueTextView.setText("00");
                    hourValueTextView.setText("00");
                    minValueTextView.setText("00");
                }
            }
        });
        /*MainActivity.myRefUser.child(getResources().getString(R.string.timestamp)).child("timestamp").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                MainActivity.myRefEndTime.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("Endtime",""+dataSnapshot.getValue(Long.class));
                                Long endtime = dataSnapshot.getValue(Long.class);
                                if(MainActivity.nowTimeStamp < endtime) { // Hafta ediyodur.
                                    startRefreshCountTime(MainActivity.nowTimeStamp, endtime);
                                }
                                else {
                                    if(countDownTimer !=null)
                                    countDownTimer.cancel();
                                    dayValueTextView.setText("00");
                                    hourValueTextView.setText("00");
                                    minValueTextView.setText("00");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });*/
    }

    private void startRefreshCountTime(Long nowTimestamp, Long endtime) {
        try {

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = new CountDownTimer((Math.abs(endtime - nowTimestamp)), 60000) {
                    @Override
                    public void onTick(long l) {
                        dayValueTextView.setText(""+ TimeUnit.MILLISECONDS.toDays(l));
                        hourValueTextView.setText(""+ (TimeUnit.MILLISECONDS.toHours(l)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(l))));
                        minValueTextView.setText(""+ (TimeUnit.MILLISECONDS.toMinutes(l)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))));
                       /* endtimeText.setText(getResources().getString(R.string.Endtime) + TimeUnit.MILLISECONDS.toHours(l) + getResources().getString(R.string.hours)
                                + (TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))) + getResources().getString(R.string.minute)
                                + (TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))) + getResources().getString(R.string.second));*/
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            } else {
                countDownTimer = new CountDownTimer((Math.abs(endtime - nowTimestamp)), 60000) {
                    @Override
                    public void onTick(long l) {
                        dayValueTextView.setText(""+ TimeUnit.MILLISECONDS.toDays(l));
                        hourValueTextView.setText(""+ (TimeUnit.MILLISECONDS.toHours(l)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(l))));
                        minValueTextView.setText(""+ (TimeUnit.MILLISECONDS.toMinutes(l)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))));
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.UserRank !=null && MainActivity.UserRank != myrank){
            rankSetup(getView());
        }
    }
}
