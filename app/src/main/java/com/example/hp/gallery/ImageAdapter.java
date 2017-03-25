package com.example.hp.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    Context context;
    ArrayList<Bitmap> arrayList;

    public ImageAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        arrayList = new ArrayList<>(paths.size());

        for (int i = 0; i < paths.size(); i++)
            arrayList.add(i, MyBitmapCompressor.getCompressedImage(paths.get(i), 100,100));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams((int) context.getResources().getDimension(R.dimen.imv_width), (int) context.getResources().getDimension(R.dimen.imv_height)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }


        loadBitmap(arrayList.get(position), imageView);
        //imageView.setImageBitmap(arrayList.get(position));

        return imageView;
    }

    public void loadBitmap(Bitmap bitmap, ImageView imageView0) {

        if(cancelPotentialWork(bitmap,imageView0)) {

            Bitmap mplaceholder=BitmapFactory.decodeResource(context.getResources(),R.drawable.placeholder);
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(imageView0);
            AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mplaceholder, bitmapWorkerTask);
            imageView0.setImageDrawable(asyncDrawable);
            bitmapWorkerTask.execute(bitmap);
        }
    }

    public Boolean cancelPotentialWork(Bitmap bitmap,ImageView imageView)
    {
        BitmapWorkerTask bitmapWorkerTask=getBitmapWorkerTask(imageView);
        if(bitmapWorkerTask!=null)
        {
            Bitmap bitmapdata=bitmapWorkerTask.data;
            if(bitmapdata==null || !bitmapdata.equals(bitmap))
            {
                bitmapWorkerTask.cancel(true);
            }
            else
                return false;
        }
        return true;
    }


    class BitmapWorkerTask extends AsyncTask<Bitmap, String, Bitmap> {
        private WeakReference<ImageView> imageViewWeakReference;
        private Bitmap data;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {

            data = params[0];
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewWeakReference != null && bitmap != null) {
                ImageView imageView = imageViewWeakReference.get();
                BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null)
                    imageView.setImageBitmap(bitmap);
            }

        }


    }

    class AsyncDrawable extends BitmapDrawable
    {
        WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap,BitmapWorkerTask bitmapWorkerTask)
        {
            super(resources,bitmap);
            bitmapWorkerTaskWeakReference=new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask()
        {
            return bitmapWorkerTaskWeakReference.get();
        }
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView)
    {
        if(imageView!=null)
        {
            Drawable drawable=imageView.getDrawable();
            if(drawable instanceof AsyncDrawable)
            {
                AsyncDrawable asyncDrawable=(AsyncDrawable)drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }


}
