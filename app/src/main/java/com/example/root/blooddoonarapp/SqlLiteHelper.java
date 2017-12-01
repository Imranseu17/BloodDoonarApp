package com.example.root.blooddoonarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Statement;

/**
 * Created by root on 11/4/17.
 */

public class SqlLiteHelper extends SQLiteOpenHelper {


    public SqlLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    public  void insertData(String name,String group,String number,byte[]image){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO DONOR VALUES(Null,?,?,?,?)";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindString(2,group);
        statement.bindString(3,number);
        statement.bindBlob(4,image);
        statement.executeInsert();
        statement.close();
    }

    public  void  updateData(String name,String group,String number,byte[]image,int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "UPDATE DONOR SET name = ?,bloodgroup = ?, number = ?,image = ? WHERE _id = ?";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.bindString(1,name);
        statement.bindString(2,group);
        statement.bindString(3,number);
        statement.bindBlob(4,image);
        statement.bindDouble(5, (double) id);
        statement.execute();
        statement.close();


    }

    public void  deleteData(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "DELETE FROM DONOR WHERE _id = ?";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double) id);
        statement.execute();
        sqLiteDatabase.close();


    }



    public Cursor getdata(String sql){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS DONOR(_id INTEGER PRIMARY KEY " +
                "AUTOINCREMENT ,name VARCHAR,bloodgroup VARCHAR,number VARCHAR,image BLOG)";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
