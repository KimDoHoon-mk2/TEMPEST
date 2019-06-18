package com.example.kim.tempest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomePageFragment1selectActivityAdapter extends RecyclerView.Adapter<HomePageFragment1selectActivityAdapter.HomePageFragment1selectActivityViewHolder> {

    private Activity mActivity;
    private ArrayList<HomePageFragment1selectActivityItem> mList;
    private String mID;

    public class HomePageFragment1selectActivityViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView textView0;

        public HomePageFragment1selectActivityViewHolder(View view){
            super(view);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.homepagefragment1selectitem_layout);
            this.textView0 = (TextView) view.findViewById(R.id.homepagefragment1selectitem_textview0);
        }
    }

    public HomePageFragment1selectActivityAdapter(Activity activity, ArrayList<HomePageFragment1selectActivityItem> list){
        this.mActivity = activity;
        this.mList = list;
    }

    @NonNull
    @Override
    public HomePageFragment1selectActivityAdapter.HomePageFragment1selectActivityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homepagefragment1selectitem,viewGroup,false);
        HomePageFragment1selectActivityAdapter.HomePageFragment1selectActivityViewHolder viewHolder = new HomePageFragment1selectActivityAdapter.HomePageFragment1selectActivityViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageFragment1selectActivityAdapter.HomePageFragment1selectActivityViewHolder viewHolder, int position) {
        final String name = mList.get(position).getName();
        viewHolder.textView0.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size():0);
    }
}
