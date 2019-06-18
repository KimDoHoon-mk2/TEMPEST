package com.example.kim.tempest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomePageActivity extends AppCompatActivity {

    private HomePageFragment0 fragment0;
    private HomePageFragment1 fragment1;

    private RelativeLayout relativeLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button button1;
    private TextView textView;

    private Intent intent;
    private String ID = "";
    private String NAME;
    private int state=1;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        init();
    }

    void init(){
        relativeLayout = (RelativeLayout) findViewById(R.id.homepage_relativelayout);
        viewPager = (ViewPager) findViewById(R.id.homepage_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.homepage_tablayout);
        button1 = (Button) findViewById(R.id.homepage_button1);
        textView = (TextView) findViewById(R.id.homepage_textview0);

        intent = getIntent();

        pref = getSharedPreferences("data",Activity.MODE_PRIVATE);
        ID = pref.getString("saveID",null);
        NAME = pref.getString("saveNAME",null);
        textView.setText(NAME);

        fragment0 = new HomePageFragment0();
        fragment1 = new HomePageFragment1();

        tabLayout.addTab(tabLayout.newTab().setText("보관함 찾기"));
        tabLayout.addTab(tabLayout.newTab().setText("사용중인 보관함"));

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_logout();
            }
        });
    }

    public class TabPagerAdapter extends FragmentPagerAdapter {
        private int tabCount;

        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragment0;
                case 1:
                    return fragment1;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    @Override
    public void onBackPressed() {
        dialog_exit();
    }

    @Override
    public void onResume() {
        super.onResume();
        state = intent.getIntExtra("SELECT",1);
        switch (state){
            case 0:
                TabLayout.Tab tab0 = tabLayout.getTabAt(0);
                tab0.select();
                break;
            case 1:
                TabLayout.Tab tab1 = tabLayout.getTabAt(1);
                tab1.select();
                break;
        }
    }

    public String ID(){
        return ID;
    }

    void dialog_logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃 하시겠습니까?");
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
                        pref = getSharedPreferences("data",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent0 = new Intent(HomePageActivity.this,LoginPageActivity.class);
                        startActivity(intent0);
                        finish();
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

    void dialog_select(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialognumberpicker, (ViewGroup)findViewById(R.id.dialognumberpicker_layout));
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
                        String str = null;
                        switch(numberPicker1.getValue()){
                            case 0:
                                str = "초";
                                break;
                            case 1:
                                str = "분";
                                break;
                            case 2:
                                str = "시간";
                                break;
                            case 3:
                                str = "일";
                                break;
                        }
                        Toast.makeText(HomePageActivity.this,num+str,Toast.LENGTH_SHORT).show();
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
}
