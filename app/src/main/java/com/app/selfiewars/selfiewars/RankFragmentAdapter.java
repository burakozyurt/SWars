package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RankFragmentAdapter extends RecyclerView.Adapter<RankFragmentAdapter.ViewHolder>{
    private Context context;
    private List<RankingInfoActivity> mData;
    public RankFragmentAdapter(Context context, List<RankingInfoActivity> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View  view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.rankcardview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.usernameTextView.setText(mData.get(i).getUserName());
        viewHolder.starImageView.setImageResource(mData.get(i).getStarImageView());
        viewHolder.userPhotoImageView.setImageResource(mData.get(i).getUserPhotoImageView());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        ImageView starImageView;
        ImageView userPhotoImageView;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            starImageView = itemView.findViewById(R.id.starImageView);
            userPhotoImageView = itemView.findViewById(R.id.userPhotoImageView);
            relativeLayout = itemView.findViewById(R.id.cardViewRelative);

        }
    }
}
