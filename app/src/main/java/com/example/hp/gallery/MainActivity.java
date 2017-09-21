package com.example.hp.gallery;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Cursor cursor,cursor1,cursor2;
    Handler handler=new Handler();
    ProgressDialog progressDialog;
    SharedPreferences sp;
    RecyclerView recyclerView;
    ArrayList<String> paths;
    CoordinatorLayout cl;
    SQLiteDatabase db;
    MainCardViewAdapter mainCardViewAdapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar=getSupportActionBar();

            recyclerView=(RecyclerView) findViewById(R.id.ma_rv);
            cl=(CoordinatorLayout)findViewById(R.id.ma_cl);

            sp=getSharedPreferences("Gallery",MODE_PRIVATE);

            DBHelper dbHelper=new DBHelper(this,"Gallery",null,1);
            dbHelper.onCreate(openOrCreateDatabase("Gallery",MODE_PRIVATE,null));

            RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            db=openOrCreateDatabase("Gallery",MODE_PRIVATE,null);

            setUpView("albums");

            if(!sp.getBoolean("saved",false)) {

                Toast.makeText(this, "Not saved", Toast.LENGTH_LONG).show();

                if (!sp.getBoolean("syncstarted", false))
                {
                    ImageUploader imageUploader = new ImageUploader();
                    imageUploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);
                }
                else
                    Toast.makeText(this, "sync already running", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "saved", Toast.LENGTH_LONG).show();

            //  startService(new Intent(this,FaceDetectorService.class));

      /*      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            final FloatingActionButton fabm1 = (FloatingActionButton) findViewById(R.id.fabmenu1);
            final FloatingActionButton fabm2 = (FloatingActionButton) findViewById(R.id.fabmenu2);
            final FloatingActionButton fabm3 = (FloatingActionButton) findViewById(R.id.fabmenu3);

            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    int flag=0;
                    LinearLayout l1,l2,l3;

                    @Override
                    public void onClick(View view) {

                        l1=(LinearLayout)findViewById(R.id.mall1);
                        l2=(LinearLayout)findViewById(R.id.mall2);
                        l3=(LinearLayout)findViewById(R.id.mall3);

                        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) l1.getLayoutParams();
                        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) l2.getLayoutParams();
                        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) l3.getLayoutParams();

                        Animation show= AnimationUtils.loadAnimation(MainActivity.this,R.anim.main_fab_show);
                        Animation hide= AnimationUtils.loadAnimation(MainActivity.this,R.anim.main_fab_hide);

                        if(flag==0) {

                            layoutParams1.bottomMargin += (int) (l1.getHeight() * 1);
                            layoutParams2.bottomMargin += (int) (l2.getHeight() * 2);
                            layoutParams3.bottomMargin += (int) (l3.getHeight() * 3);

                            l1.setLayoutParams(layoutParams1);
                            l2.setLayoutParams(layoutParams2);
                            l3.setLayoutParams(layoutParams3);

                            l1.setVisibility(View.VISIBLE);
                            l2.setVisibility(View.VISIBLE);
                            l3.setVisibility(View.VISIBLE);

                //            l1.startAnimation(show);
                  //          l2.startAnimation(show);
                    //        l3.startAnimation(show);

                            flag=1;
                        }
                        else
                        {

                            layoutParams1.bottomMargin -= (int) (l1.getHeight() * 1);
                            layoutParams2.bottomMargin -= (int) (l2.getHeight() * 2);
                            layoutParams3.bottomMargin -= (int) (l3.getHeight() * 3);

                            l1.setLayoutParams(layoutParams1);
                            l2.setLayoutParams(layoutParams2);
                            l3.setLayoutParams(layoutParams3);

              //              l1.startAnimation(hide);
              //              l2.startAnimation(hide);
               //             l3.startAnimation(hide);

                            l1.setVisibility(View.INVISIBLE);
                            l2.setVisibility(View.INVISIBLE);
                            l3.setVisibility(View.INVISIBLE);

                            flag=0;
                        }
                    }
                });
            }*/
        }
        catch (Exception e)
        {
            Toast.makeText(this,"main:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpView(String type)
    {
        try {

        /*    progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();*/

            paths = new ArrayList<>();


            if(type.equals("date"))
            {
                Calendar calendar=Calendar.getInstance();
                String col1[] = {MediaStore.Images.Media.DATE_ADDED,MediaStore.Images.Media.DATA};
                cursor1 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, col1, null, null,MediaStore.Images.Media.DATE_ADDED+" desc");

                if(cursor1.moveToFirst())
                {
                        /*calendar.setTimeInMillis(Long.valueOf(cursor.getString(0)));
                        calendars.add(calendar);
                        paths.add(cursor.getString(1));
                        if(cursor.moveToNext())
                        {
                            calendar.setTimeInMillis(Long.valueOf(cursor.getString(0)));
                        }*/

                    db.execSQL("drop table if exists temp");
                    db.execSQL("create table if not exists temp (dates varchar(15),paths text)");
                    do {
                        calendar.setTimeInMillis(Long.valueOf(cursor1.getString(0)));
                        ContentValues contentValues=new ContentValues();
                        contentValues.put("dates",(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR));
                        contentValues.put("paths",cursor1.getString(1));
                        db.insert("temp",null,contentValues);
                    }while (cursor1.moveToNext());

                    cursor=db.rawQuery("select distinct dates from temp",null);
                    cursor.moveToFirst();
                    do {
                        //paths.add(cursor.getString(1));
                        Toast.makeText(this,cursor.getString(0),Toast.LENGTH_SHORT).show();

                    }while (cursor.moveToNext());

                }
                else
                    Toast.makeText(this,"Not found",Toast.LENGTH_SHORT).show();


//                    Toast.makeText(this,"Month:"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR),Toast.LENGTH_SHORT).show();
            }
            else if(type.equals("albums"))
            {
                String col1[] = {"distinct " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, col1, null, null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                if(cursor.moveToFirst())
                {
                    do {
                        String cname = cursor.getString(0);
                        if (cname.contains("'"))
                            cname = cname.replace("'", "''");

                        cursor1 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ='" + cname + "'", null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME+ " LIMIT 1");
                        if (cursor1.moveToFirst())
                            paths.add(cursor1.getString(0));
                    } while (cursor.moveToNext());
                }
                else
                    Toast.makeText(this,"Not found",Toast.LENGTH_SHORT).show();

            }
            else if (type.equals("event") || type.equals("location"))
            {
                cursor=db.rawQuery("select distinct "+ type +" from Images",null);
/*                    if(cursor.moveToFirst())
                        Toast.makeText(this,"event:"+cursor.getString(0),Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this,"not found",Toast.LENGTH_SHORT).show();
*/
                if(cursor.moveToFirst()) {
                    do {
                        String cname = cursor.getString(0);
                        if (cname.contains("'"))
                            cname = cname.replace("'", "''");

                        Cursor temp=db.rawQuery("select IId from Images where "+type+"='"+cname+"' LIMIT 1",null);
/*                            if(temp.moveToFirst())
                                Toast.makeText(this,"temp:"+temp.getString(0)+" count:"+temp.getCount(),Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(this,"Not found temp",Toast.LENGTH_SHORT).show();
*/
                        temp.moveToFirst();
                        cursor1 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, MediaStore.Images.Media._ID + " =" + temp.getString(0) , null,null);

                        if (cursor1.moveToFirst())
                        {
                            // Toast.makeText(this,"path:"+cursor1.getString(0),Toast.LENGTH_SHORT).show();
                            paths.add(cursor1.getString(0));
                        }
                        else
                        {
                            Toast.makeText(this,"Not found path",Toast.LENGTH_SHORT).show();
                        }
                    } while (cursor.moveToNext());
                }
                else
                    Toast.makeText(this,"Not found",Toast.LENGTH_SHORT).show();
            }
            else if(type.equals("people"))
            {
                cursor=db.rawQuery("select name,PId from People",null);
                if(cursor.moveToFirst())
                {
                    do {
                        Cursor temp1=db.rawQuery("select IId from Image_People where PId='"+cursor.getString(1)+"' LIMIT 1",null);
                        if(temp1.moveToFirst())
                        {
                            cursor1 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, MediaStore.Images.Media._ID + " =" + temp1.getString(0) , null,MediaStore.Images.Media._ID+" LIMIT 1");
                            if(cursor1.moveToFirst())
                                paths.add(cursor1.getString(0));
                        }
                    }while (cursor.moveToNext());
                }
                else
                    Toast.makeText(this,"Not found",Toast.LENGTH_SHORT).show();
            }

            if(paths.size()!=0)
            {
                mainCardViewAdapter = new MainCardViewAdapter(this, paths, cursor);
                recyclerView.setAdapter(mainCardViewAdapter);
                mainCardViewAdapter.setViewType(type);
            }
            cursor=null;
            cursor1=null;

            //   progressDialog.dismiss();
        }
        catch (Exception e)
        {
            Toast.makeText(this,"setup:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            //  progressDialog.dismiss();
        }
    }

    class ImageUploader extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            try
            {
                SharedPreferences.Editor editor=sp.edit();
                editor.putBoolean("syncstarted",true);
                editor.apply();

                String col[] = {MediaStore.Images.Media._ID};
                cursor2 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, col, null, null, null);
//            final int colindex1 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media._ID);


                if(cursor2.moveToFirst())
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Synching",Toast.LENGTH_SHORT).show();
                        }
                    });

                    do {

                        MyImage image=new MyImage(MainActivity.this,cursor2.getString(0));
                        image.create();


                    }while (cursor2.moveToNext());

                    editor.putBoolean("saved",true);
                    editor.apply();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Synched",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"not importing",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            catch (Exception e)
            {
                final Exception e1=e;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"saver:"+e1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(this,"menu:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.ma_menu_album) {
                setUpView("albums");
                return true;
            } else if (id == R.id.ma_menu_date) {
                setUpView("date");
                return true;
            } else if (id == R.id.ma_menu_event) {
                setUpView("event");
                return true;
            } else if (id == R.id.ma_menu_loc) {
                setUpView("location");
                return true;
            } else if (id == R.id.ma_menu_people) {
                setUpView("people");
                return true;
            } else if (id == R.id.ma_menu_search) {
                startActivity(new Intent(this,FilterActivity.class));
                return true;
            } else if (id == R.id.ma_menu_sync_people) {
                startService(new Intent(this,FaceDetectorService.class));
                return true;
            } else if (id == R.id.maps_activity) {
                startService(new Intent(this,MapsActivity.class));
                return true;
            }
            else
                return false;

        }
        catch (Exception e)
        {
            Toast.makeText(this,"menu:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;
        }
    }



}
