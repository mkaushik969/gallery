package com.example.hp.gallery;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class FilterCardViewAdapter extends RecyclerView.Adapter<FilterCardViewAdapter.FilterViewHolder> {

    Context context;
    ViewFlipper viewFlipper;

    public FilterCardViewAdapter(Context context, ViewFlipper viewFlipper) {
        this.context = context;
        this.viewFlipper = viewFlipper;
    }

    String[] listitems={"Albums","People","Event","Location"};
    int[] imageitems={R.drawable.ic_photo_album_black_48dp,
            R.drawable.ic_people_black_48dp,
            R.drawable.ic_event_note_black_48dp,
            R.drawable.ic_location_on_black_48dp};

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_activity_cardview,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView=(TextView) v.findViewById(R.id.fa_cv_tv);
                ((FilterActivity)context).choices.add(1,textView.getText().toString());
                ((FilterActivity)context).setViewFlipperPosition(1);
            }
        });
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        holder.tv.setText(listitems[position]);
        holder.iv.setImageDrawable(context.getResources().getDrawable(imageitems[position]));
    }

    @Override
    public int getItemCount() {
        return imageitems.length;
    }

    class FilterViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        ImageView iv;
        public FilterViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.fa_cv_tv);
            iv=(ImageView)itemView.findViewById(R.id.fa_cv_iv);
        }

        @Override
        public String toString() {
            return tv.toString();
        }
    }
}
