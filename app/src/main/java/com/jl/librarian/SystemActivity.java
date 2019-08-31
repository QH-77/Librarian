package com.jl.librarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SystemActivity extends AppCompatActivity implements View.OnClickListener{
    Button mBt_addUser;
    Button mBt_queryUser;
    Button mBt_deleteUser;
    Button mBt_updatePwd;

    String str_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        initView();
    }
    public void initView(){
        mBt_addUser=(Button)findViewById(R.id.btn_addUser);
        mBt_queryUser=(Button)findViewById(R.id.btn_queryUser);
        mBt_deleteUser=(Button)findViewById(R.id.btn_deleteUser);
        mBt_updatePwd=(Button)findViewById(R.id.btn_updatePwd);
        str_user=getUser();
        mBt_addUser.setOnClickListener(this);
        mBt_queryUser.setOnClickListener(this);
        mBt_deleteUser.setOnClickListener(this);
        mBt_updatePwd.setOnClickListener(this);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_addUser:
                startActivity(new Intent(this,AddUserActivity.class));
                break;
            case R.id.btn_queryUser:
                startActivity(new Intent(this,QueryUserActivity.class));
                break;
            case R.id.btn_deleteUser:
                startActivity(new Intent(this,DeleteUserActivity.class));
                break;
            case R.id.btn_updatePwd:
                startActivity(new Intent(this,UpdatePwdActivity.class).putExtra("name",str_user));
                break;
        }
    }
    public String getUser(){
        Intent intent=getIntent();
        return intent.getStringExtra("name");
    }
}
