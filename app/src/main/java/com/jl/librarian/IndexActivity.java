package com.jl.librarian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener{
    TextView mTv_currentTime;
    TextView mTv_addBook;
    TextView mTv_bookLend;
    TextView mTv_bookReturn;
    TextView mTv_bookQuery;
    TextView mTv_bookProtection;
    TextView mTv_record;
    TextView mTv_Internet;
    TextView mTv_system;
    TextView mTv_exit;

    String str_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
    }
    protected void onDestroy(){
        super.onDestroy();
    }
    public void initView(){
        mTv_currentTime=(TextView)findViewById(R.id.tv_currentTime);
        mTv_addBook=(TextView)findViewById(R.id.tv_AddBook);
        mTv_bookLend=(TextView)findViewById(R.id.tv_BookLend);
        mTv_bookReturn=(TextView)findViewById(R.id.tv_BookReturn);
        mTv_bookQuery=(TextView)findViewById(R.id.tv_BookQuery);
        mTv_bookProtection=(TextView)findViewById(R.id.tv_BookProtection);
        mTv_record=(TextView)findViewById(R.id.tv_Record);
        mTv_Internet=(TextView)findViewById(R.id.tv_Internet);
        mTv_system=(TextView)findViewById(R.id.tv_System);
        mTv_exit=(TextView)findViewById(R.id.tv_Exit);

        str_user=getUser();

        mTv_currentTime.setOnClickListener(this);
        mTv_addBook.setOnClickListener(this);
        mTv_bookLend.setOnClickListener(this);
        mTv_bookReturn.setOnClickListener(this);
        mTv_bookQuery.setOnClickListener(this);
        mTv_bookProtection.setOnClickListener(this);
        mTv_record.setOnClickListener(this);
        mTv_Internet.setOnClickListener(this);
        mTv_system.setOnClickListener(this);
        mTv_exit.setOnClickListener(this);

    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.tv_AddBook:
                activityJumpToAddBook();
                break;
            case R.id.tv_BookLend:
                activityJumpToBookLend();
                break;
            case R.id.tv_BookReturn:
                activityJumpToBookReturn();
                break;
            case R.id.tv_BookQuery:
                activityJumpToBookQuery();
                break;
            case R.id.tv_BookProtection:
                activityJumpToBookProtection();
                break;
            case R.id.tv_Record:
                activityJumpToRecord();
                break;
            case R.id.tv_Internet:
                activityJumpToInternet();
                break;
            case R.id.tv_System:
                activityJumpToSystem();
                break;
            case R.id.tv_Exit:
                new AlertDialog.Builder(this)
                        .setTitle("提示：")
                        .setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(new String[]{"注销", "退出"}, -1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i==0)
                                        {
                                            activityJumpToLogin();
                                            onDestroy();
                                        }else if(i==1)
                                        {
                                            System.exit(0);
                                        }else{
                                            dialogInterface.dismiss();
                                        }
                                    }
                                })
                        .setNegativeButton("取消",null)
                        .show();

                break;
        }
    }
    public String getUser(){
        Intent intent=getIntent();
        return intent.getStringExtra("name");
    }
    public void activityJumpToAddBook(){
        Intent intent1=new Intent(this,AddBookActivity.class);
        startActivity(intent1);
    }
    public void activityJumpToBookLend(){
        Intent intent2=new Intent(this,BookLendActivity.class);
        startActivity(intent2);
    }
    public void activityJumpToBookReturn(){
        Intent intent3=new Intent(this,BookReturnActivity.class);
        startActivity(intent3);
    }
    public void activityJumpToBookQuery(){
        Intent intent4=new Intent(this,BookQueryActivity.class);
        startActivity(intent4);
    }
    public void activityJumpToBookProtection(){
        Intent intent5=new Intent(this,BookProtectionActivity.class);
        startActivity(intent5);
    }
    public void activityJumpToRecord(){
        Intent intent6=new Intent(this,RecordActivity.class);
        startActivity(intent6);
    }
    public void activityJumpToInternet(){
        Intent intent7=new Intent();
        intent7.setAction("android.intent.action.VIEW");
        intent7.setData(Uri.parse("http://club.99read.com"));
        startActivity(intent7);
    }
    public void activityJumpToSystem(){
        Intent intent8=new Intent(this,SystemActivity.class);
        intent8.putExtra("name",str_user);
        startActivity(intent8);
    }
    public void activityJumpToLogin(){
        Intent intent9=new Intent(this,LoginActivity.class);
        startActivity(intent9);
    }
}