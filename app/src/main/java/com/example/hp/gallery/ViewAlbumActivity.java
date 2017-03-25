package com.example.hp.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ViewAlbumActivity extends AppCompatActivity {

    GridView gridView;
    Cursor cursor,cursor1;
    ArrayList<String> arrayList;
    String album,viewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        viewType=intent.getStringExtra("type");

        gridView=(GridView)findViewById(R.id.vaa_gv);

        SQLiteDatabase db=openOrCreateDatabase("Gallery",MODE_PRIVATE,null);

            arrayList=new ArrayList<>();

            if(viewType.equals("albums"))
            {
                album=intent.getStringExtra("name");
                cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA},MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" ='"+album.replace("'","''")+"'", null,null);

                if(cursor.moveToFirst()) {
                    do {
                        arrayList.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
                else
                    Toast.makeText(ViewAlbumActivity.this,"not found",Toast.LENGTH_SHORT).show();

            }
            else if (viewType.equals("date"))
            {
                album=intent.getStringExtra("name");
                Toast.makeText(ViewAlbumActivity.this,"coming soon",Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(viewType.equals("location") || viewType.equals("event"))
            {
                album=intent.getStringExtra("name");
                 cursor=db.rawQuery("select IId from Images where "+viewType+"='"+album+"'",null);
                if(cursor.moveToFirst())
                {
                    cursor1=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA},MediaStore.Images.Media._ID+" in("+makePlaceholders(cursor)+")", null,null);
                    if(cursor1.moveToFirst())
                    {
                        do {
                            arrayList.add(cursor1.getString(0));
                        } while (cursor1.moveToNext());
                    }
                    else
                        Toast.makeText(ViewAlbumActivity.this,"not found",Toast.LENGTH_SHORT).show();
                }
            }
            else if(viewType.equals("people"))
            {
                album=intent.getStringExtra("name");
                Cursor temp=db.rawQuery("Select PId from People where name='"+album+"' LIMIT 1",null);
                temp.moveToFirst();

                cursor1=db.rawQuery("select IId from Image_People where PId='"+temp.getString(0)+"'",null);
                if(cursor1.moveToFirst())
                {
                    cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA},MediaStore.Images.Media._ID+" in("+makePlaceholders(cursor1)+")", null,null);
                    if(cursor.moveToFirst())
                    {
                        do {
                            arrayList.add(cursor.getString(0));
                        }while (cursor.moveToNext());
                    }
                }
            }
            else if (viewType.equals("peoplepeople"))
            {
                album=intent.getStringExtra("name");

                Cursor cursor3=db.rawQuery("select PId from People where name in("+album+")",null);
                if(cursor3.moveToFirst())
                {
                    Toast.makeText(ViewAlbumActivity.this,"cursor3:"+cursor3.getCount(),Toast.LENGTH_SHORT).show();

                    String quer="select IId from Image_People where PId='"+cursor3.getString(0)+"'"+makeAndPlaceholders(cursor3);
                    Cursor cursor4=db.rawQuery(quer,null);

                    Toast.makeText(ViewAlbumActivity.this,quer,Toast.LENGTH_SHORT).show();

                    if(cursor4.moveToFirst())
                    {
                        Toast.makeText(ViewAlbumActivity.this,"cursor4:"+cursor4.getCount(),Toast.LENGTH_SHORT).show();

                        cursor3=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA},MediaStore.Images.Media._ID+" in("+makePlaceholders(cursor4)+")", null,null);
                        if(cursor3.moveToFirst())
                        {
                            Toast.makeText(ViewAlbumActivity.this,"cursor3:"+cursor3.getCount(),Toast.LENGTH_SHORT).show();
                            do {
                                arrayList.add(cursor3.getString(0));
                            }while (cursor3.moveToNext());
                        }

                    }

                }

            }
            else if (viewType.equals("peopleevent"))
            {
                String pname=getIntent().getStringExtra("pname");
                album=intent.getStringExtra("name");

                Cursor cursor3=db.rawQuery("select IId from Images where event in("+album+")",null);
                if(cursor3.moveToFirst())
                {
//                    Toast.makeText(ViewAlbumActivity.this,"cursor3:"+cursor3.getCount(),Toast.LENGTH_SHORT).show();

                    Cursor cursor4=db.rawQuery("select PId from People where name='"+pname+"' LIMIT 1",null);

                    if(cursor4.moveToFirst())
                    {
//                        Toast.makeText(ViewAlbumActivity.this,"cursor4:"+cursor4.getCount(),Toast.LENGTH_SHORT).show();

                        Cursor cursor5=db.rawQuery("select IId from Image_People where PId='"+cursor4.getString(0)+"' and IId in("+makePlaceholders(cursor3)+")",null);
                        if(cursor5.moveToFirst())
                        {
  //                          Toast.makeText(ViewAlbumActivity.this,"cursor5:"+cursor5.getCount(),Toast.LENGTH_SHORT).show();

                            cursor3=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA},MediaStore.Images.Media._ID+" in("+makePlaceholders(cursor5)+")", null,null);
                            if(cursor3.moveToFirst())
                            {
                                do {
                                    arrayList.add(cursor3.getString(0));
                                }while (cursor3.moveToNext());
                            }
                        }
                    }

                }

            }
            else if (viewType.equals("peoplelocation"))
            {
 //               Toast.makeText(ViewAlbumActivity.this,"peoplelocation",Toast.LENGTH_SHORT).show();

                String pname=getIntent().getStringExtra("pname");
                album=intent.getStringExtra("name");

                Cursor cursor3=db.rawQuery("select IId from Images where location in("+album+")",null);
                if(cursor3.moveToFirst())
                {
//                    Toast.makeText(ViewAlbumActivity.this,"cursor3:"+cursor3.getCount(),Toast.LENGTH_SHORT).show();

                    Cursor cursor4=db.rawQuery("select PId from People where name='"+pname+"' LIMIT 1",null);

                    if(cursor4.moveToFirst())
                    {
  //                      Toast.makeText(ViewAlbumActivity.this,"cursor4:"+cursor4.getCount(),Toast.LENGTH_SHORT).show();

                        Cursor cursor5=db.rawQuery("select IId from Image_People where PId='"+cursor4.getString(0)+"' and IId in("+makePlaceholders(cursor3)+")",null);
                        if(cursor5.moveToFirst())
                        {
    //                        Toast.makeText(ViewAlbumActivity.this,"cursor5:"+cursor5.getCount(),Toast.LENGTH_SHORT).show();

                            cursor3=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA},MediaStore.Images.Media._ID+" in("+makePlaceholders(cursor5)+")", null,null);
                            if(cursor3.moveToFirst())
                            {
                                do {
                                    arrayList.add(cursor3.getString(0));
                                }while (cursor3.moveToNext());
                            }
                        }
                    }

                }

            }

            if(arrayList.size()!=0)
            {
                ViewAlbumAdapter viewAlbumAdapter=new ViewAlbumAdapter(ViewAlbumActivity.this,arrayList);
                gridView.setAdapter(viewAlbumAdapter);
            }
            else
            {
                Toast.makeText(ViewAlbumActivity.this,"No Images found",Toast.LENGTH_SHORT).show();
            }

            getSupportActionBar().setSubtitle(arrayList.size()+"");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                try
                {
                    Intent intent1 = new Intent(ViewAlbumActivity.this, ViewImageActivity.class);
                    intent1.putStringArrayListExtra("paths",arrayList);
                    intent1.putExtra("pos", position);
                    startActivity(intent1);

                }catch (Exception e)
                {
                    Toast.makeText(ViewAlbumActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Snackbar.make(view, intent.getStringExtra("album"), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
        }catch (Exception e)
        {
            Toast.makeText(ViewAlbumActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    String makePlaceholders(Cursor mCursor) {
        try {
            if (!mCursor.moveToFirst()) {
                // It will lead to an invalid query anyway ..
                throw new RuntimeException("No placeholders");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(mCursor.getString(0));
                if (mCursor.moveToNext()) {
                    do {
                        sb.append("," + mCursor.getString(0));
                    }
                    while (mCursor.moveToNext());
                }
                return sb.toString();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(ViewAlbumActivity.this,"sb:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    String makePlaceholders2(Cursor mCursor) {
        try {
            if (!mCursor.moveToFirst()) {
                // It will lead to an invalid query anyway ..
                throw new RuntimeException("No placeholders");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("'"+mCursor.getString(0)+"'");
                if (mCursor.moveToNext()) {
                    do {
                        sb.append(",'" + mCursor.getString(0)+"'");
                    }
                    while (mCursor.moveToNext());
                }
                return sb.toString();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(ViewAlbumActivity.this,"sb:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    String makeAndPlaceholders(Cursor mCursor) {
        try {
            StringBuilder sb = new StringBuilder();
            if(mCursor.moveToPosition(1))
            {
                do {
                    sb.append(" and PId='"+mCursor.getString(0)+"'");
                }
                while (mCursor.moveToNext());
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            Toast.makeText(ViewAlbumActivity.this,"sb:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}
