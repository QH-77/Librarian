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

import com.jl.librarian.zxing.activity.CaptureActivity;
import com.jl.dao.MyHelper;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity implements View.OnClickListener{
    MyHelper myHelper;
    TextView mTv_scan;
    EditText mEt_ISBN;
    EditText mEt_bookName;
    EditText mEt_bookAuthor;
    EditText mEt_bookPress;
    EditText mEt_bookPrice;
    EditText mEt_bookPublishDay;
    Spinner spinner;
    List<String> data_list;
    ArrayAdapter<String> arr_adapter;
    String str_bookStatus;
    Button mBt_insert;
    Button mBt_backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        myHelper=new MyHelper(this);
        initView();
    }

    public void initView(){
        mTv_scan=(TextView)findViewById(R.id.tv_scan);
        mEt_ISBN=(EditText)findViewById(R.id.et_isbn);
        mEt_bookName=(EditText)findViewById(R.id.et_bookName);
        mEt_bookAuthor=(EditText)findViewById(R.id.et_bookAuthor);
        mEt_bookPress=(EditText)findViewById(R.id.et_bookPress);
        mEt_bookPrice=(EditText)findViewById(R.id.et_bookPrice);
        mEt_bookPublishDay=(EditText)findViewById(R.id.et_bookPublishDay);
        mBt_insert=(Button)findViewById(R.id.btn_insert);
        mBt_backToMain=(Button)findViewById(R.id.btn_backToMain);
        spinner=(Spinner) findViewById(R.id.sp_bookStatus);
        data_list = new ArrayList<String>();
        data_list.add("未借出");
        data_list.add("已借出");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                str_bookStatus=arr_adapter.getItem(arg2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                str_bookStatus="未借出";
            }
        });
        mTv_scan.setOnClickListener(this);
        mBt_insert.setOnClickListener(this);
        mBt_backToMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String str_ISBN;
        String str_bookName;
        String str_bookAuthor;
        String str_bookPress;
        Double str_bookPrice;
        String str_bookPublishDay;

        switch(v.getId()){
            case R.id.tv_scan:
                activityJumpToScan();
                break;
            case R.id.btn_insert:
                str_ISBN=mEt_ISBN.getText().toString().trim();
                str_bookName=mEt_bookName.getText().toString().trim();
                str_bookAuthor=mEt_bookAuthor.getText().toString().trim();
                str_bookPress=mEt_bookPress.getText().toString().trim();
                str_bookPrice=Double.valueOf(mEt_bookPrice.getText().toString().trim());
                str_bookPublishDay=mEt_bookPublishDay.getText().toString().trim();
                if(TextUtils.isEmpty(str_ISBN)){
                    Toast.makeText(this, "请输入图书的ISBN!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkISBN(str_ISBN))
                {
                    Toast.makeText(this, "该图书已在库中！不能重复入库", Toast.LENGTH_SHORT).show();
                    mEt_ISBN.setText("");
                    mEt_bookName.setText("");
                    mEt_bookAuthor.setText("");
                    mEt_bookPress.setText("");
                    mEt_bookPrice.setText("");
                    mEt_bookPublishDay.setText("");
                }else{
                    insert(str_ISBN,str_bookName,str_bookAuthor,str_bookPress,str_bookPrice,str_bookPublishDay,str_bookStatus);
                    Toast.makeText(this, "该图书入库成功！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_backToMain:
                activityJumpToMain();
                break;
        }
    }
    public boolean checkISBN(String str_ISBN){
        SQLiteDatabase db;
        db=myHelper.getReadableDatabase();
        boolean result=false;
        Cursor cursor=db.query("book",null,"ISBN=?",new String[]{str_ISBN},
                null,null,null);
        if(cursor.getCount()!=0){
            result=true;
        }
        cursor.close();
        db.close();
        return result;
    }
    public void insert(String str_ISBN1,String str_bookName,String str_bookAuthor,String str_bookPress,
                       Double str_bookPrice,String str_bookPublishDay,String str_bookStatus){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("ISBN",str_ISBN1);
        values.put("Bname",str_bookName);
        values.put("Bauthor",str_bookAuthor);
        values.put("Bpress",str_bookPress);
        values.put("Bprice",str_bookPrice);
        values.put("BpublishDay",str_bookPublishDay);
        values.put("Bstatus",str_bookStatus);
        db.insert("book",null,values);
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
                mEt_ISBN.setText(bundle.getString("result"));
            }
        }
    }
}
