package com.example.kim.tempest;

import android.app.Activity;
import android.content.Context;
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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomePageFragment0selectActivityAdapter extends RecyclerView.Adapter<HomePageFragment0selectActivityAdapter.HomePageFragment0selectActivityViewHolder> {

    private Activity mActivity;
    private ArrayList<HomePageFragment0selectActivityItem> mList;
    private String mID;

    public class HomePageFragment0selectActivityViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView textView0, textView1;
        Button button;


        public HomePageFragment0selectActivityViewHolder(View view){
            super(view);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.homepagefragment0selectitem_layout);
            this.textView0 = (TextView) view.findViewById(R.id.homepagefragment0selectitem_textview0);
            this.textView1 = (TextView) view.findViewById(R.id.homepagefragment0selectitem_textview1);
            this.button = (Button) view.findViewById(R.id.homepagefragment0selectitem_button);
        }
    }

    public HomePageFragment0selectActivityAdapter(Activity activity, ArrayList<HomePageFragment0selectActivityItem> list, String id){
        this.mActivity = activity;
        this.mList = list;
        this.mID = id;
    }

    @NonNull
    @Override
    public HomePageFragment0selectActivityAdapter.HomePageFragment0selectActivityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homepagefragment0selectitem,viewGroup,false);
        HomePageFragment0selectActivityViewHolder viewHolder = new HomePageFragment0selectActivityViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageFragment0selectActivityAdapter.HomePageFragment0selectActivityViewHolder viewHolder, int position) {
        final String id = mList.get(position).getId();
        final String name = mList.get(position).getName();
        final String status = mList.get(position).getStatus();
        viewHolder.textView0.setText(name);
        if(status.equals("0")){
            viewHolder.button.setVisibility(View.VISIBLE);
            viewHolder.textView1.setVisibility(View.GONE);
        }
        else{
            viewHolder.button.setVisibility(View.GONE);
            viewHolder.textView1.setVisibility(View.VISIBLE);
        }
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_select(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size():0);
    }

    void dialog_select(final String id){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialognumberpicker, (ViewGroup)mActivity.findViewById(R.id.dialognumberpicker_layout));
        builder.setView(view);
        builder.setTitle("사용 시간 선택");
        final NumberPicker numberPicker0 = (NumberPicker) view.findViewById(R.id.dialognumberpicker_numberpicker0);
        numberPicker0.setMinValue(1);
        numberPicker0.setMaxValue(30);
        numberPicker0.setWrapSelectorWheel(false);
        final NumberPicker numberPicker1 = (NumberPicker) view.findViewById(R.id.dialognumberpicker_numberpicker1);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(3);
        numberPicker1.setWrapSelectorWheel(false);
        numberPicker1.setDisplayedValues(new String[]
                {"초","분","시간", "일"});
        builder.setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num = String.valueOf(numberPicker0.getValue());
                        String type = String.valueOf(numberPicker1.getValue());
                        try {
                            ServerTrans getOTP = new ServerTrans();
                            getOTP.getOTP(mID, id);
                            String result = getOTP.execute().get();
                            //result = "000000";
                            if (result.length() != 6) {
                                dialog_neterror();
                            } else {
                                Intent intent0 = new Intent(mActivity, OTPActivity.class);
                                intent0.putExtra("OTP", result);
                                intent0.putExtra("PAGE", "0");
                                intent0.putExtra("ID",mID);
                                intent0.putExtra("LOCK_ID",id);
                                intent0.putExtra("NUM",num);
                                intent0.putExtra("TYPE",type);
                                mActivity.startActivityForResult(intent0, 0);
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void dialog_neterror(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("네트워크 오류");
        builder.setMessage("인터넷 연결에 실패하였습니다.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
