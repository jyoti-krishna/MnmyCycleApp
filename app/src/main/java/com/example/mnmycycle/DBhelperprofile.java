package com.example.mnmycycle;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DBhelperprofile extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME="PROFILE_DATABASE";
    private static final String TABLE_NAME="PROFILE_TABLE";
    private static final String COL_1="NAME";
    private static final String COL_2="WEIGHT";
    private static final String COL_3="AGE";
    private static final String COL_4="HEIGHT";
    private static final String COL_5="WTLOSS";
    private static final String COL_6="DAYS";
//    private static final String COL_4="MAX";

    public DBhelperprofile(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME+"(NAME STRING PRIMARY KEY,WEIGHT INTEGER,AGE INTEGER,HEIGHT INTEGER,WTLOSS INTEGER,DAYS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertdata(String COL_1,String COL_2,String COL_3,String COL_4,String COL_5,String COL_6){
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",COL_1);
        contentValues.put("WEIGHT",COL_2);
        contentValues.put("AGE",COL_3);
        contentValues.put("HEIGHT",COL_4);
        contentValues.put("WTLOSS",COL_5);
        contentValues.put("DAYS",COL_6);

        long res=db.insert(TABLE_NAME,null,contentValues);
        if(res==-1){
            return false;
        }
        else return true;
    }
    public void deletedata(String COL_1){
        db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE NAME=?" ,new String[]{COL_1});
        if(cursor.getCount()>0)
            db.delete(TABLE_NAME,"NAME=?",new String[]{COL_1});
    }

    public Cursor getdata(){
        db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " +TABLE_NAME,null);
        return cursor;
    }
}
