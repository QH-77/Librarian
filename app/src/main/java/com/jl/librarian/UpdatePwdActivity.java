package com.jl.librarian;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jl.dao.MyHelper;

public class UpdatePwdActivity extends AppCompatActivity implements View.OnClickListener{
    MyHelper myHelper;
    TextView mTv_getName;
    TextView mTv_getQuestion1;
    EditText mEt_answer1;
    TextView mTv_getQuestion2;
    EditText mEt_answer2;
    EditText mEt_formerPwd;
    EditText mEt_newPwd;
    Button mBt_sure;
    Button mBt_back;
    String str_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        myHelper=new MyHelper(this);
        initView();
    }
    public void initView(){
        mTv_getName=(TextView)findViewById(R.id.tv_getName);
        mTv_getQuestion1=(TextView)findViewById(R.id.tv_getquestion1);
        mEt_answer1=(EditText)findViewById(R.id.et_answer1);
        mTv_getQuestion2=(TextView)findViewById(R.id.tv_getquestion2);
        mEt_answer2=(EditText)findViewById(R.id.et_answer2);
        mEt_formerPwd=(EditText)findViewById(R.id.et_formerPwd);
        mEt_newPwd=(EditText)findViewById(R.id.et_newPwd);
        mBt_sure=(Button)findViewById(R.id.btn_sure);
        mBt_back=(Button)findViewById(R.id.btn_back);

        str_user=getUser();
        mTv_getName.setText(str_user);
        getQuestion(str_user);

        mBt_sure.setOnClickListener(this);
        mBt_back.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_sure:
                String str_answer1=mEt_answer1.getText().toString().trim();
                String str_answer2=mEt_answer2.getText().toString().trim();
                String str_formerPwd=mEt_formerPwd.getText().toString().trim();
                String str_newPwd=mEt_newPwd.getText().toString().trim();
                if(checkAnswer(str_answer1,str_answer2))
                {
                    if(checkFormerPwd(str_formerPwd))
                    {
                        updatePwd(str_user,str_newPwd);
                    }else{
                        Toast.makeText(this, "原密码验证错误！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "问题验证不通过！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_back:
                startActivity(new Intent(this,SystemActivity.class));
                break;
        }
    }
    public String getUser(){
        Intent intent=getIntent();
        return intent.getStringExtra("name");
    }
    public void getQuestion(String str_user){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("user",null,"Uname=?",new String[]{str_user},
                null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            mTv_getQuestion1.setText(cursor.getString(2));
            mTv_getQuestion2.setText(cursor.getString(4));
        }
        cursor.close();
        db.close();
    }
    public boolean checkAnswer(String str_answer1,String str_answer2){
        boolean result=false;
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("user",null,"Uanswer1=? and Uanswer2=?",
                    new String[]{str_answer1,str_answer2},null,null,null);
        if(cursor.getCount()!=0){
            result=true;
        }
        cursor.close();
        db.close();
        return result;
    }
    public boolean checkFormerPwd(String str_formerPwd){
        boolean result=false;
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("user",null,"Upassword=?",
                new String[]{str_formerPwd},null,null,null);
        if(cursor.getCount()!=0){
            result=true;
        }
        cursor.close();
        db.close();
        return result;
    }
    public void updatePwd(String str_user,String str_newPwd){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Upassword",str_newPwd);
        db.update("user",values,"Uname=?",new String[]{str_user});
        Toast.makeText(this, "密码修改成功！", Toast.LENGTH_SHORT).show();
        db.close();
    }
}
