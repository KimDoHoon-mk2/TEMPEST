package com.example.kim.tempest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class HomePageFragment0 extends Fragment {

    private View view;

    private int[] llID = {R.id.homepagefragment0_layout1,R.id.homepagefragment0_layout2,R.id.homepagefragment0_layout3,R.id.homepagefragment0_layout4
                        ,R.id.homepagefragment0_layout5,R.id.homepagefragment0_layout6,R.id.homepagefragment0_layout7,R.id.homepagefragment0_layout8
                        ,R.id.homepagefragment0_layout9,R.id.homepagefragment0_layout10,R.id.homepagefragment0_layout11,R.id.homepagefragment0_layout12
                        ,R.id.homepagefragment0_layout13,R.id.homepagefragment0_layout14,R.id.homepagefragment0_layout15,R.id.homepagefragment0_layout16
                        ,R.id.homepagefragment0_layout17};
    private LinearLayout[] layouts = new LinearLayout[llID.length];
    private int[] tvID = {R.id.homepagefragment0_textview1,R.id.homepagefragment0_textview2,R.id.homepagefragment0_textview3,R.id.homepagefragment0_textview4
                          ,R.id.homepagefragment0_textview5,R.id.homepagefragment0_textview6,R.id.homepagefragment0_textview7,R.id.homepagefragment0_textview8
                          ,R.id.homepagefragment0_textview9,R.id.homepagefragment0_textview10,R.id.homepagefragment0_textview11,R.id.homepagefragment0_textview12
                          ,R.id.homepagefragment0_textview13,R.id.homepagefragment0_textview14,R.id.homepagefragment0_textview15,R.id.homepagefragment0_textview16
                          ,R.id.homepagefragment0_textview17};
    private TextView[] textViews = new TextView[tvID.length];
    private int[] ll1ID = {R.id.homepagefragment0_layout1_0,R.id.homepagefragment0_layout1_1,R.id.homepagefragment0_layout1_2,R.id.homepagefragment0_layout1_3
                           ,R.id.homepagefragment0_layout1_4,R.id.homepagefragment0_layout1_5,R.id.homepagefragment0_layout1_6,R.id.homepagefragment0_layout1_7
                           ,R.id.homepagefragment0_layout1_8,R.id.homepagefragment0_layout1_9,R.id.homepagefragment0_layout1_10,R.id.homepagefragment0_layout1_11
                           ,R.id.homepagefragment0_layout1_12,R.id.homepagefragment0_layout1_13,R.id.homepagefragment0_layout1_14,R.id.homepagefragment0_layout1_15
                           ,R.id.homepagefragment0_layout1_16,R.id.homepagefragment0_layout1_17,R.id.homepagefragment0_layout1_18,R.id.homepagefragment0_layout1_19
                           ,R.id.homepagefragment0_layout1_20,R.id.homepagefragment0_layout1_21,R.id.homepagefragment0_layout1_22,R.id.homepagefragment0_layout1_23
                           ,R.id.homepagefragment0_layout1_24,R.id.homepagefragment0_layout1_25};
    private LinearLayout[] layouts1 = new LinearLayout[ll1ID.length];

    private Intent intent;
    private String ID;
    private String result;

    public HomePageFragment0(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepagefragment0,container,false);

        init();

        return view;
    }

    void init(){
        ID = ((HomePageActivity)getActivity()).ID();
        for(int i=0;i<llID.length;i++){
            textViews[i] = (TextView) view.findViewById(tvID[i]);
            layouts[i] = (LinearLayout) view.findViewById(llID[i]);
            layouts[i].setOnClickListener(llgroup);
        }
        for(int i=0;i<ll1ID.length;i++){
            layouts1[i] = (LinearLayout) view.findViewById(ll1ID[i]);
            layouts1[i].setOnClickListener(llgroup);
        }
    }


    View.OnClickListener llgroup = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.homepagefragment0_layout1:
                    if (layouts1[0].getVisibility() == View.VISIBLE) {
                        layouts1[0].setVisibility(View.GONE);
                        textViews[0].setText(">");
                    } else {
                        layouts1[0].setVisibility(View.VISIBLE);
                        textViews[0].setText("-");
                    }
                    break;
                case R.id.homepagefragment0_layout1_24:
                    try {
                        ServerTrans getAddres = new ServerTrans();
                        getAddres.getIoT_byAddress("24");
                        result = getAddres.execute().get();
                        switch (result.length()){
                            case 0:
                                dialog_neterror();
                                break;
                            default:
                                intent = new Intent(getActivity(),HomePageFragment0selectActivity.class);
                                intent.putExtra("ADDRESS","중구");
                                intent.putExtra("ID", ID);
                                intent.putExtra("RESULT", result);
                                getActivity().startActivityForResult(intent,0);
                                break;

                        }
                    }catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    void dialog_neterror(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
