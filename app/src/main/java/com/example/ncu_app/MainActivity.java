package com.example.ncu_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Switch btn_switch;
    private boolean isEnglish;
    private EditText et_userName;
    private Button btn_start;
    private TextView tv_Chinese, tv_English;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();

        //set the switch listener
        btn_switch = findViewById(R.id.btn_switch);
        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isEnglish = true;
                }else {
                    isEnglish = false;
                }
                Log.i("---onCheckedChanged---", String.valueOf(isChecked));
                updateUI();
            }
        });

        //get the switch status
        if(btn_switch.isChecked()){
            isEnglish = true;
        }else {
            isEnglish = false;
        }

        tv_Chinese = findViewById(R.id.tv_Chinese);
        tv_English = findViewById(R.id.tv_English);
        et_userName = findViewById(R.id.et_inputName);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                Bundle bundle = new Bundle();
                String userName = et_userName.getText().toString().trim();
                if(!userName.isEmpty()){
                    et_userName.setText("");
                    bundle.putString("userName", userName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Please input your name", Toast.LENGTH_LONG).show();
                }

            }
        });

        initUI();
    }

    private void initUI(){
        if(isEnglish){
            actionBar.setTitle("Your Name");
            et_userName.setHint("UserName");
            btn_start.setText("Start");
            tv_Chinese.setText("Chinese");
            tv_English.setText("English");
        }else {
            actionBar.setTitle("你的名字");
            et_userName.setHint("使用者名稱");
            btn_start.setText("開始");
            tv_Chinese.setText("中文");
            tv_English.setText("英文");
        }
    }

    private void updateUI(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initUI();
            }
        });

    }

}
