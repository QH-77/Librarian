package com.jl.librarian;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jl.dao.MyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryUserActivity extends AppCompatActivity{
    MyHelper myHelper;
    ListView mLv_userInfo;
    List<Map<String, Object>> data2;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_user);
        myHelper=new MyHelper(this);
        mLv_userInfo=(ListView)findViewById(R.id.lv_userInfo);
        data2 = new ArrayList<Map<String, Object>>();
        userAdapter=new UserAdapter(this,data2);
        queryAll();
        mLv_userInfo.setAdapter(userAdapter);
    }
    public void queryAll(){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("user",null,null,null,
                null,null,null);
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name",cursor.getString(0));
                map.put("question1",cursor.getString(2));
                map.put("answer1",cursor.getString(3));
                map.put("question2",cursor.getString(4));
                map.put("answer2",cursor.getString(5));
                data2.add(map);
            }
        }
        cursor.close();
        db.close();
    }
    public class UserAdapter extends BaseAdapter {
        private Context context1;
        private List<Map<String, Object>> data1;

        public UserAdapter(Context context, List<Map<String, Object>> data) {
            this.context1 = context;
            this.data1 = data;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data1.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data1.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder;
            if(convertView==null){
                convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_userinfo,parent,false);
                holder=new ViewHolder();
                holder.mTv_name=(TextView) convertView.findViewById(R.id.tv_getName);
                holder.mTv_question1=(TextView)convertView.findViewById(R.id.tv_getQuestion1);
                holder.mTv_answer1=(TextView)convertView.findViewById(R.id.tv_getAnswer1);
                holder.mTv_question2=(TextView)convertView.findViewById(R.id.tv_getQuestion2);
                holder.mTv_answer2=(TextView)convertView.findViewById(R.id.tv_getAnswer2);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Map map = data1.get(position);
            holder.mTv_name.setText(map.get("name").toString());
            holder.mTv_question1.setText(map.get("question1").toString());
            holder.mTv_answer1.setText(map.get("answer1").toString());
            holder.mTv_question2.setText(map.get("question2").toString());
            holder.mTv_answer2.setText(map.get("answer2").toString());
            return convertView;
        }
        class ViewHolder{
            TextView mTv_name;
            TextView mTv_question1;
            TextView mTv_answer1;
            TextView mTv_question2;
            TextView mTv_answer2;
        }
    }
}
