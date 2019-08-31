package com.jl.librarian;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jl.dao.MyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordActivity extends AppCompatActivity{
    MyHelper myHelper;
    ListView mLv_recordInfo;
    List<Map<String, Object>> data1;
    RecordAdapter recordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        myHelper=new MyHelper(this);
        mLv_recordInfo=(ListView)findViewById(R.id.lv_recordInfo);
        data1 = new ArrayList<Map<String, Object>>();
        recordAdapter=new RecordAdapter(this,data1);
        queryAll();
        mLv_recordInfo.setAdapter(recordAdapter);
    }
    protected void onDestroy(){
        super.onDestroy();
    }
    public void queryAll(){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("record",null,null,null,
                null,null,null);
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ISBN",cursor.getString(0));
                map.put("bookName",cursor.getString(1));
                map.put("readerName",cursor.getString(2));
                map.put("readerSex",cursor.getString(3));
                map.put("readerTel",cursor.getString(4));
                map.put("date",cursor.getString(5));
                map.put("remark",cursor.getString(6));
                data1.add(map);
            }
        }
        cursor.close();
        db.close();
    }
    public class RecordAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, Object>> data;
        String str_ISBN;

        public RecordAdapter(Context context, List<Map<String, Object>> data) {
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
            ViewHolder holder;
            if(convertView==null){
                convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_recordinfo,parent,false);
                holder=new ViewHolder();
                holder.mTv_ISBN=(TextView) convertView.findViewById(R.id.tv_getISBN);
                holder.mTv_bookName=(TextView)convertView.findViewById(R.id.tv_getBookName);
                holder.mTv_readerName=(TextView)convertView.findViewById(R.id.tv_getReaderName);
                holder.mTv_readerSex=(TextView)convertView.findViewById(R.id.tv_getReaderSex);
                holder.mTv_readerTel=(TextView)convertView.findViewById(R.id.tv_getReaderTel);
                holder.mTv_date=(TextView)convertView.findViewById(R.id.tv_getDate);
                holder.mTv_remark=(TextView)convertView.findViewById(R.id.tv_getRemark);
                holder.mBt_deleteRecord=(Button)convertView.findViewById(R.id.btn_deleteRecord);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Map map = data.get(position);
            str_ISBN=map.get("ISBN").toString();
            holder.mTv_ISBN.setText(str_ISBN);
            holder.mTv_bookName.setText(map.get("bookName").toString());
            holder.mTv_readerName.setText(map.get("readerName").toString());
            holder.mTv_readerSex.setText(map.get("readerSex").toString());
            holder.mTv_readerTel.setText(map.get("readerTel").toString());
            holder.mTv_date.setText(map.get("date").toString());
            holder.mTv_remark.setText(map.get("remark").toString());
            holder.mBt_deleteRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog=new AlertDialog.Builder(RecordActivity.this)
                            .setTitle("提示：")
                            .setMessage("是否删除该条借阅记录？")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteRecord(str_ISBN);
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create();
                    dialog.show();

                }
            });
            return convertView;
        }
        private void deleteRecord(String str_ISBN){
            SQLiteDatabase db=myHelper.getWritableDatabase();
            db.delete("record","ISBN=?",new String[]{str_ISBN});
            Toast.makeText(RecordActivity.this, "删除记录成功！", Toast.LENGTH_SHORT).show();
            db.close();
        }
        class ViewHolder{
            TextView mTv_ISBN;
            TextView mTv_bookName;
            TextView mTv_readerName;
            TextView mTv_readerSex;
            TextView mTv_readerTel;
            TextView mTv_date;
            TextView mTv_remark;
            Button mBt_deleteRecord;
        }
    }
}
