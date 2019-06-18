package com.example.kim.tempest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomePageFragment0selectActivity extends AppCompatActivity {

    private TextView textView;
    private RecyclerView recyclerView;

    private LinearLayoutManager llm;
    private HomePageFragment0selectActivityAdapter adapter;

    private ArrayList<HomePageFragment0selectActivityItem> list;

    private Intent intent;
    private String address;
    private String ID;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepagefragment0select);

        init();
    }

    void init(){
        textView = (TextView) findViewById(R.id.homepagefragment0select_textview);
        recyclerView = (RecyclerView) findViewById(R.id.homepagefragment0select_recyclerview);

        intent = getIntent();
        address = intent.getStringExtra("ADDRESS");
        ID = intent.getStringExtra("ID");
        result = intent.getStringExtra("RESULT");

        textView.setText(address);

        llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        list = new ArrayList<>();
        adapter = new HomePageFragment0selectActivityAdapter(this,list,ID);
        recyclerView.setAdapter(adapter);
        JSONParser();
        adapter.notifyDataSetChanged();
    }

    void JSONParser(){
        try{
            JSONArray jsonArray = new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                HomePageFragment0selectActivityItem temp = new HomePageFragment0selectActivityItem();
                temp.setId(jsonObject.getString("lock_id"));
                temp.setName(jsonObject.getString("name"));
                temp.setStatus(jsonObject.getString("status"));

                list.add(temp);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void dialog_neterror(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("네트워크 오류");
        builder.setMessage("인터넷 연결에 실패하였습니다.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();
    }
}
