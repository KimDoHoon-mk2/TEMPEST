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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomePageFragment1Adapter extends RecyclerView.Adapter<HomePageFragment1Adapter.HomePageFragment1ViewHolder> {

    private Activity mActivity;
    private HomePageFragment1 mFragment;
    private ArrayList<HomePageFragment1Item> mList;
    private String mID;


    public class HomePageFragment1ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView textView0, textView1;
        Button button0, button1;

        public HomePageFragment1ViewHolder(View view){
            super(view);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.homepagefragment1item_layout);
            this.textView0 = (TextView) view.findViewById(R.id.homepagefragment1item_textview0);
            this.textView1 = (TextView) view.findViewById(R.id.homepagefragment1item_textview1);
            this.button0 = (Button) view.findViewById(R.id.homepagefragment1item_button0);
            this.button1 = (Button) view.findViewById(R.id.homepagefragment1item_button1);
        }
    }

    public HomePageFragment1Adapter(Activity activity, HomePageFragment1 fragment, ArrayList<HomePageFragment1Item> list, String ID){
        this.mActivity = activity;
        this.mFragment = fragment;
        this.mList = list;
        this.mID = ID;
    }

    @NonNull
    @Override
    public HomePageFragment1Adapter.HomePageFragment1ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homepagefragment1item,viewGroup,false);
        HomePageFragment1ViewHolder viewHolder = new HomePageFragment1ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageFragment1Adapter.HomePageFragment1ViewHolder viewHolder, final int position) {
        final String lock_id = mList.get(position).getId();
        final String name = mList.get(position).getName();
        final String time = mList.get(position).getTime();
        viewHolder.textView0.setText(name);
        viewHolder.textView1.setText(time);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ServerTrans checkList = new ServerTrans();
                    checkList.checkList(mID,lock_id);
                    String result = checkList.execute().get();
                    switch (result){
                        case "0":
                            Intent intent0 = new Intent(mActivity,HomePageFragment1selectActivity.class);
                            intent0.putExtra("name",name);
                            intent0.putExtra("time", time);
                            intent0.putExtra("ID",mID);
                            intent0.putExtra("LOCK_ID",lock_id);
                            mActivity.startActivityForResult(intent0,100);
                            break;
                        case "1":
                            mFragment.listUpdate();
                            toast_timeout();
                            break;
                        default:
                            dialog_neterror();
                            break;

                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ServerTrans getOTP2 = new ServerTrans();
                    getOTP2.getOTP2(mID, lock_id);
                    String result = getOTP2.execute().get();
                    //result = "000000";
                    if (result.equals("1")) {
                        mFragment.listUpdate();
                        toast_timeout();
                    } else {
                        if(result.length()!=6) {
                            dialog_neterror();
                        }
                        else{
                            Intent intent0 = new Intent(mActivity, OTPActivity.class);
                            intent0.putExtra("OTP", result);
                            intent0.putExtra("PAGE", "1");
                            intent0.putExtra("ID",mID);
                            intent0.putExtra("LOCK_ID",lock_id);
                            mActivity.startActivityForResult(intent0, 0);
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ServerTrans closed = new ServerTrans();
                    closed.closed(mID, lock_id);
                    String result = closed.execute().get();
                    //result = "000000";
                    switch (result){
                        case "0":
                            toast_closed();
                            break;
                        case "1":
                            mFragment.listUpdate();
                            toast_timeout();
                            break;
                        default:
                            dialog_neterror();
                            break;
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size():0);
    }

    void dialog_neterror(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("네트워크 오류");
        builder.setMessage("연결에 실패하였습니다.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void toast_closed(){
        Toast.makeText(mActivity, "성공적으로 닫았습니다.", Toast.LENGTH_SHORT).show();
    }

    void toast_timeout(){
        Toast.makeText(mActivity, "사용 가능 시간이 지났습니다.", Toast.LENGTH_SHORT).show();
    }
}

