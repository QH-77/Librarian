package com.jl.librarian;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jl.dao.MyHelper;
import com.jl.librarian.zxing.activity.CaptureActivity;

public class BookReturnActivity extends AppCompatActivity implements View.OnClickListener{
    MyHelper myHelper;
    EditText mEt_ISBN;
    TextView mTv_scan;
    TextView mTv_getBookName;
    TextView mTv_getReaderName;
    TextView mTv_getReaderSex;
    TextView mTv_getReaderTel;
    TextView mTv_getDate;
    TextView mTv_getRemark;
    Button mBt_sure;
    Button mBt_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_return);
        myHelper=new MyHelper(this);
        initView();
    }
    public void initView(){
        mEt_ISBN=(EditText)findViewById(R.id.et_isbn);
        mTv_scan=(TextView)findViewById(R.id.tv_scan);
        mTv_getBookName=(TextView)findViewById(R.id.tv_getBookName);
        mTv_getReaderName=(TextView)findViewById(R.id.tv_getReaderName);
        mTv_getReaderSex=(TextView)findViewById(R.id.tv_getReaderSex);
        mTv_getReaderTel=(TextView)findViewById(R.id.tv_getReaderTel);
        mTv_getDate=(TextView)findViewById(R.id.tv_getDate);
        mTv_getRemark=(TextView)findViewById(R.id.tv_getRemark);
        mBt_sure=(Button)findViewById(R.id.btn_sure);
        mBt_back=(Button)findViewById(R.id.btn_backToMain);
        mTv_scan.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);
        mBt_back.setOnClickListener(this);
    }
    public void onClick(View v){
        String str_ISBN;
        switch(v.getId()){
            case R.id.tv_scan:
                activityJumpToScan();
                break;
            case R.id.btn_sure:
                str_ISBN=mEt_ISBN.getText().toString().trim();
                if(TextUtils.isEmpty(str_ISBN)){
                    Toast.makeText(this, "请输入图书的ISBN号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkStatus(str_ISBN)){
                    Toast.makeText(this, "该图书未借出或已归还！", Toast.LENGTH_SHORT).show();
                }else{
                    updateStatus(str_ISBN);
                    Toast.makeText(this, "图书归还操作成功！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_backToMain:
                activityJumpToMain();
                break;
        }
    }
    public void activityJumpToScan(){
        Intent intent=new Intent(this,CaptureActivity.class);
        startActivityForResult(intent,1);
    }
    public void activityJumpToMain(){
        Intent intent1=new Intent(this,IndexActivity.class);
        startActivity(intent1);
    }
    public void getInfo(String str_ISBN){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("record",null,"ISBN=?",new String[]{str_ISBN},
                null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            mTv_getBookName.setText(cursor.getString(1));
            mTv_getReaderName.setText(cursor.getString(2));
            mTv_getReaderSex.setText(cursor.getString(3));
            mTv_getReaderTel.setText(cursor.getString(4));
            mTv_getDate.setText(cursor.getString(5));
            mTv_getRemark.setText(cursor.getString(6));
        }else{
            Toast.makeText(this, "无该图书的借出记录！", Toast.LENGTH_SHORT).show();
            mEt_ISBN.setText("");
        }
        cursor.close();
        db.close();
    }

    public boolean checkStatus(String str_ISBN){
        boolean result=false;
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("book",null,"ISBN=? and Bstatus=?",new String[]{str_ISBN,"未借出"},
                null,null,null);
        if(cursor.getCount()!=0){
            result=true;
        }
        db.close();
        cursor.close();
        return result;
    }

    public void updateStatus(String str_ISBN){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Bstatus","未借出");
        db.update("book",values,"ISBN=?",new String[]{str_ISBN});
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==1)
            {
                Bundle bundle=data.getExtras();
                String result=bundle.getString("result");
                mEt_ISBN.setText(result);
                getInfo(result);
            }
        }
    }
}
