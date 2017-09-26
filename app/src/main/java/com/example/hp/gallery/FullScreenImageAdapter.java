package com.example.hp.gallery;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> paths;
    Matrix matrix=new Matrix();
    float scale=1f;
    int i=0;
    Button btn;
    ImageView imageView;

    public FullScreenImageAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewlayout=inflater.inflate(R.layout.fullscreen_imageview,container,false);

        imageView=(ImageView)viewlayout.findViewById(R.id.via_iv);

        imageView.setImageBitmap(MyBitmapCompressor.getCompressedImage(paths.get(position),720,720));
        final Paint rectPaint=new Paint();
        rectPaint.setStrokeWidth(10);
        rectPaint.setColor(Color.RED);
        rectPaint.setStyle(Paint.Style.STROKE);

        final Bitmap tempBitmap=Bitmap.createBitmap((MyBitmapCompressor.getCompressedImage(paths.get(position),720,720)).getWidth(),(MyBitmapCompressor.getCompressedImage(paths.get(position),720,720)).getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas=new Canvas(tempBitmap);
        canvas.drawBitmap((MyBitmapCompressor.getCompressedImage(paths.get(position),720,720)),0,0,null);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FaceDetector faceDetector=new FaceDetector.Builder(context)
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();
                if(!faceDetector.isOperational())
                {
                    Toast.makeText(context, "Face detection not working..!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Frame frame=new Frame.Builder().setBitmap(MyBitmapCompressor.getCompressedImage(paths.get(position),720,720)).build();
                SparseArray<Face> sparseArray=faceDetector.detect(frame);

                for(int i=0;i<sparseArray.size();i++)
                {
                    Face face=sparseArray.valueAt(i);
                    float x1=face.getPosition().x;
                    float y1=face.getPosition().y;
                    float x2=face.getPosition().x+face.getWidth();
                    float y2=face.getPosition().y+face.getHeight();
                    RectF rectF=new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPaint);
                }
                imageView.setImageDrawable(new BitmapDrawable(Resources.getSystem(),tempBitmap));
            }
        });
        ((ViewPager)container).addView(viewlayout);

        return viewlayout;
    }

    private void funcDetection(ImageView imgView ,int pos) {


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((LinearLayout)object);
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale*=detector.getScaleFactor();
            scale=Math.max(0.1f,Math.min(scale,5.0f));

            matrix.setScale(scale,scale);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }

}