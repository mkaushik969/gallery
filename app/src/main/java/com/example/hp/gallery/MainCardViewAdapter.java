package com.example.hp.gallery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainCardViewAdapter extends RecyclerView.Adapter<MainCardViewAdapter.MainViewHolder>{

    Cursor cursor;
    ArrayList<String> paths;
    Context context;
    Bitmap myBitmaps[];
    Handler handler=new Handler();
    String albumType;

    public MainCardViewAdapter(Context context,ArrayList<String> paths,Cursor cursor) {
        this.cursor = cursor;
        this.context = context;
        this.paths=paths;

        myBitmaps=new Bitmap[cursor.getCount()];
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        final View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,ViewAlbumActivity.class);
                CardView cv=(CardView) v;
                TextView tv=(TextView)cv.findViewById(R.id.ma_cv_tv);
                intent.putExtra("type",albumType);
                intent.putExtra("name",tv.getText().toString());
                context.startActivity(intent);

     //           Toast.makeText(context,"type:"+albumType+" name:"+tv.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.textView.setText(cursor.getString(0));
      //  holder.imageView.setImageBitmap(bitmap[position]);

        ImageLoaderTask imageLoaderTask=new ImageLoaderTask(holder.imageView,position);
        imageLoaderTask.execute(paths.get(position));

        Drawable placeholder = context.getResources().getDrawable(R.drawable.placeholder);
        holder.imageView.setImageDrawable(placeholder);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        public MainViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.ma_cv_iv);
            textView=(TextView)itemView.findViewById(R.id.ma_cv_tv);
        }
    }

    public void setViewType(String viewType)
    {
        albumType=viewType;
    }

    class ImageLoaderTask extends AsyncTask<String,String,Bitmap>
    {
        private final WeakReference<ImageView> imageViewReference;
        int pos;
        Bitmap bit;

        public ImageLoaderTask(ImageView imageView,int position) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            pos=position;
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
                        Toast.makeText(context,"loader:"+e1.getMessage(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context,"pos:"+e1.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
