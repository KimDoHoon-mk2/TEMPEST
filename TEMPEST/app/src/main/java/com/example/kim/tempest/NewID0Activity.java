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
import java.util.regex.Pattern;

public class NewID0Activity extends AppCompatActivity {

    private RelativeLayout relativeLayout0;
    private int[] etID = {R.id.newid0_edittext0,R.id.newid0_edittext1,R.id.newid0_edittext2};
    private EditText[] editTexts = new EditText[etID.length];
    private int[] tvID = {R.id.newid0_textview0,R.id.newid0_textview1,R.id.newid0_textview2};
    private TextView[] textViews = new TextView[tvID.length];
    private Button button0;

    private InputMethodManager imm;
    private InputFilter inputFilter;

    private Boolean et0=false,et1=false,et2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newid0);

        init();
    }

    void init(){
        relativeLayout0 = (RelativeLayout) findViewById(R.id.newid0_relativelayout0);
        for(int i=0;i<etID.length;i++){
            editTexts[i] = (EditText) findViewById(etID[i]);
        }
        for(int i=0;i<tvID.length;i++){
            textViews[i] = (TextView) findViewById(tvID[i]);
        }
        button0 = (Button) findViewById(R.id.newid0_button0);

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

        relativeLayout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
            }
        });


        editTexts[0].setFilters(new InputFilter[]{inputFilter});
        editTexts[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTexts[0].getText().toString().length() >= 1){
                    et0=true;
                    if(et0&&et1&&et2){
                        button0.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    et0=false;
                    button0.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTexts[1].setFilters(new InputFilter[]{inputFilter});
        editTexts[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTexts[1].getText().toString().length() >= 6){
                    et1=true;
                    textViews[1].setVisibility(View.INVISIBLE);
                    if(et0&&et1&&et2){
                        button0.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    et1=false;
                    textViews[1].setVisibility(View.VISIBLE);
                    button0.setVisibility(View.INVISIBLE);
                }
                if(editTexts[1].getText().toString().equals(editTexts[2].getText().toString())){
                    et2=true;
                    textViews[2].setVisibility(View.INVISIBLE);
                    if(et0&&et1&&et2){
                        button0.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    et2=false;
                    textViews[2].setVisibility(View.VISIBLE);
                    button0.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTexts[2].setFilters(new InputFilter[]{inputFilter});
        editTexts[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTexts[1].getText().toString().equals(editTexts[2].getText().toString())){
                    et2=true;
                    textViews[2].setVisibility(View.INVISIBLE);
                    if(et0&&et1&&et2){
                        button0.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    et2=false;
                    textViews[2].setVisibility(View.VISIBLE);
                    button0.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hidekeyboard();
                    ServerTrans newID = new ServerTrans();
                    newID.newID(editTexts[0].getText().toString(), editTexts[1].getText().toString());
                    String result = newID.execute().get();
                    //result = "0";
                    switch (result){
                        case "0":
                            Intent intent0 = new Intent(NewID0Activity.this,NewID1Activity.class);
                            intent0.putExtra("ID", editTexts[0].getText().toString());
                            intent0.putExtra("PW", editTexts[1].getText().toString());
                            startActivity(intent0);
                            break;
                        case "1":
                            dialog_fail();
                            break;
                        default :
                            Toast.makeText(NewID0Activity.this,result,Toast.LENGTH_SHORT).show();
                            //dialog_neterror();
                            break;
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(NewID0Activity.this,"fail",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    void dialog_fail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입에 실패하였습니다");
        builder.setMessage("이미 있는 ID 입니다");
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
        imm.hideSoftInputFromWindow(editTexts[0].getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editTexts[1].getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editTexts[2].getWindowToken(), 0);
    }
}
