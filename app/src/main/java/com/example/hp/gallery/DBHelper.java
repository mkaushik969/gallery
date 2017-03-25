package com.example.hp.gallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists Images(IId varchar(10) primary key,location text,event varchar(50));");
        db.execSQL("create table if not exists People(PId varchar(10) primary key,name varchar(50),phno varchar(15));");
        db.execSQL("create table if not exists Image_People(IId varchar(10) references Images(IId),PId varchar(50) references People(PId));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertImage(MyImage image) throws Exception
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("IId",image.getId());
        contentValues.put("location","others");
        contentValues.put("event","others");
        db.insert("Images",null,contentValues);
    }

    public void insertPeople(Person person) throws Exception
    {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PId",person.getpId());
        contentValues.put("name",person.getName());
        sqLiteDatabase.insert("People",null,contentValues);

    }

    public void linkPeople(Person person) throws Exception
    {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("IId",person.getIId());
        contentValues.put("PId",person.getpId());
        sqLiteDatabase.insert("Image_People",null,contentValues);
    }
}
