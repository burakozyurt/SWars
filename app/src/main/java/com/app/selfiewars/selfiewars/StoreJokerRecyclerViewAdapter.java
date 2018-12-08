package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StoreJokerRecyclerViewAdapter extends RecyclerView.Adapter<StoreJokerRecyclerViewAdapter.MyVievHolder> {
    private Context mcontext;
    private List<StoreJoker> mData;

    public StoreJokerRecyclerViewAdapter(Context mcontext, List<StoreJoker> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public StoreJokerRecyclerViewAdapter.MyVievHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mcontext);
        view = mInflater.inflate(R.layout.store_jokers_cardview,viewGroup,false);
        return new StoreJokerRecyclerViewAdapter.MyVievHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoreJokerRecyclerViewAdapter.MyVievHolder myVievHolder, final int i) {
      /*if(mData.get(i).jokerid==0)
            myVievHolder.constraintLayout.setBackgroundColor(mcontext.getResources().getColor(R.color.fiftyfifty_background));
        else if(mData.get(i).jokerid==1)
            myVievHolder.constraintLayout.setBackgroundColor(mcontext.getResources().getColor(R.color.x2joker_background));*/
        myVievHolder.jokerValueNumber.setText(mData.get(i).getJokerValueNumber());
        myVievHolder.jokerPrice.setText(mData.get(i).getJokerPrice());
        myVievHolder.jokerImage.setImageResource(mData.get(i).getJokerImage());
        final Handler handler =  new Handler();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (myVievHolder.buyTextView.getVisibility() == View.VISIBLE) {
                            myVievHolder.buyTextView.setVisibility(View.GONE);
                            myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                            myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        },0,6000);
        myVievHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myVievHolder.buyTextView.getVisibility() == View.VISIBLE) {
                    //Toast.makeText(mcontext, "Tıklandı", Toast.LENGTH_SHORT).show();
                    /*FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("token").child("diamondValue").runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Toast.makeText(mcontext, "Transaction çalışıyor", Toast.LENGTH_SHORT).show();
                            Integer diamondValue = mutableData.getValue(Integer.class);
                            if (Integer.valueOf(mData.get(i).jokerPrice) <= diamondValue) {
                                diamondValue-= Integer.valueOf(mData.get(i).jokerPrice);
                                mutableData.setValue(diamondValue);
                                Toast.makeText(mcontext, "elmas" + diamondValue, Toast.LENGTH_SHORT).show();

                                return Transaction.success(mutableData);
                            }else {
                                return null;
                            }
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            if(b){
                                if(mData.get(i).jokerid==0){
                                    Toast.makeText(mcontext, "Azaltıldı elmas", Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("wildcards").child("fiftyFiftyValue").runTransaction(new Transaction.Handler() {
                                        @NonNull
                                        @Override
                                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                            Integer newvalue = mutableData.getValue(Integer.class) + Integer.valueOf(mData.get(i).jokerValueNumber);
                                            mutableData.setValue(newvalue);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                            if(b){
                                                MainActivity.showPopUpInfo(null,"Alındı",null,mcontext);
                                                    myVievHolder.buyTextView.setVisibility(View.GONE);
                                                    myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                                    myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                                            }else {
                                                MainActivity.showPopUpInfo(null,"Sorun Yaşandı",null,mcontext);

                                            }
                                        }
                                    });
                                }else if(mData.get(i).jokerid==1){
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("wildcards").child("doubleDipValue").runTransaction(new Transaction.Handler() {
                                        @NonNull
                                        @Override
                                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                            Integer newvalue = mutableData.getValue(Integer.class) +Integer.valueOf(mData.get(i).jokerValueNumber);
                                            mutableData.setValue(newvalue);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                            if(b){
                                                MainActivity.showPopUpInfo(null,"Alındı",null,mcontext);
                                                myVievHolder.buyTextView.setVisibility(View.GONE);
                                                myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                                myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                                            }else {
                                                MainActivity.showPopUpInfo(null,"Sorun Yaşandı",null,mcontext);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });*/
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("token").child("diamondValue").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Integer diamondValue = dataSnapshot.getValue(Integer.class);
                            if (Integer.valueOf(mData.get(i).jokerPrice) <= diamondValue) {
                                diamondValue-= Integer.valueOf(mData.get(i).jokerPrice);
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("token").child("diamondValue").setValue(diamondValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(mData.get(i).jokerid == 0){
                                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("wildcards").child("fiftyFiftyValue").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Integer newValue = dataSnapshot.getValue(Integer.class) + Integer.valueOf(mData.get(i).jokerValueNumber);
                                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("wildcards").child("fiftyFiftyValue").setValue(newValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            MainActivity.showPopUpInfo(null,"Alındı",null,mcontext);
                                                            myVievHolder.buyTextView.setVisibility(View.GONE);
                                                            myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                                            myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }else {
                                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("wildcards").child("doubleDipValue").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Integer newValue = dataSnapshot.getValue(Integer.class) + Integer.valueOf(mData.get(i).jokerValueNumber);
                                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("wildcards").child("doubleDipValue").setValue(newValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            MainActivity.showPopUpInfo(null,"Alındı",null,mcontext);
                                                            myVievHolder.buyTextView.setVisibility(View.GONE);
                                                            myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                                            myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        MainActivity.showPopUpInfo(null,"Sorun Yaşandı",null,mcontext);
                                        myVievHolder.buyTextView.setVisibility(View.GONE);
                                        myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                        myVievHolder.currencyimage.setVisibility(View.VISIBLE);

                                    }
                                });
                            }else {
                                MainActivity.showPopUpInfo(null,"Yeterli elmas bulunmamaktadır.",null,mcontext);
                                myVievHolder.buyTextView.setVisibility(View.GONE);
                                myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    myVievHolder.buyTextView.setVisibility(View.VISIBLE);
                    myVievHolder.jokerPrice.setVisibility(View.GONE);
                    myVievHolder.currencyimage.setVisibility(View.GONE);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyVievHolder extends RecyclerView.ViewHolder {
        TextView jokerValueNumber;
        TextView jokerPrice;
        TextView buyTextView;
        ImageView jokerImage;
        ImageView currencyimage;
        ConstraintLayout constraintLayout;

        public MyVievHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout =itemView.findViewById(R.id.constraintLayout);
            jokerValueNumber = itemView.findViewById(R.id.store_joker_value_number);
            jokerPrice = itemView.findViewById(R.id.store_joker_price);
            jokerImage = itemView.findViewById(R.id.store_joker_imageview);
            buyTextView = itemView.findViewById(R.id.store_buyTextView);
            currencyimage = itemView.findViewById(R.id.store_joker_currency_type);
        }
    }
}
