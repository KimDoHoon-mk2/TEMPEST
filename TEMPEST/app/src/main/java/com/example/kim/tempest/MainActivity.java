package com.example.kim.tempest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startActivity(new Intent(MainActivity.this,LoginPageActivity.class));
        overridePendingTransition(0,0);

        startActivity(new Intent(MainActivity.this,StartPageActivity.class));
        overridePendingTransition(0,0);

        finish();
    }
}
