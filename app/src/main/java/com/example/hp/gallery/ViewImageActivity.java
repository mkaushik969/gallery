package com.example.hp.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Handler;

import static com.example.hp.gallery.FullScreenImageAdapter.*;

public class ViewImageActivity extends AppCompatActivity {

    ArrayList<String> paths;
    ScaleGestureDetector sgd;
    Cursor cursor,cursor1;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_image);


            final ViewPager viewPager = (ViewPager) findViewById(R.id.via_vp);
            FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.via_fab);

            final Intent intent = getIntent();
            paths = intent.getStringArrayListExtra("paths");
            int pos = intent.getIntExtra("pos", 0);

            FullScreenImageAdapter fullScreenImageAdapter=new FullScreenImageAdapter(this, paths);

            sgd=new ScaleGestureDetector(this,fullScreenImageAdapter.new ScaleListener());

            viewPager.setAdapter(fullScreenImageAdapter);
            viewPager.setCurrentItem(pos);


            String val=String.valueOf(viewPager.getCurrentItem());
            Log.d("VAL",val);



            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1=new Intent(ViewImageActivity.this,ImageDetailsActivity.class);
                        intent1.putExtra("path",paths.get(viewPager.getCurrentItem()));
                        startActivity(intent1);

                    }
                });
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportActionBar().isShowing())
        getSupportActionBar().hide();

        sgd.onTouchEvent(event);
        return true;
    }
}
