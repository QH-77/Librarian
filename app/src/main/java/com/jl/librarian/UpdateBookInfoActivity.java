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

public class UpdateBookInfoActivity extends AppCompatActivity implements View.OnClickListener{
    MyHelper myHelper;
    TextView mTv_getISBN;
    EditText mEt_bookName;
    EditText mEt_bookAuthor;
    EditText mEt_bookPress;
    EditText mEt_bookPrice;
    EditText mEt_bookPublishDay;
    TextView mTv_getBookStatus;
    Button mBt_updateBook;
    Button mBt_cancel;

    String str_ISBN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book_info);
        myHelper=new MyHelper(this);
        initView();
    }
    public void initView(){
        mTv_getISBN=(TextView)findViewById(R.id.tv_getISBN);
        str_ISBN=getISBN();
        mTv_getISBN.setText(str_ISBN);
        mEt_bookName=(EditText)findViewById(R.id.et_bookName);
        mEt_bookAuthor=(EditText)findViewById(R.id.et_bookAuthor);
        mEt_bookPress=(EditText)findViewById(R.id.et_bookPress);
        mEt_bookPrice=(EditText)findViewById(R.id.et_bookPrice);
        mEt_bookPublishDay=(EditText)findViewById(R.id.et_bookPublishDay);
        mTv_getBookStatus=(TextView)findViewById(R.id.tv_getBookStatus);
        getBookInfo(str_ISBN);
        mBt_updateBook=(Button)findViewById(R.id.btn_updateBook);
        mBt_cancel=(Button)findViewById(R.id.btn_cancel);
        mBt_updateBook.setOnClickListener(this);
        mBt_cancel.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_updateBook:
                String str_bookName=mEt_bookName.getText().toString().trim();
                String str_bookAuthor=mEt_bookAuthor.getText().toString().trim();
                String str_bookPress=mEt_bookPress.getText().toString().trim();
                double str_bookPrice=Double.valueOf(mEt_bookPrice.getText().toString().trim());
                String str_bookPublishDay=mEt_bookPublishDay.getText().toString().trim();
                updateBook(str_ISBN,str_bookName,str_bookAuthor,str_bookPress,str_bookPrice,str_bookPublishDay);
                Toast.makeText(this, "图书信息修改成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                startActivity(new Intent(this,BookProtectionActivity.class));
                break;
        }
    }
    public String getISBN(){
        Intent intent=getIntent();
        return intent.getStringExtra("isbn");
    }
    public void getBookInfo(String str_ISBN){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("book",null,"ISBN=?",new String[]{str_ISBN},
                null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            mEt_bookName.setText(cursor.getString(1));
            mEt_bookAuthor.setText(cursor.getString(2));
            mEt_bookPress.setText(cursor.getString(3));
            mEt_bookPrice.setText(cursor.getString(4));
            mEt_bookPublishDay.setText(cursor.getString(5));
            mTv_getBookStatus.setText(cursor.getString(6));
        }
        cursor.close();
        db.close();
    }
    public void updateBook(String str_ISBN,String str_bookName,String str_bookAuthor,
                           String str_bookPress,double str_bookPrice,String str_bookPublishDay){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Bname",str_bookName);
        values.put("Bauthor",str_bookAuthor);
        values.put("Bpress",str_bookPress);
        values.put("Bprice",str_bookPrice);
        values.put("BpublishDay",str_bookPublishDay);
        db.update("book",values,"ISBN=?",new String[]{str_ISBN});
        db.close();
    }
}
