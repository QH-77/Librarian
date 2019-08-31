package com.jl.librarian;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jl.dao.MyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookProtectionActivity extends AppCompatActivity implements View.OnClickListener{
    MyHelper myHelper;
    Spinner mSp_condition;
    List<String> data_list;
    ArrayAdapter<String> arr_adapter;
    String str_condition;
    EditText mEt_condition;
    Button mBt_query;
    Button mBt_queryAllBook;
    ListView mLv_bookInfo;
    List<Map<String, Object>> data;
    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_query);
        myHelper=new MyHelper(this);
        initView();
    }
    public void initView(){
        mEt_condition=(EditText)findViewById(R.id.et_condition);
        mBt_query=(Button)findViewById(R.id.btn_query);
        mBt_queryAllBook=(Button)findViewById(R.id.btn_queryAllBook);
        mLv_bookInfo=(ListView)findViewById(R.id.lv_bookInfo);
        data = new ArrayList<Map<String, Object>>();
        bookAdapter = new BookAdapter(this, data);
        mSp_condition=(Spinner)findViewById(R.id.sp_condition);
        data_list = new ArrayList<String>();
        data_list.add("书名");
        data_list.add("作者");
        data_list.add("出版社");
        data_list.add("图书状态");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSp_condition.setAdapter(arr_adapter);
        mSp_condition.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                str_condition=arr_adapter.getItem(arg2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                str_condition="书名";
            }
        });
        mBt_query.setOnClickListener(this);
        mBt_queryAllBook.setOnClickListener(this);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_query:
                String conditionValue=mEt_condition.getText().toString().trim();
                data.clear();
                query(str_condition,conditionValue);
                mLv_bookInfo.setAdapter(bookAdapter);
                mEt_condition.setText("");
                break;
            case R.id.btn_queryAllBook:
                data.clear();
                queryAll();
                mLv_bookInfo.setAdapter(bookAdapter);
                break;
        }
    }
    public void queryAll(){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("book",null,null,null,
                null,null,null);
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ISBN",cursor.getString(0));
                map.put("bookName",cursor.getString(1));
                map.put("author",cursor.getString(2));
                map.put("press",cursor.getString(3));
                map.put("price",cursor.getInt(4));
                map.put("publishDay",cursor.getString(5));
                map.put("status",cursor.getString(6));
                data.add(map);
            }
        }else{
            Toast.makeText(this, "查无数据！", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }
    public void query(String str_condition,String conditionValue){
        String str_condition1=conditionTransfer(str_condition);
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("book",null,str_condition1+"=?",new String[]{conditionValue},
                null,null,null);
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ISBN",cursor.getString(0));
                map.put("bookName",cursor.getString(1));
                map.put("author",cursor.getString(2));
                map.put("press",cursor.getString(3));
                map.put("price",cursor.getInt(4));
                map.put("publishDay",cursor.getString(5));
                map.put("status",cursor.getString(6));
                data.add(map);
            }
        }else{
            Toast.makeText(this, "查无数据！", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }
    public String conditionTransfer(String str_condition){
        String result=null;
        if(str_condition.equals("书名")){
            result="Bname";
        }else if(str_condition.equals("作者")){
            result="Bauthor";
        }else if(str_condition.equals("出版社")){
            result="Bpress";
        }else if(str_condition.equals("图书状态")){
            result="Bstatus";
        }
        return result;
    }
    public class BookAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, Object>> data;
        private String str_ISBN;

        private BookAdapter(Context context, List<Map<String, Object>> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            final ViewHolder holder;
            if(convertView==null){
                convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_bookinfo1,parent,false);
                holder=new ViewHolder();
                holder.mTv_ISBN=(TextView) convertView.findViewById(R.id.tv_getISBN);
                holder.mTv_bookName=(TextView)convertView.findViewById(R.id.tv_getBookName);
                holder.mTv_bookAuthor=(TextView)convertView.findViewById(R.id.tv_getBookAuthor);
                holder.mTv_bookPress=(TextView)convertView.findViewById(R.id.tv_getBookPress);
                holder.mTv_bookPrice=(TextView)convertView.findViewById(R.id.tv_getBookPrice);
                holder.mTv_bookPublishDay=(TextView)convertView.findViewById(R.id.tv_getBookPublishDay);
                holder.mTv_bookStatus=(TextView)convertView.findViewById(R.id.tv_getBookStatus);
                holder.mBt_update=(Button)convertView.findViewById(R.id.btn_update);
                holder.mBt_delete=(Button)convertView.findViewById(R.id.btn_delete);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Map map = data.get(position);
            str_ISBN=map.get("ISBN").toString();
            holder.mTv_ISBN.setText(str_ISBN);
            holder.mTv_bookName.setText(map.get("bookName").toString());
            holder.mTv_bookAuthor.setText(map.get("author").toString());
            holder.mTv_bookPress.setText(map.get("press").toString());
            holder.mTv_bookPrice.setText(map.get("price").toString());
            holder.mTv_bookPublishDay.setText(map.get("publishDay").toString());
            holder.mTv_bookStatus.setText(map.get("status").toString());
            holder.mBt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityJumpToUpdate(str_ISBN);
                }
            });
            holder.mBt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog=new AlertDialog.Builder(BookProtectionActivity.this)
                            .setTitle("提示：")
                            .setMessage("是否删除该图书的信息？")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delete(str_ISBN);
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create();
                    dialog.show();

                }
            });
            return convertView;
        }
        private void activityJumpToUpdate(String str_ISBN){
            Intent intent=new Intent(BookProtectionActivity.this,UpdateBookInfoActivity.class);
            intent.putExtra("isbn",str_ISBN);
            startActivity(intent);
        }
        private void delete(String str_ISBN){
            SQLiteDatabase db=myHelper.getWritableDatabase();
            db.delete("book","ISBN=?",new String[]{str_ISBN});
            Toast.makeText(context, "删除记录成功！", Toast.LENGTH_SHORT).show();
            db.close();
        }
        class ViewHolder{
            TextView mTv_ISBN;
            TextView mTv_bookName;
            TextView mTv_bookAuthor;
            TextView mTv_bookPress;
            TextView mTv_bookPrice;
            TextView mTv_bookPublishDay;
            TextView mTv_bookStatus;
            Button mBt_update;
            Button mBt_delete;
        }
    }
}
