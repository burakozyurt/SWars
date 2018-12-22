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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RankFragmentAdapter extends RecyclerView.Adapter<RankFragmentAdapter.ViewHolder>{
    private Context context;
    private List<ScoreInfo> mData;
    public RankFragmentAdapter(Context context, List<ScoreInfo> mData) {
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        FirebaseDatabase.getInstance().getReference("Users/"+mData.get(i).getUid()).child("properties").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProperties userProperties = dataSnapshot.getValue(UserProperties.class);
                viewHolder.usernameTextView.setText(""+userProperties.getUserName());
                Picasso.with(context).load(userProperties.getPhotoUrl()).into(viewHolder.userPhotoImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(i==0)
        viewHolder.starImageView.setImageResource(R.drawable.star);
        else viewHolder.starImageView.setVisibility(View.INVISIBLE);
        viewHolder.rankNumber.setText(""+(i+1));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView rankNumber;
        ImageView starImageView;
        ImageView userPhotoImageView;
        RelativeLayout relativeLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            starImageView = itemView.findViewById(R.id.starImageView);
            userPhotoImageView = itemView.findViewById(R.id.userPhotoImageView);
            rankNumber = itemView.findViewById(R.id.rank_starNumber);
            relativeLayout = itemView.findViewById(R.id.cardViewRelative);



        }
    }
}
