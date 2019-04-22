package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
    public MyVievHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mcontext);
        view = mInflater.inflate(R.layout.store_jokers_cardview,viewGroup,false);
        return new MyVievHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVievHolder myVievHolder, final int i) {
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
        myVievHolder.buyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myVievHolder.loadanim.getVisibility() == View.GONE){
                    buyingOperation(myVievHolder, i);
                }
            }
        });
        myVievHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myVievHolder.loadanim.getVisibility() == View.GONE){
                    buyingOperation(myVievHolder, i);
                }
            }
        });

    }

    private void buyingOperation(final MyVievHolder myVievHolder, final int i){
        if (myVievHolder.buyTextView.getVisibility() == View.VISIBLE) {
            Integer diamondValue = MainActivity.diamondValue;
            myVievHolder.loadanim.setVisibility(View.VISIBLE);
            myVievHolder.buyTextView.setVisibility(View.GONE);
            if (Integer.valueOf(mData.get(i).jokerPrice) <= diamondValue) {
                diamondValue-= Integer.valueOf(mData.get(i).jokerPrice);
                MainActivity.myRefUser.child("token").child("diamondValue").setValue(diamondValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(mData.get(i).jokerid == 0){
                            Integer newValue = MainActivity.wildcards.getFiftyFiftyValue() + Integer.valueOf(mData.get(i).jokerValueNumber);
                            MainActivity.myRefUser.child("wildcards").child("fiftyFiftyValue").setValue(newValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    MainActivity.showPopUpInfo(null,"Alındı",null,mcontext);
                                    myVievHolder.buyTextView.setVisibility(View.GONE);
                                    myVievHolder.loadanim.setVisibility(View.GONE);
                                    myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                    myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                                    StoreFragment.UpdateJokers();

                                }
                            });
                        }else {
                            Integer newValue = MainActivity.wildcards.getDoubleDipValue() + Integer.valueOf(mData.get(i).jokerValueNumber);
                            MainActivity.myRefUser.child("wildcards").child("doubleDipValue").setValue(newValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    MainActivity.showPopUpInfo(null,"Alındı",null,mcontext);
                                    myVievHolder.buyTextView.setVisibility(View.GONE);
                                    myVievHolder.loadanim.setVisibility(View.GONE);
                                    myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                                    myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                                    StoreFragment.UpdateJokers();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MainActivity.showPopUpInfo(null,"Sorun Yaşandı",null,mcontext);
                        myVievHolder.buyTextView.setVisibility(View.GONE);
                        myVievHolder.loadanim.setVisibility(View.GONE);
                        myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                        myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                        StoreFragment.UpdateJokers();

                    }
                });
            }else {
                MainActivity.showPopUpInfo(null,"Yeterli elmas bulunmamaktadır.",null,mcontext);
                myVievHolder.buyTextView.setVisibility(View.GONE);
                myVievHolder.loadanim.setVisibility(View.GONE);
                myVievHolder.jokerPrice.setVisibility(View.VISIBLE);
                myVievHolder.currencyimage.setVisibility(View.VISIBLE);
                StoreFragment.UpdateJokers();
            }
        }else {
            myVievHolder.buyTextView.setVisibility(View.VISIBLE);
            myVievHolder.jokerPrice.setVisibility(View.GONE);
            myVievHolder.currencyimage.setVisibility(View.GONE);
        }

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
        LottieAnimationView loadanim;

        public MyVievHolder(@NonNull View itemView) {
            super(itemView);
            loadanim = itemView.findViewById(R.id.store_buyloadingLottieView);
            constraintLayout =itemView.findViewById(R.id.constraintLayout);
            jokerValueNumber = itemView.findViewById(R.id.store_joker_value_number);
            jokerPrice = itemView.findViewById(R.id.store_joker_price);
            jokerImage = itemView.findViewById(R.id.store_joker_imageview);
            buyTextView = itemView.findViewById(R.id.store_buyTextView);
            currencyimage = itemView.findViewById(R.id.store_joker_currency_type);
        }
    }
}
