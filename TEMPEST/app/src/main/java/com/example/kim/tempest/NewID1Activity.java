package com.example.kim.tempest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewID1Activity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private EditText editText0, editText1;
    private TextView textView;
    private Button button;

    private InputMethodManager imm;
    private InputFilter inputFilter;

    private Intent intent;
    private String id;
    private String pw;

    private Pattern p = Pattern.compile("^010(\\d{8})$");
    private Matcher m;
    private Boolean et0=false,et1=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newid1);
        init();
    }

    void init(){
        relativeLayout = (RelativeLayout) findViewById(R.id.newid1_relativelayout);
        editText0 = (EditText) findViewById(R.id.newid1_edittext0);
        editText1 = (EditText) findViewById(R.id.newid1_edittext1);
        textView = (TextView) findViewById(R.id.newid1_textview1);
        button = (Button) findViewById(R.id.newid1_button0);

        intent = getIntent();
        id = intent.getStringExtra("ID");
        pw = intent.getStringExtra("PW");

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if(!pattern.matcher(source).matches()){
                    return "";
                }
                return null;
            }
        };

        editText0.setFilters(new InputFilter[]{inputFilter});
        editText0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText0.getText().toString().length() >= 1){
                    et0=true;
                    if(et0&&et1){
                        button.setVisibility(View.VISIBLE);
                    }
                }else{
                    et0=false;
                    button.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText1.setFilters(new InputFilter[]{inputFilter});
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                m = p.matcher(editText1.getText().toString());
                if(m.find()){
                    et1=true;
                    textView.setVisibility(View.INVISIBLE);
                    if(et0&&et1){
                        button.setVisibility(View.VISIBLE);
                    }
                }else{
                    et1=false;
                    textView.setVisibility(View.VISIBLE);
                    button.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NewID1_NAME ",editText0.getText().toString());
                Log.i("NewID1_NUMBER ",editText1.getText().toString());
                try {
                    hidekeyboard();
                    ServerTrans newInfo = new ServerTrans();
                    newInfo.newInfo(id,pw,editText0.getText().toString(), editText1.getText().toString());
                    String result = newInfo.execute().get();
                    //result = "0";
                    switch (result){
                        case "0":
                            dialog_pass();
                            break;
                        case "1":
                            dialog_fail();
                            break;
                        default :
                            dialog_neterror();
                            break;
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(NewID1Activity.this,"fail",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    void dialog_pass(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");
        builder.setMessage("회원가입에 성공하였습니다.");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent0 = new Intent(NewID1Activity.this,LoginPageActivity.class);
                        intent0.addFlags(intent0.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent0);
                        finish();
                    }
                });
        builder.show();
    }

    void dialog_fail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");
        builder.setMessage("이미 있는 닉네임 입니다");
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

    void hidekeyboard(){
        imm.hideSoftInputFromWindow(editText0.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
    }
}