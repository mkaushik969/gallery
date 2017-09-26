package com.example.hp.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ViewAlbumAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> paths;
    Bitmap myBitmaps[];
    Handler handler=new Handler();
    int height,width;

    public ViewAlbumAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths=paths;
        myBitmaps=new Bitmap[paths.size()];

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        height=width=200;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    // For a simple image list:


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        imageView = new ImageView(context);

        try {

            //    imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,GridView.LayoutParams.WRAP_CONTENT));
            imageView.setLayoutParams(new GridView.LayoutParams(width, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            new ImageLoaderTask(imageView, position).execute(paths.get(position));

            Drawable placeholder = context.getResources().getDrawable(R.drawable.placeholder);
            imageView.setImageDrawable(placeholder);
            return imageView;

        }
        catch (Exception e)
        {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return imageView;
    }

    class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        int pos;
        Bitmap bit;

        public ImageLoaderTask(ImageView imageView,int position) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            pos=position;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {

                if(myBitmaps[pos]==null){
                    bit = MyBitmapCompressor.getCompressedThumbnail(params[0]);
                    myBitmaps[pos]=bit;
                } else
                    bit = myBitmaps[pos];
            }
            catch(Exception e)
            {
                final Exception e1=e;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,e1.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return bit;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try
            {
                if (isCancelled()) {
                    bitmap = null;
                }

                if (imageViewReference != null) {
                    ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                        //else missing pics
                    }
                }
            }
            catch(Exception e)
            {
                final Exception e1=e;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,e1.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}