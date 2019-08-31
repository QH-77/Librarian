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

public class DeleteUserActivity extends AppCompatActivity{
    MyHelper myHelper;
    ListView mLv_userInfo;
    List<Map<String, Object>> data2;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
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
                data2.add(map);
            }
        }
        cursor.close();
        db.close();
    }
    public class UserAdapter extends BaseAdapter {
        private Context context1;
        private List<Map<String, Object>> data1;
        String str_name;

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
                convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_userinfo1,parent,false);
                holder=new ViewHolder();
                holder.mTv_name=(TextView) convertView.findViewById(R.id.tv_getName);
                holder.mBt_deleteUser=(Button)convertView.findViewById(R.id.btn_deleteUser1); 
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Map map = data1.get(position);
            str_name=map.get("name").toString();
            holder.mTv_name.setText(str_name);
            holder.mBt_deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog=new AlertDialog.Builder(DeleteUserActivity.this)
                            .setTitle("提示：")
                            .setMessage("是否删除该用户？")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteUser(str_name);
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create();
                    dialog.show();

                }
            });
            return convertView;
        }
        private void deleteUser(String str_name){
            SQLiteDatabase db=myHelper.getWritableDatabase();
            db.delete("user","Uname=?",new String[]{str_name});
            Toast.makeText(DeleteUserActivity.this, "删除该用户成功！", Toast.LENGTH_SHORT).show();
            db.close();
        }
        class ViewHolder{
            TextView mTv_name;
            Button mBt_deleteUser;
        }
    }
}
