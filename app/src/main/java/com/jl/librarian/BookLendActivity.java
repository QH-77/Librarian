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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jl.dao.MyHelper;
import com.jl.librarian.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookLendActivity extends AppCompatActivity implements View.OnClickListener{
    static MyHelper myHelper;
    EditText mEt_ISBN;
    TextView mTv_scan;
    TextView mTv_getBookName;
    EditText mEt_readerName;
    Spinner mSp_readerSex;
    List<String> data_list;
    ArrayAdapter<String> arr_adapter;
    String str_readerSex;
    EditText mEt_readerTel;
    TextView mTv_getDate;
    EditText mEt_remark;
    Button mBt_sure;
    Button mBt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_lend);
        myHelper=new MyHelper(this);
        initView();
    }
    public void initView(){
        mEt_ISBN=(EditText)findViewById(R.id.et_isbn);
        mTv_scan=(TextView)findViewById(R.id.tv_scan);
        mTv_getBookName=(TextView)findViewById(R.id.tv_getBookName);
        mEt_readerName=(EditText)findViewById(R.id.et_readerName);
        mEt_readerTel=(EditText)findViewById(R.id.et_readerTel);
        mTv_getDate=(TextView)findViewById(R.id.tv_getDate);
        getDate();
        mEt_remark=(EditText)findViewById(R.id.et_remark);
        mBt_sure=(Button)findViewById(R.id.btn_sure);
        mBt_back=(Button)findViewById(R.id.btn_backToMain);
        mSp_readerSex=(Spinner) findViewById(R.id.sp_readerSex);
        data_list = new ArrayList<String>();
        data_list.add("男");
        data_list.add("女");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSp_readerSex.setAdapter(arr_adapter);
        mSp_readerSex.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                str_readerSex=arr_adapter.getItem(arg2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                str_readerSex="男";
            }
        });
        mTv_scan.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);
        mBt_back.setOnClickListener(this);
    }
    public void onClick(View v){
        String str_ISBN;
        String str_bookName;
        String str_readerName;
        String str_readerTel;
        String str_date;
        String str_remark;
        switch(v.getId()){
            case R.id.tv_scan:
                activityJumpToScan();
                break;
            case R.id.btn_sure:
                str_ISBN=mEt_ISBN.getText().toString().trim();
                str_bookName=mTv_getBookName.getText().toString().trim();
                str_readerName=mEt_readerName.getText().toString().trim();
                str_readerTel=mEt_readerTel.getText().toString().trim();
                str_date=mTv_getDate.getText().toString().trim();
                str_remark=mEt_remark.getText().toString().trim();
                if(TextUtils.isEmpty(str_ISBN)){
                    Toast.makeText(this, "请输入ISBN号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(str_readerName)){
                    Toast.makeText(this, "请输入借阅者姓名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(str_readerTel)){
                    Toast.makeText(this, "请输入借阅者联系电话！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkStatus(str_ISBN)){
                    Toast.makeText(this, "该图书已不在库中！不能借出", Toast.LENGTH_SHORT).show();
                }else{
                    insertRecord(str_ISBN,str_bookName,str_readerName,str_readerSex,str_readerTel,str_date,str_remark);
                    Toast.makeText(this, "图书借出成功！已生成借阅信息", Toast.LENGTH_SHORT).show();
                    updateStatus(str_ISBN);
                }
                break;
            case R.id.btn_backToMain:
                activityJumpToMain();
                break;
        }
    }

    public void getDate(){
        Calendar calendar=Calendar.getInstance();
        String year=String.valueOf(calendar.get(Calendar.YEAR));
        String month=String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day=String.valueOf(calendar.get(Calendar.DATE));
        String time=year+"-"+month+"-"+day;
        mTv_getDate.setText(time);
    }

    public void getBookName(String str_ISBN){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("book",null,"ISBN=?",new String[]{str_ISBN},
                null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            mTv_getBookName.setText(cursor.getString(1));
        }
        db.close();
        cursor.close();
    }

    public void insertRecord(String str_ISBN,String str_bookName,String str_readerName,String str_readerSex,
                             String str_readerTel,String str_date,String str_remark){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("ISBN",str_ISBN);
        values.put("Bname",str_bookName);
        values.put("Rname",str_readerName);
        values.put("Rsex",str_readerSex);
        values.put("Rtel",str_readerTel);
        values.put("Rdate",str_date);
        values.put("Rremark",str_remark);
        db.insert("record",null,values);
        db.close();
    }

    public boolean checkStatus(String str_ISBN){
        boolean result=false;
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("book",null,"ISBN=? and Bstatus=?",new String[]{str_ISBN,"已借出"},
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
        values.put("Bstatus","已借出");
        db.update("book",values,"ISBN=?",new String[]{str_ISBN});
        db.close();
    }

    public void activityJumpToScan(){
        Intent intent=new Intent(this,CaptureActivity.class);
        startActivityForResult(intent,1);
    }
    public void activityJumpToMain(){
        Intent intent1=new Intent(this,IndexActivity.class);
        startActivity(intent1);
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
                getBookName(result);
            }
        }
    }
}
