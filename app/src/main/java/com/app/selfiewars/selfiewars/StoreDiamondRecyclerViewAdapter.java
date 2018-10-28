package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StoreDiamondRecyclerViewAdapter extends RecyclerView.Adapter<StoreDiamondRecyclerViewAdapter.MyVievHolder> {
    private Context mcontext;
    private List<StoreDiamond> mData;

    public StoreDiamondRecyclerViewAdapter(Context mcontext, List<StoreDiamond> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyVievHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mcontext);
        view = mInflater.inflate(R.layout.store_diamond_cardview,viewGroup,false);
        return new MyVievHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVievHolder myVievHolder, int i) {
        myVievHolder.diamondValueNumber.setText(mData.get(i).getDiamondValueNumber());
        myVievHolder.diamondPrice.setText(mData.get(i).getDiamondPrice());
        myVievHolder.diamondImage.setImageResource(mData.get(i).getDiamondImage());



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyVievHolder extends RecyclerView.ViewHolder{
        TextView diamondValueNumber;
        TextView diamondPrice;
        ImageView diamondImage;

    public MyVievHolder(@NonNull View itemView) {
        super(itemView);
        diamondValueNumber = itemView.findViewById(R.id.store_diamond_value_number);
        diamondPrice = itemView.findViewById(R.id.store_diamond_price);
        diamondImage = itemView.findViewById(R.id.store_diamond_imageview);
    }
}

}
