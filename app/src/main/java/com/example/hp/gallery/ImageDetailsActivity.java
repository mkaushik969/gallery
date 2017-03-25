package com.example.hp.gallery;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ImageDetailsActivity extends AppCompatActivity {

    TextView t1,t2;
    TextView t3,t4,t5;
    ImageView imageView;
    SQLiteDatabase db;
    ImageButton b3,b4,b5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {

        setContentView(R.layout.activity_image_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        t1 = (TextView) findViewById(R.id.ida_tv1);
        t2 = (TextView) findViewById(R.id.ida_tv2);
        t3 = (TextView) findViewById(R.id.ida_tv3);
        t4 = (TextView) findViewById(R.id.ida_tv4);
        t5 = (TextView) findViewById(R.id.ida_tv5);
        b3 = (ImageButton)findViewById(R.id.ida_ib3);
            b4 = (ImageButton)findViewById(R.id.ida_ib4);
            b5 = (ImageButton)findViewById(R.id.ida_ib5);
            imageView = (ImageView) findViewById(R.id.ida_iv);

        db = openOrCreateDatabase("Gallery", MODE_PRIVATE, null);

        final String path = getIntent().getStringExtra("path");

        t1.setText(path);

        final Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED}, MediaStore.Images.Media.DATA + "='" + path + "'", null,MediaStore.Images.Media.DATE_ADDED+" desc LIMIT 1");
        if (cursor.moveToFirst()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(1)));
            t2.setText(calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));

            Cursor cursor1 = db.rawQuery("select event,location from Images where IId='" + cursor.getString(0) + "' LIMIT 1", null);
            if (cursor1.moveToFirst()) {
                t3.setText(cursor1.getString(0));
                t4.setText(cursor1.getString(1));
            }

            cursor1=db.rawQuery("select PId from Image_People where IId='"+cursor.getString(0)+"'",null);
            if(cursor1.moveToFirst())
            {
                Cursor cursor2=db.rawQuery("select name from People where PId in("+makePlaceholders(cursor1)+")",null);
                if(cursor2.moveToFirst())
                {
                    t5.setText("");
                    do {
                        t5.append(cursor2.getString(0)+",");
                    }while (cursor2.moveToNext());
                    t5.setText(t5.getText().toString().substring(0,t5.length()-1));
                }
            }
        }

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Button cancelbtn, savebtn;
                    final EditText editText;

                    final Dialog dialog = new Dialog(ImageDetailsActivity.this);

                    dialog.setTitle("Event");
                    dialog.setContentView(R.layout.edit_details_dialog_layout);

                    cancelbtn = (Button) dialog.findViewById(R.id.edd_cbtn);
                    savebtn = (Button) dialog.findViewById(R.id.edd_sbtn);
                    editText = (EditText) dialog.findViewById(R.id.edd_et);

                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(editText.getText().toString().isEmpty())
                                Toast.makeText(ImageDetailsActivity.this, "empty field", Toast.LENGTH_SHORT).show();
                            else
                            {
                                db.execSQL("update images set event='"+editText.getText().toString()+"' where IId='"+cursor.getString(0)+"'");
                                t3.setText(editText.getText().toString());
                                dialog.dismiss();
                                Toast.makeText(ImageDetailsActivity.this, "Change saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                }
                catch (Exception e)
                {
                    Toast.makeText(ImageDetailsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Button cancelbtn, savebtn;
                        final EditText editText;

                        final Dialog dialog = new Dialog(ImageDetailsActivity.this);

                        dialog.setTitle("Event");
                        dialog.setContentView(R.layout.edit_details_dialog_layout);

                        cancelbtn = (Button) dialog.findViewById(R.id.edd_cbtn);
                        savebtn = (Button) dialog.findViewById(R.id.edd_sbtn);
                        editText = (EditText) dialog.findViewById(R.id.edd_et);

                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        savebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(editText.getText().toString().isEmpty())
                                    Toast.makeText(ImageDetailsActivity.this, "empty field", Toast.LENGTH_SHORT).show();
                                else
                                {
                                    db.execSQL("update images set location='"+editText.getText().toString()+"' where IId='"+cursor.getString(0)+"'");
                                    t4.setText(editText.getText().toString());
                                    dialog.dismiss();
                                    Toast.makeText(ImageDetailsActivity.this, "Change saved", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(ImageDetailsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

            b5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Button cancelbtn, savebtn;
                        final EditText editText;

                        final Dialog dialog = new Dialog(ImageDetailsActivity.this);

                        dialog.setTitle("Event");
                        dialog.setContentView(R.layout.edit_details_dialog_layout);

                        cancelbtn = (Button) dialog.findViewById(R.id.edd_cbtn);
                        savebtn = (Button) dialog.findViewById(R.id.edd_sbtn);
                        editText = (EditText) dialog.findViewById(R.id.edd_et);

                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        savebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ImageDetailsActivity.this, "people saved", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(ImageDetailsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ImageDetailsActivity.this,"sb:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}
