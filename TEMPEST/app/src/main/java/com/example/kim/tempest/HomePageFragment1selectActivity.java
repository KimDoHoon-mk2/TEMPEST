package com.example.kim.tempest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomePageFragment1selectActivity extends AppCompatActivity {

    private Button button0, button1, button2;
    private ListView listView;
    private TextView textView0, textView1;
    private RecyclerView recyclerView;

    private LinearLayoutManager llm;
    private HomePageFragment1selectActivityAdapter adapter;

    private ArrayList<HomePageFragment1selectActivityItem> list;

    private Intent intent;
    private String ID;
    private String time;
    private String lock_id;
    private String name;
    private String result;

    private String newID;
    private String newName;
    private String newNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepagefragment1select);

        init();
    }

    void init(){
        button0 = (Button) findViewById(R.id.homepagefragment1select_button0);
        button1 = (Button) findViewById(R.id.homepagefragment1select_button1);
        button2 = (Button) findViewById(R.id.homepagefragment1select_button2);
        textView0 = (TextView) findViewById(R.id.homepagefragment1select_textview0);
        textView1 = (TextView) findViewById(R.id.homepagefragment1select_textview1);
        recyclerView = (RecyclerView) findViewById(R.id.homepagefragment1select_recyclerview);

        intent = getIntent();
        time = intent.getStringExtra("time");
        ID = intent.getStringExtra("ID");
        lock_id = intent.getStringExtra("LOCK_ID");
        name = intent.getStringExtra("name");

        llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        list = new ArrayList<>();
        adapter = new HomePageFragment1selectActivityAdapter(this,list);
        recyclerView.setAdapter(adapter);

        listUpdate();

        textView0.setText(name);
        textView1.setText(time);

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_userPlus();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_select();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listUpdate();
    }

    @Override
    public void onBackPressed() {
        Intent intent0 = new Intent(HomePageFragment1selectActivity.this,HomePageActivity.class);
        intent0.addFlags(intent0.FLAG_ACTIVITY_CLEAR_TOP);
        intent0.putExtra("SELECT",1);
        startActivity(intent0);
        finish();
    }

    void JSONParser(){
        list.clear();
        try{
            JSONArray jsonArray = new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                HomePageFragment1selectActivityItem temp = new HomePageFragment1selectActivityItem();
                temp.setName(jsonObject.getString("name"));

                list.add(temp);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void JSONParser2(){
        try{
                JSONObject jsonObject = new JSONObject(result);

                newID = jsonObject.getString("ID");
                newNumber = jsonObject.getString("number");
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void listUpdate(){
        try {
            ServerTrans getUser = new ServerTrans();
            getUser.getUser_byIoT(lock_id);
            result = getUser.execute().get();
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

    void dialog_userPlus(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialogedittext, (ViewGroup)findViewById(R.id.dialogedittext_layout));
        builder.setView(view);
        builder.setTitle("사용자 추가");
        builder.setMessage("추가할 사용자의 이름을 입력하세요.");
        final EditText editText = (EditText) view.findViewById(R.id.dialogedittext_edittext);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            newName = editText.getText().toString();
                            ServerTrans getNumber = new ServerTrans();
                            getNumber.getNumber_byName(newName);
                            result = getNumber.execute().get();
                            switch (result){
                                case "1":
                                    dialog_unknown();
                                    newName="";
                                    break;
                                default:
                                    JSONParser2();
                                    if(newID.length()<1){
                                        dialog_neterror();
                                        newName="";
                                    }
                                    else{
                                        dialog_check();
                                    }
                                    break;
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void dialog_check(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용자 추가 확인");
        builder.setMessage("이름 : "+newName+"\n"+"번호 : "+newNumber);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ServerTrans getOTP2 = new ServerTrans();
                            getOTP2.getOTP2(ID, lock_id);
                            String result = getOTP2.execute().get();
                            //result = "000000";
                            if (result.equals("1")) {
                                finish();
                            } else {
                                if(result.length()!=6) {
                                    dialog_neterror();
                                }
                                else{
                                    Intent intent0 = new Intent(HomePageFragment1selectActivity.this, OTPActivity.class);
                                    intent0.putExtra("OTP", result);
                                    intent0.putExtra("PAGE", "2");
                                    intent0.putExtra("ID",ID);
                                    intent0.putExtra("LOCK_ID",lock_id);
                                    intent0.putExtra("NEWID",newID);
                                    intent0.putExtra("TIME",time);
                                    startActivityForResult(intent0, 0);
                                }
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void dialog_select(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialognumberpicker, (ViewGroup)findViewById(R.id.dialognumberpicker_layout));
        builder.setView(view);
        builder.setTitle("추가할 시간 선택");
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
                            ServerTrans getOTP2 = new ServerTrans();
                            getOTP2.getOTP2(ID, lock_id);
                            String result = getOTP2.execute().get();
                            //result = "000000";
                            if (result.length() != 6) {
                                dialog_neterror();
                            } else {
                                Intent intent0 = new Intent(HomePageFragment1selectActivity.this, OTPActivity.class);
                                intent0.putExtra("OTP", result);
                                intent0.putExtra("PAGE", "3");
                                intent0.putExtra("ID",ID);
                                intent0.putExtra("LOCK_ID",lock_id);
                                intent0.putExtra("NUM",num);
                                intent0.putExtra("TYPE",type);
                                startActivityForResult(intent0, 0);
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

    void dialog_exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용 종료");
        builder.setMessage("사용을 끝내시겠습니까?");
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            ServerTrans finishIoT = new ServerTrans();
                            finishIoT.finishIoT(ID,lock_id);
                            result = finishIoT.execute().get();
                            switch(result){
                                case "0":
                                case "1":
                                    toast_pass();
                                    Intent intent0 = new Intent(HomePageFragment1selectActivity.this,HomePageActivity.class);
                                    intent0.addFlags(intent0.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent0.putExtra("SELECT",1);
                                    startActivity(intent0);
                                    finish();
                                    break;
                                default:
                                    dialog_neterror();
                                    break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                });
        builder.show();
    }

    void dialog_fail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용자 추가");
        builder.setMessage("이미 존재하는 사용자입니다.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    void dialog_unknown(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용자 추가");
        builder.setMessage("존재하지 않는 사용자입니다.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    void dialog_neterror(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    void toast_pass(){
        Toast.makeText(HomePageFragment1selectActivity.this, "사용을 성공적으로 종료했습니다.", Toast.LENGTH_SHORT).show();
    }

    void toast_plus(){
        Toast.makeText(HomePageFragment1selectActivity.this, "사용자 추가를 완료했습니다.", Toast.LENGTH_SHORT).show();
    }

    void toast_timeout(){
        Toast.makeText(HomePageFragment1selectActivity.this, "사용 가능 시간이 지났습니다.", Toast.LENGTH_SHORT).show();
    }
}