package com.example.kim.tempest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class LoginPageActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private EditText editText0, editText1;
    private Button button0, button1, button2;

    private InputMethodManager imm;
    private InputFilter inputFilter;

    private SharedPreferences pref;
    private String ID=null,PW=null,NAME=null;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        init();
    }

    void init(){
        relativeLayout = (RelativeLayout) findViewById(R.id.loginpage_relativelayout);
        editText0 = (EditText) findViewById(R.id.loginpage_edittext0);
        editText1 = (EditText) findViewById(R.id.loginpage_edittext1);
        button0 = (Button) findViewById(R.id.loginpage_button0);
        button1 = (Button) findViewById(R.id.loginpage_button1);
        button2 = (Button) findViewById(R.id.loginpage_button2);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
                if(!pattern.matcher(source).matches()){
                    return "";
                }
                return null;
            }
        };

        editText0.setFilters(new InputFilter[]{inputFilter});
        editText0.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER){
                    return true;
                }
                return false;
            }
        });

        editText1.setFilters(new InputFilter[]{inputFilter});
        editText1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER){
                    return true;
                }
                return false;
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
                if(editText0.getText().toString().length() == 0){
                    dialog_id();
                }
                else{
                    if(editText1.getText().toString().length() == 0){
                        dialog_pw();
                    }
                    else{
                        try {
                            ServerTrans login = new ServerTrans();
                            login.login(editText0.getText().toString(),editText1.getText().toString());
                            result = login.execute().get();
                            //result = "0";
                            switch (result){
                                case "1":
                                    dialog_fail0();
                                    break;
                                case "2":
                                    dialog_fail1();
                                    break;
                                default:
                                    JSONParser();
                                    if(NAME==null){
                                        dialog_neterror();
                                    }
                                    else{
                                        pref = getSharedPreferences("data",Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("saveID",ID);
                                        editor.putString("savePW",PW);
                                        editor.putString("saveNAME",NAME);
                                        editor.apply();

                                        Intent intent0 = new Intent(LoginPageActivity.this, HomePageActivity.class);
                                        intent0.putExtra("ID",ID);
                                        intent0.putExtra("NAME",NAME);
                                        intent0.putExtra("SELECT",0);
                                        startActivityForResult(intent0,0);
                                        finish();
                                    }
                                    break;
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            Toast.makeText(LoginPageActivity.this,"fail",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
                startActivity(new Intent(LoginPageActivity.this, NewID0Activity.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
                startActivity(new Intent(LoginPageActivity.this, FindIDActivity.class));
            }
        });

        pref = getSharedPreferences("data", Activity.MODE_PRIVATE);
        ID = pref.getString("saveID",null);
        PW = pref.getString("savePW",null);
        NAME = pref.getString("saveNAME",null);

        if(ID!=null){
            try {
                ServerTrans login = new ServerTrans();
                login.login(ID,PW);
                result = login.execute().get();
                Log.i("homepagevalue__r",result);
                //result = "0";
                switch (result){
                    case "1":
                        dialog_fail0();
                        break;
                    case "2":
                        dialog_fail1();
                        break;
                    default:
                        JSONParser();
                        if(NAME==null){
                            dialog_neterror();
                        }
                        else{
                            Intent intent0 = new Intent(LoginPageActivity.this, HomePageActivity.class);
                            intent0.putExtra("ID",ID);
                            intent0.putExtra("NAME",NAME);
                            intent0.putExtra("SELECT",1);
                            startActivityForResult(intent0,0);
                            finish();
                        }
                        break;
                }
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(LoginPageActivity.this,"fail",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        dialog_exit();
    }

    void JSONParser(){
        try{
            JSONObject jsonObject = new JSONObject(result);

            ID = jsonObject.getString("ID");
            PW = jsonObject.getString("Password");
            NAME = jsonObject.getString("name");

            Log.i("homepagevalues__v",ID+"/"+PW+"/"+NAME);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void dialog_id(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패");
        builder.setMessage("아이디를 입력하세요.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void dialog_pw(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패");
        builder.setMessage("비밀번호를 입력하세요.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void dialog_fail0(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패");
        builder.setMessage("존재하지 않는 아이디입니다.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void dialog_fail1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패");
        builder.setMessage("비밀번호가 일치하지 않습니다.");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    void dialog_exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료하시겠습니까?");
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
                        finish();
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

    void hidekeyboard(){
        imm.hideSoftInputFromWindow(editText0.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
    }
}



