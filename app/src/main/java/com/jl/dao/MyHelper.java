package com.jl.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context){
        super(context,"librarian.db",null,2);
    }
    public void onCreate(SQLiteDatabase db){
        //由于主键的唯一性，相同主键的数据将无法再次写入数据库！
        db.execSQL("CREATE TABLE user(Uname VARCHAR(20),Upassword VARCHAR(20)," +
                "Uquestion1 VARCHAR(30),Uanswer1 VARCHAR(50),Uquestion2 VARCHAR(30),Uanswer2 VARCHAR(50))");
        db.execSQL("CREATE TABLE book(ISBN VARCHAR(20),Bname VARCHAR(30),Bauthor VARCHAR(20)," +
                "Bpress VARCHAR(30),Bprice DOUBLE,BpublishDay VARCHAR(10),Bstatus VARCHAR(10))");
        db.execSQL("CREATE TABLE record(ISBN VARCHAR(20),Bname VARCHAR(30),Rname VARCHAR(10)," +
                "Rsex VARCHAR(2),Rtel VARCHAR(15),Rdate VARCHAR(10),Rremark VARCHAR(50))");
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //
    }
}

