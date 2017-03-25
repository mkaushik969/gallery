package com.example.hp.gallery;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class FilterCheckboxListAdapter extends BaseAdapter {

    Context context;
    Cursor cursor;

    public FilterCheckboxListAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        cursor.moveToPosition(position);
        return cursor.getString(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        CheckboxListItem checkboxListItem=new CheckboxListItem();

        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.checkbox_list_layout,parent,false);
        }

        checkboxListItem.checkBox=(CheckBox)convertView.findViewById(R.id.fa_list3_cb);
        checkboxListItem.textView=(TextView)convertView.findViewById(R.id.fa_list3_tv);


        cursor.moveToPosition(position);
        checkboxListItem.textView.setText(cursor.getString(0));

        return convertView;
    }

    class CheckboxListItem
    {
        CheckBox checkBox;
        TextView textView;
    }
}
