package com.app.selfiewars.selfiewars;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {
    private List<RankingInfoActivity> rankingInfoActivityList;
    private TextView dayValueTextView;
    private TextView hourValueTextView;
    private TextView minValueTextView;
    private CountDownTimer countDownTimer;
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
        rankSetup(view);
        setRankEndTime(view);
        getTimestampForEndTime();
        return view;
    }

    public void rankSetup(final View view) {
        FirebaseDatabase mydatabase;
        final DatabaseReference myRefScore;
        final DatabaseReference myRefUsers;
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance();
        myRefUsers = mydatabase.getReference("Users");
        myRefScore = mydatabase.getReference("Scores");
        myRefScore.orderByValue().limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<ScoreInfo> scoreInfos = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ScoreInfo scoreInfo = new ScoreInfo();
                    scoreInfo.setScoreValue(ds.getValue(Integer.class));
                    scoreInfo.setUid(ds.getKey());
                    scoreInfos.add(scoreInfo);
                    if (scoreInfos.size() == 5) {
                        Collections.reverse(scoreInfos);
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
        FirebaseDatabase mydatabase;
        final DatabaseReference myRefUser;
        final DatabaseReference myRefEndTime;
        FirebaseAuth myAuth;

        mydatabase = FirebaseDatabase.getInstance();
        myAuth = FirebaseAuth.getInstance();
        myRefUser = mydatabase.getReference("Users/" + myAuth.getUid());
        myRefEndTime = mydatabase.getReference("EndTime/" + getResources().getString(R.string.timestamp));

        myRefUser.child(getResources().getString(R.string.timestamp)).child("timestamp").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Long nowTimestamp = dataSnapshot.child("nowtimestamp").child(getResources().getString(R.string.timestamp)).getValue(Long.class);

                        myRefEndTime.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Long endtime = dataSnapshot.getValue(Long.class);
                                if(nowTimestamp < endtime) { // Hafta ediyodur.
                                    startRefreshCountTime(nowTimestamp, endtime);
                                }
                                else {
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
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

}
