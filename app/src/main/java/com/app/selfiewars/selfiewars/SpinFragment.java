package com.app.selfiewars.selfiewars;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpinFragment extends Fragment {
    private FirebaseDatabase mydatabase;
    private DatabaseReference myRefUser;
    private DatabaseReference myRefRightOfGame;
    private FirebaseAuth mAuth;
    private LuckyWheelView luckyWheelView;
    private List<LuckyItem> data;
    private Button spin;
    private boolean isrunnigluckywheel;
    private Long nowTimestamp;
    private Long rightofspinMilis;
    private Integer rightofSpin;
    private LottieAnimationView spinAdsAnimView;
    private TextView righofSpinText;
    private TextView endtimeText;
    private CountDownTimer countDownTimer;
    private final int idDiamond = 0, idJoker1 = 1, idJoker2 = 2, idReSpin = 3, idHealth = 4, idGreenGem = 5;
    private final String color1 = "#ffffff", color2 = "#e3f2fd", color3 = "#bbdefb";

    public SpinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spin, container, false);
        luckyWheelView = view.findViewById(R.id.luckyWheel);
        spin = view.findViewById(R.id.button3);
        righofSpinText = view.findViewById(R.id.home_profile_rightOfGame_textView);
        spinAdsAnimView = view.findViewById(R.id.lottieAdsSpin);
        endtimeText = view.findViewById(R.id.endtimSpinTextView);
        define();
        getTimeStampControlOfSpin();
        spinClick();
        setupLuckyWheel();
        return view;
    }

    private void define() {
        isrunnigluckywheel = false;
        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance();
        myRefUser = mydatabase.getReference("Users/" + mAuth.getUid());
        myRefRightOfGame = mydatabase.getReference("RightOfGame/"+mAuth.getUid());

    }

    private void getTimeStampControlOfSpin() {
        myRefUser.child("nowtimestamp").child("timestamp").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(getResources().getString(R.string.nowtimestamp)).hasChild(getResources().getString(R.string.timestamp))) {
                            Integer controlrighofSpin = dataSnapshot.child("rightofspin").child(getResources().getString(R.string.spinValue)).getValue(Integer.class);
                            rightofSpin = controlrighofSpin;
                            righofSpinText.setText("" + controlrighofSpin);
                            rightofspinMilis = dataSnapshot.child("rightofspin").child(getResources().getString(R.string.timestamp)).getValue(Long.class);
                            nowTimestamp = dataSnapshot.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).getValue(Long.class);
                            if (nowTimestamp < rightofspinMilis) {
                                if (controlrighofSpin == 0) {
                                    startOrRefresCountTime(nowTimestamp);
                                    endtimeText.setVisibility(View.VISIBLE);
                                    spinAdsAnimView.setVisibility(View.VISIBLE);
                                } else {
                                    endtimeText.setVisibility(View.INVISIBLE);
                                    spinAdsAnimView.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                if (controlrighofSpin == 0) {
                                    endtimeText.setVisibility(View.INVISIBLE);
                                    spinAdsAnimView.setVisibility(View.INVISIBLE);
                                    myRefUser.child("rightofspin").child(getResources().getString(R.string.spinValue)).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                } else {
                                    endtimeText.setVisibility(View.INVISIBLE);
                                    spinAdsAnimView.setVisibility(View.INVISIBLE);
                                    righofSpinText.setText("" + controlrighofSpin);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

            }
        });
    }

    private void startOrRefresCountTime(Long nowTimestamp) {
        try {

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = new CountDownTimer((Math.abs(rightofspinMilis - nowTimestamp)), 1000) {
                    @Override
                    public void onTick(long l) {
                        endtimeText.setText(getResources().getString(R.string.Endtime) + TimeUnit.MILLISECONDS.toHours(l) + getResources().getString(R.string.hours)
                                + (TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))) + getResources().getString(R.string.minute)
                                + (TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))) + getResources().getString(R.string.second));
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            } else {
                countDownTimer = new CountDownTimer((Math.abs(rightofspinMilis - nowTimestamp)), 1000) {
                    @Override
                    public void onTick(long l) {
                        endtimeText.setText(getResources().getString(R.string.Endtime) + TimeUnit.MILLISECONDS.toHours(l) + getResources().getString(R.string.hours)
                                + (TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))) + getResources().getString(R.string.minute)
                                + (TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))) + getResources().getString(R.string.second));
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        } catch (Exception e) {

        }
    }

    public void setupLuckyWheel() {
        setSpinItem();
        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                if (isrunnigluckywheel == true) {
                    if (index > 0) {
                        isrunnigluckywheel = false;
                        increaseTheReturnValue(index-1);
                    } else {
                        increaseTheReturnValue(index);
                        isrunnigluckywheel = false;
                    }
                }
            }
        });
    }

    public void spinClick() {
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isrunnigluckywheel) {
                    isrunnigluckywheel = true;
                    //Toast.makeText(getContext(), "Tıklandı", Toast.LENGTH_SHORT).show();
                    myRefUser.child("rightofspin").child(getResources().getString(R.string.spinValue)).runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Integer i = mutableData.getValue(Integer.class);
                            if (i == 0) {
                                return null;
                            } else {
                                i--;
                                mutableData.setValue(i);
                                return Transaction.success(mutableData);
                            }

                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            if (b) {
                                Integer i = dataSnapshot.getValue(Integer.class);
                                righofSpinText.setText("" + i);
                                if (i == 0) {
                                    myRefUser.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    getTimeStampControlOfSpin();
                                                    Long nowTimeStamp = dataSnapshot.child("nowtimestamp").child(getResources().getString(R.string.timestamp)).getValue(Long.class);
                                                    myRefUser.child("rightofspin").child(getResources().getString(R.string.timestamp)).setValue(nowTimeStamp + 86400000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //Toast.makeText(getContext(), "Tıklandı", Toast.LENGTH_SHORT).show();
                                                            luckyWheelView.startLuckyWheelWithTargetIndex(new Random().nextInt(12));

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                } else {
                                    //Toast.makeText(getContext(), "Tıklandı", Toast.LENGTH_SHORT).show();
                                    luckyWheelView.startLuckyWheelWithTargetIndex(new Random().nextInt(12));
                                }
                            }else {
                                MainActivity.showPopUpInfo(null, "Uyarı!", "Çark çevirme hakkınız dolmuştur", getContext());
                            }
                        }
                    });
                }
            }

        });

    }

    public void increaseTheReturnValue(Integer index) {
        final Integer value = data.get(index).valueNumber;
        Integer itemId = data.get(index).id;
        if (itemId == idDiamond) {
            myRefUser.child("token").child("diamondValue").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer diamondValue = mutableData.getValue(Integer.class);
                    diamondValue += value;
                    mutableData.setValue(diamondValue);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if(b){
                        MainActivity.showPopUpInfo(R.drawable.diamond, "Tebrikler!", value + " " + "Adet elmas kazandınız", getContext());
                    }
                }
            });
        }
        else if(itemId == idJoker1) {
            myRefUser.child("wildcards").child("fiftyFiftyValue").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer fiftyFiftyValue = mutableData.getValue(Integer.class);
                    fiftyFiftyValue += value;
                    mutableData.setValue(fiftyFiftyValue);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if(b){
                        MainActivity.showPopUpInfo(R.drawable.joker1, "Tebrikler!", value + " " + "Adet joker kazandınız", getContext());
                    }
                }
            });
        }
        else if(itemId == idJoker2) {
            myRefUser.child("wildcards").child("doubleDipValue").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer doubleDipValue = mutableData.getValue(Integer.class);
                    doubleDipValue += value;
                    mutableData.setValue(doubleDipValue);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if(b){
                        MainActivity.showPopUpInfo(R.drawable.joker2, "Tebrikler!", value + " " + "Adet joker kazandınız", getContext());
                    }
                }
            });

        }
        else if(itemId == idReSpin) {
            myRefUser.child("rightofspin").child("spinValue").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer spinValue = mutableData.getValue(Integer.class);
                    spinValue += value;
                    mutableData.setValue(spinValue);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if(b){
                        MainActivity.showPopUpInfo(R.drawable.spin, "Tebrikler!", value + " " + "Adet çark çevirme hakkı kazandınız", getContext());
                    }
                }
            });

        }
        else if(itemId == idHealth) {
            myRefUser.child("wildcards").child("healthValue").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer healthValue = mutableData.getValue(Integer.class);
                    healthValue += value;
                    mutableData.setValue(healthValue);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if(b){
                        MainActivity.showPopUpInfo(R.drawable.health, "Tebrikler!", value + " " + "Adet can kazandınız", getContext());
                    }
                }
            });
        }
        else if(itemId == idGreenGem) {
            myRefRightOfGame.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer rightOfGame = mutableData.getValue(Integer.class);
                    rightOfGame += value;
                    mutableData.setValue(rightOfGame);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if(b){
                        MainActivity.showPopUpInfo(R.drawable.greengem, "Tebrikler!", value + " " + "Adet tahmin etme hakkı kazandınız", getContext());
                    }
                }
            });
        }
    }

    public void setSpinItem() {
        data = new ArrayList<>();
        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "x1";
        luckyItem1.valueNumber = 1;
        luckyItem1.icon = R.drawable.health;
        luckyItem1.color = Color.parseColor(color1);
        luckyItem1.id = idHealth;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "x3";
        luckyItem2.valueNumber = 3;
        luckyItem2.icon = R.drawable.joker2;
        luckyItem2.color = Color.parseColor(color2);
        luckyItem2.id = idJoker2;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "x1";
        luckyItem3.valueNumber = 1;
        luckyItem3.icon = R.drawable.joker1;
        luckyItem3.color = Color.parseColor(color3);
        luckyItem3.id = idJoker1;
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "x5";
        luckyItem4.valueNumber = 5;
        luckyItem4.id = idDiamond;
        luckyItem4.icon = R.drawable.diamond;
        luckyItem4.color = Color.parseColor(color1);
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "x1";
        luckyItem5.valueNumber = 1;
        luckyItem5.id = idGreenGem;
        luckyItem5.icon = R.drawable.greengem;
        luckyItem5.color = Color.parseColor(color2);
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "x2";
        luckyItem6.id = idReSpin;
        luckyItem6.valueNumber = 2;
        luckyItem6.icon = R.drawable.spin;
        luckyItem6.color = Color.parseColor(color3);
        data.add(luckyItem6);
        //////////////////

        //////////////////////
        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "x2";
        luckyItem7.valueNumber = 2;
        luckyItem7.id = idHealth;
        luckyItem7.icon = R.drawable.health;
        luckyItem7.color = Color.parseColor(color1);
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "x1";
        luckyItem8.id = idJoker2;
        luckyItem8.valueNumber = 1;
        luckyItem8.icon = R.drawable.joker2;
        luckyItem8.color = Color.parseColor(color2);
        data.add(luckyItem8);

        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.text = "x3";
        luckyItem9.id = idJoker1;
        luckyItem9.valueNumber = 3;
        luckyItem9.icon = R.drawable.joker1;
        luckyItem9.color = Color.parseColor(color3);
        data.add(luckyItem9);

        LuckyItem luckyItem10 = new LuckyItem();
        luckyItem10.text = "x10";
        luckyItem10.id = idDiamond;
        luckyItem10.valueNumber = 10;
        luckyItem10.icon = R.drawable.diamond;
        luckyItem10.color = Color.parseColor(color1);
        data.add(luckyItem10);

        LuckyItem luckyItem11 = new LuckyItem();
        luckyItem11.text = "x2";
        luckyItem11.id = idGreenGem;
        luckyItem11.valueNumber = 2;
        luckyItem11.icon = R.drawable.greengem;
        luckyItem11.color = Color.parseColor(color2);
        data.add(luckyItem11);

        LuckyItem luckyItem12 = new LuckyItem();
        luckyItem12.text = "x1";
        luckyItem12.id = idReSpin;
        luckyItem12.valueNumber = 1;
        luckyItem12.icon = R.drawable.spin;
        luckyItem12.color = Color.parseColor(color3);
        data.add(luckyItem12);

        luckyWheelView.setData(data);
        luckyWheelView.setRound(7);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            //Toast.makeText(getContext(),"Sayaç Bekletildi" , Toast.LENGTH_SHORT).show();
            countDownTimer.cancel();
        }
        if (isrunnigluckywheel == true) isrunnigluckywheel = false;
    }

}
