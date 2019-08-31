package com.jl.librarian;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jl.dao.MyHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    MyHelper myHelper;
    EditText mEt_user;
    EditText mEt_pwd;
    Button mBt_login;

    String str_user=null;
    String str_pwd=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myHelper = new MyHelper(this);
        initView();
    }
    protected void onDestroy(){
        super.onDestroy();
    }
    public void initView(){
        mEt_user = (EditText) findViewById(R.id.et_user);
        mEt_pwd = (EditText) findViewById(R.id.et_pwd);
        mBt_login = (Button) findViewById(R.id.btn_login);
        mBt_login.setOnClickListener(this);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_login:
                str_user=mEt_user.getText().toString().trim();
                str_pwd=mEt_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(str_user)){
                    Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(str_pwd)){
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkLogin(str_user,str_pwd))
                {
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    activityJumpToMainFrame();
                    onDestroy();
                }else {
                    if(checkUser(str_user)){
                        Toast.makeText(this, "您的密码错误！", Toast.LENGTH_SHORT).show();
                        mEt_pwd.setText("");
                    }else{
                        Toast.makeText(this, "无此账户，请注册！", Toast.LENGTH_SHORT).show();
                        mEt_user.setText("");
                        mEt_pwd.setText("");
                    }
                }
                break;
        }
    }
    public void activityJumpToMainFrame(){
        Intent intent=new Intent(this,IndexActivity.class);
        intent.putExtra("name",str_user);
        startActivity(intent);
    }

    public boolean checkLogin(String str_user,String str_pwd){
        SQLiteDatabase db;
        db=myHelper.getReadableDatabase();
        boolean result=false;
        Cursor cursor=db.query("user",null,"Uname=? and Upassword=?",
                new String[]{str_user,str_pwd},null,null,null);
        if(cursor.getCount()!=0){
            result=true;
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean checkUser(String str_user){
        SQLiteDatabase db;
        db=myHelper.getReadableDatabase();
        boolean result=false;
        Cursor cursor=db.query("user",null,"Uname=?",new String[]{str_user},
                null,null,null);
        if(cursor.getCount()!=0){
            result=true;
        }
        cursor.close();
        db.close();
        return result;
    }
}
