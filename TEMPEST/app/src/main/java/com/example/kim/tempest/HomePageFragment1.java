package com.example.kim.tempest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomePageFragment1 extends Fragment {

    private View view;

    private TextView textView;
    private RecyclerView recyclerView;

    private LinearLayoutManager llm;
    private HomePageFragment1Adapter adapter;

    private ArrayList<HomePageFragment1Item> list;
    private String result;

    private Intent intent;
    private String ID;

    public HomePageFragment1(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepagefragment1,container,false);

        init();

        return view;
    }

    void init() {
        ID = ((HomePageActivity) getActivity()).ID();
        textView = (TextView) view.findViewById(R.id.homepagefragment1_textview);

        recyclerView = (RecyclerView) view.findViewById(R.id.homepagefragment1_recyclerview);
        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        list =  new ArrayList<>();

        try {
            ServerTrans getItem = new ServerTrans();
            getItem.getIoT_byUser(ID);
            result = getItem.execute().get();
            Log.i("Fragment1Result = ", result);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        JSONParser();
        if (list.size() != 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }

        adapter = new HomePageFragment1Adapter(getActivity(), this, list,ID);
        recyclerView.setAdapter(adapter);
    }

    void JSONParser(){
        list.clear();
        try{
            JSONArray jsonArray = new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                HomePageFragment1Item temp = new HomePageFragment1Item();
                temp.setId(jsonObject.getString("lock_id"));
                temp.setName(jsonObject.getString("name"));
                temp.setTime(jsonObject.getString("due_date"));

                list.add(temp);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void listUpdate(){
        try {
            ServerTrans getItem = new ServerTrans();
            getItem.getIoT_byUser(ID);
            result = getItem.execute().get();
            if(result==null){
                dialog_neterror();
            }
            else{
                JSONParser();
                adapter.notifyDataSetChanged();
            }
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    void dialog_neterror(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
}