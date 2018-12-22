package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SSSRecyclerViewAdapter extends RecyclerView.Adapter<SSSRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<SSSItem> sssItemList;

    public SSSRecyclerViewAdapter(Context mContext, List<SSSItem> sssItemList) {
        this.mContext = mContext;
        this.sssItemList = sssItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.sss_cardview,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(""+sssItemList.get(i).getTitle());
        myViewHolder.description.setText(""+sssItemList.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return sssItemList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sss_title_TextView);
            description = itemView.findViewById(R.id.sss_decription_TextView);
        }
    }
}
