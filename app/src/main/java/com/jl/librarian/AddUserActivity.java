package com.jl.librarian;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jl.dao.MyHelper;
import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener{
    MyHelper myHelper;
    EditText mEt_name;
    EditText mEt_password;
    Spinner spinner1;
    List<String> data_list1;
    ArrayAdapter<String> arr_adapter1;
    EditText mEt_answer1;
    Spinner spinner2;
    List<String> data_list2;
    ArrayAdapter<String> arr_adapter2;
    EditText mEt_answer2;
    Button mBt_add;
    Button mBt_back;

    String str_question1;
    String str_question2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        myHelper=new MyHelper(this);
        initView();
    }
    public void initView(){
        mEt_name=(EditText)findViewById(R.id.et_name);
        mEt_password=(EditText) findViewById(R.id.et_password);
        mEt_answer1=(EditText) findViewById(R.id.et_answer1);
        mEt_answer2=(EditText) findViewById(R.id.et_answer2);

        spinner1=(Spinner) findViewById(R.id.sp_question1);
        data_list1 = new ArrayList<String>();
        data_list1.add("你最尊敬的人的名字是？");
        data_list1.add("对你意义重大的数字是？");
        data_list1.add("你喜欢的人的名字是？");
        //适配器
        arr_adapter1= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list1);
        //设置样式
        arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner1.setAdapter(arr_adapter1);
        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                str_question1=arr_adapter1.getItem(arg2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                str_question1=null;
            }
        });

        spinner2=(Spinner) findViewById(R.id.sp_question2);
        data_list2 = new ArrayList<String>();
        data_list2.add("你的爱好？");
        data_list2.add("你的学号？");
        data_list2.add("你的高中班主任的名字是？");
        //适配器
        arr_adapter2= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list2);
        //设置样式
        arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner2.setAdapter(arr_adapter2);
        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                str_question2=arr_adapter2.getItem(arg2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                str_question2=null;
            }
        });

        mBt_add=(Button)findViewById(R.id.btn_add);
        mBt_back=(Button)findViewById(R.id.btn_back);
        mBt_add.setOnClickListener(this);
        mBt_back.setOnClickListener(this);
    }
    public void onClick(View v){
        String str_name;
        String str_password;
        String str_answer1;
        String str_answer2;
        switch(v.getId()){
            case R.id.btn_add:
                str_name=mEt_name.getText().toString().trim();
                str_password=mEt_password.getText().toString().trim();
                str_answer1=mEt_answer1.getText().toString().trim();
                str_answer2=mEt_answer2.getText().toString().trim();
                if(TextUtils.isEmpty(str_name)){
                    Toast.makeText(this, "请输入昵称！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(str_password)){
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkUser(str_name)){
                    Toast.makeText(this, "此昵称已存在！", Toast.LENGTH_SHORT).show();
                    mEt_name.setText("");
                }else{
                    register(str_name,str_password,str_question1,str_answer1,str_question2,str_answer2);
                    Toast.makeText(this, "添加用户成功！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_back:
                startActivity(new Intent(this,SystemActivity.class));
                break;
        }
    }
    public void register(String str_name,String str_password,String str_question1,String str_answer1,
                         String str_question2,String str_answer2){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Uname",str_name);
        values.put("Upassword",str_password);
        values.put("Uquestion1",str_question1);
        values.put("Uanswer1",str_answer1);
        values.put("Uquestion2",str_question2);
        values.put("Uanswer2",str_answer2);
        db.insert("user",null,values);
        db.close();
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
