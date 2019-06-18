package com.example.kim.tempest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class OTPActivity extends AppCompatActivity {

    private TextView textView0,textView1;
    private EditText editText;
    private Button button;

    private Runnable runnable;
    private Handler handler = new Handler();
    private TimerTask timerTask;
    private Timer timer;

    private Intent intent;
    private String ID;
    private String LOCK_ID;
    private String PAGE;
    private String NUM;
    private String TYPE;
    private String NEWID;
    private String TIME;
    private String result;

    private static int count = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);

        init();
    }

    void init(){
        textView0 = (TextView) findViewById(R.id.otp_textview0);
        textView1 = (TextView) findViewById(R.id.otp_textview1);
        editText = (EditText) findViewById(R.id.otp_edittext);
        button = (Button) findViewById(R.id.otp_button);

        intent = getIntent();
        ID = intent.getStringExtra("ID");
        LOCK_ID = intent.getStringExtra("LOCK_ID");
        PAGE = intent.getStringExtra("PAGE");

        textView0.setText(intent.getStringExtra("OTP"));

        count=30;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,1000);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (PAGE) {
                        case "0":
                            NUM = intent.getStringExtra("NUM");
                            TYPE = intent.getStringExtra("TYPE");
                            ServerTrans checkOTP0 = new ServerTrans();
                            checkOTP0.authOTP(ID, editText.getText().toString(), LOCK_ID, NUM, TYPE);
                            result = checkOTP0.execute().get();
                            switch (result) {
                                case "0":
                                    toast_auth();
                                    timer.cancel();
                                    Intent intent0 = new Intent(OTPActivity.this, HomePageActivity.class);
                                    intent0.addFlags(intent0.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent0.putExtra("SELECT", 2);
                                    startActivity(intent0);
                                    break;
                                case "1":
                                    dialog_fail();
                                    break;
                                default:
                                    dialog_neterror();
                                    break;
                            }
                            break;
                        case "1":
                            ServerTrans checkOTP1 = new ServerTrans();
                            checkOTP1.openOTP(ID, editText.getText().toString(), LOCK_ID);
                            result = checkOTP1.execute().get();
                            switch (result) {
                                case "0":
                                    toast_auth();
                                    timer.cancel();
                                    Intent intent0 = new Intent(OTPActivity.this, HomePageActivity.class);
                                    intent0.addFlags(intent0.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent0.putExtra("SELECT", 2);
                                    startActivity(intent0);
                                    break;
                                case "1":
                                    dialog_fail();
                                    break;
                                default:
                                    dialog_neterror();
                                    break;
                            }
                            break;
                        case "2":
                            NEWID = intent.getStringExtra("NEWID");
                            TIME = intent.getStringExtra("TIME");
                            ServerTrans checkOTP2 = new ServerTrans();
                            checkOTP2.plusUserOTP(ID, editText.getText().toString(), LOCK_ID, NEWID, TIME);
                            result = checkOTP2.execute().get();
                            switch (result) {
                                case "0":
                                    toast_auth();
                                    timer.cancel();
                                    finish();
                                    break;
                                case "1":
                                    dialog_fail();
                                    break;
                                default:
                                    dialog_neterror();
                                    break;
                            }
                            break;
                        case "3":
                            NUM = intent.getStringExtra("NUM");
                            TYPE = intent.getStringExtra("TYPE");
                            ServerTrans checkOTP3 = new ServerTrans();
                            checkOTP3.plusTimeOTP(ID, editText.getText().toString(), LOCK_ID, NUM, TYPE);
                            result = checkOTP3.execute().get();
                            switch (result) {
                                case "0":
                                    toast_auth();
                                    timer.cancel();
                                    Intent intent0 = new Intent(OTPActivity.this, HomePageActivity.class);
                                    intent0.addFlags(intent0.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent0.putExtra("SELECT", 2);
                                    startActivity(intent0);
                                    break;
                                case "1":
                                    dialog_fail();
                                    break;
                                default:
                                    dialog_neterror();
                                    break;
                            }
                            break;
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (PAGE){
            case "0":
                timer.cancel();
                finish();
                break;
            case "1":
                timer.cancel();
                Intent intent0 = new Intent(OTPActivity.this,HomePageActivity.class);
                intent0.addFlags(intent0.FLAG_ACTIVITY_CLEAR_TOP);
                intent0.putExtra("SELECT",2);
                startActivity(intent0);
                finish();
                break;
            case "2":
                timer.cancel();
                finish();
                break;
            case "3":
                timer.cancel();
                finish();
                break;
        }
    }

    private void update(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                textView1.setText(String.valueOf(count));
                if(count==0) {
                    timer.cancel();
                    finish();
                }
                count--;
            }
        };
        handler.post(runnable);
    }

    void toast_auth(){
        Toast.makeText(OTPActivity.this, "인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
    }

    void dialog_fail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("OTP 인증 실패");
        builder.setMessage("OTP 번호가 맞지 않습니다.");
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
}