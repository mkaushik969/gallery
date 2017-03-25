package com.example.hp.gallery;

import android.content.Context;


public class Person {
    String pId,name,IId;
    Context context;
    DBHelper dbHelper;

    public Person(Context context,String pId, String name,String IId) {
        this.pId = pId;
        this.name = name;
        this.context = context;
        this.IId=IId;
        dbHelper=new DBHelper(context,"Gallery",null,1);
    }

    public void create() throws Exception
    {
        dbHelper.insertPeople(this);
    }

    public void link() throws Exception
    {
        dbHelper.linkPeople(this);
    }

    public String getpId() {
        return pId;
    }

    public String getIId() {
        return IId;
    }

    public String getName() {
        return name;
    }
}
