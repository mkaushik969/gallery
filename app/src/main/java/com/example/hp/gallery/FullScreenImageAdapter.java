package com.example.hp.gallery;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> paths;
    Matrix matrix=new Matrix();
    float scale=1f;
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
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewlayout=inflater.inflate(R.layout.fullscreen_imageview,container,false);

        imageView=(ImageView)viewlayout.findViewById(R.id.via_iv);
        imageView.setImageBitmap(MyBitmapCompressor.getCompressedImage(paths.get(position),720,720));

        ((ViewPager)container).addView(viewlayout);

        return viewlayout;
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
