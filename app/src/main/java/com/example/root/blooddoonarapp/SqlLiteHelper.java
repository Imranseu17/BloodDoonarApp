package com.example.root.blooddoonarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by root on 11/4/17.
 */

public class SqlLiteHelper extends SQLiteOpenHelper {
        SQLiteDatabase sqLiteDatabase;
        long _id;

    public SqlLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    public  void insertData(String name,String group,String number,byte[]image){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO DONAR VALUES(Null,?,?,?,?)";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindString(2,group);
        statement.bindString(3,number);
        statement.bindBlob(4,image);
        statement.executeInsert();
        statement.close();
    }

    public int update( String name, String group, String number, byte[]image) {
        ContentValues  contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("bloodgroup",group);
        contentValues.put("number",number);
        contentValues.put("image",image);
        int updatedata = sqLiteDatabase.
                update("DONAR", contentValues, "-id = " + _id, null);
        return updatedata;


    }

    public void delete(long _id) {
        sqLiteDatabase.delete("DONAR",
                "_id =" + _id, null);
    }


    public Cursor getdata(String sql){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS DONAR(_id INTEGER PRIMARY KEY " +
                "AUTOINCREMENT ,name VARCHAR,bloodgroup VARCHAR,number VARCHAR,image BLOG)";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
