package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
        myVievHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext,"Fiyat :" + mcontext.getResources().getString(R.string.currency_type)+ mData.get(i).jokerPrice,Toast.LENGTH_LONG).show();
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
        ImageView jokerImage;
        ConstraintLayout constraintLayout;

        public MyVievHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout =itemView.findViewById(R.id.constraintLayout);
            jokerValueNumber = itemView.findViewById(R.id.store_joker_value_number);
            jokerPrice = itemView.findViewById(R.id.store_joker_price);
            jokerImage = itemView.findViewById(R.id.store_joker_imageview);
        }
    }
}
