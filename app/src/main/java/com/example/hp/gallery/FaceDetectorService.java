package com.example.hp.gallery;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;

public class FaceDetectorService extends IntentService {

    Handler handler=new Handler();
    GalleryScanner galleryScanner;

    public FaceDetectorService() {
        super("FaceDetectorService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            galleryScanner=new GalleryScanner(getApplicationContext());
            galleryScanner.initFacialProcessing();

                final Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID},MediaStore.Images.Media.BUCKET_DISPLAY_NAME+"='BYD & Banglasahib'", null, null);
                if (cursor.moveToFirst()) {
                    do {
                        galleryScanner.processImage(cursor.getString(0),cursor.getString(1));
                    } while (cursor.moveToNext());
                } else
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "photos not found (service)", Toast.LENGTH_SHORT).show();
                    }
                });
        }
        catch (Exception e)
        {
            final Exception e1=e;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"service:"+e1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        galleryScanner.deinitFacialProcessing();
        Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT).show();
    }
}

