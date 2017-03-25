package com.example.hp.gallery;

import android.content.Context;

public class MyImage {
    Context context;
  private String id;

    public MyImage(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public void create() throws Exception
    {

        DBHelper db=new DBHelper(context,"Gallery",null,1);
        db.insertImage(this);

    }


    public String getId() {
        return id;
    }
}
