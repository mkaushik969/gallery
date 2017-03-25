package com.example.hp.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    ListView listView1,listView2,listView3;
    SimpleCursorAdapter simpleCursorAdapter;
    RecyclerView recyclerView;
    ViewFlipper vf;
    String category;
    SQLiteDatabase db;
    EditText search;
    ActionBar actionBar;
    Cursor cursor;
    int vfpos=0;
    ArrayList<String> choices;
    ArrayList<String> arrayList;
    Button list3btn;
    String parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_filter);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();

            vf = (ViewFlipper) findViewById(R.id.fa_vf);
            db = openOrCreateDatabase("Gallery", MODE_PRIVATE, null);

            recyclerView = (RecyclerView) findViewById(R.id.fa_rv);
            listView1 = (ListView) findViewById(R.id.fa_list1);
            listView2 = (ListView) findViewById(R.id.fa_list2);
            listView3 = (ListView) findViewById(R.id.fa_list3);

            choices=new ArrayList<>();

            choices.add(0,"");
            setViewFlipperPosition(0);

            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view;
                    choices.add(2,textView.getText().toString());
                    setViewFlipperPosition(2);
                }
            });

            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view;
                    choices.add(3,textView.getText().toString());
                    setViewFlipperPosition(3);

                }
            });

            listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CheckBox checkBox=(CheckBox)view.findViewById(R.id.fa_list3_cb);
                    TextView textView=(TextView)view.findViewById(R.id.fa_list3_tv);

                    if(checkBox.isChecked())
                    {
                        checkBox.setChecked(false);
                        arrayList.remove(textView.getText().toString());
                    }
                    else
                    {
                        checkBox.setChecked(true);
                        arrayList.add(textView.getText().toString());
                    }

                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpCategories()
    {
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new FilterCardViewAdapter(this,vf));
    }

    public void setCategory(String category)
    {
        try {
            this.category = category;

            //Toast.makeText(this,"-"+category+"-",Toast.LENGTH_SHORT).show();

            cursor = null;
            if (category.equals("Albums"))
            {
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"distinct "+MediaStore.Images.Media.BUCKET_DISPLAY_NAME,"rowid _id"}, null, null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                category=MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
            }
            else if (category.equals("Event") || category.equals("Location"))
                cursor = db.rawQuery("select " + category.toLowerCase() + ",rowid _id from Images group by "+category.toLowerCase(), null);
            else if (category.equals("People"))
            {
                cursor = db.rawQuery("select rowid _id,name from People", null);
                category="name";
            }

            simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.support_simple_spinner_dropdown_item, cursor, new String[]{category.toLowerCase()}, new int[]{android.R.id.text1}, 0);
            listView1.setAdapter(simpleCursorAdapter);

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setViewFlipperPosition(int position)
    {

        if(vfpos>position)
            vf.showPrevious();
        else if(vfpos<position)
            vf.showNext();

        if(position>0)
        parameter=choices.get(position);

        if(position==-1)
        {
            finish();
        }
        else if(position==0)
        {
            vfpos=0;
            setUpCategories();
        }
        else if(position==1)
        {
            vfpos=1;
            setCategory(parameter);

            LayoutInflater layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(R.layout.ma_actionbar_layout,null);
            actionBar.setCustomView(view);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            search=(EditText)view.findViewById(R.id.ma_actionbar_et);

            search.addTextChangedListener(new TextWatcher() {
                String category1 = FilterActivity.this.category;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (FilterActivity.this.category.equals("Albums")) {
                        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"distinct " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "rowid _id"}, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " like '%" + s.toString() + "%'", null, null);
                        category1 = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
                    } else if (FilterActivity.this.category.equals("Event") || FilterActivity.this.category.equals("Location")) {
                        cursor = db.rawQuery("select " + FilterActivity.this.category.toLowerCase() + ",rowid _id from Images where " + FilterActivity.this.category.toLowerCase() + " like '%" + s.toString() + "%' group by " + category1.toLowerCase(), null);
                    } else if (FilterActivity.this.category.equals("People")) {
                        cursor = db.rawQuery("select rowid _id,name from People where name like '%" + s.toString() + "%'", null);
                        category1 = "name";
                    }

                    simpleCursorAdapter = new SimpleCursorAdapter(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, cursor, new String[]{category1.toLowerCase()}, new int[]{android.R.id.text1}, 0);
                    listView1.setAdapter(simpleCursorAdapter);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        else if(position==2)
        {
            vfpos=2;

            if(category.equals("Albums") || category.equals("Event") || category.equals("Location"))
            {
                Intent intent=new Intent(this,ViewAlbumActivity.class);
                intent.putExtra("name",parameter);
                intent.putExtra("type",category.toLowerCase());
                startActivity(intent);
                finish();
            }
            else if(category.equals("People"))
            {
                listView2.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,new String[]{"All","with People","at Location","at Event"}));
            }
        }
        else if (position==3)
        {
            vfpos=3;

            if(parameter.equals("All"))
            {
                Intent intent=new Intent(this,ViewAlbumActivity.class);
                intent.putExtra("name",choices.get(2));
                intent.putExtra("type",category.toLowerCase());
                startActivity(intent);
                finish();
            }
            else
            {
                String str[]=parameter.split(" ");
                final String choice=str[1];

                Cursor cursor1=null;
                if(choice.equals("People"))
                {
                    cursor1=db.rawQuery("select name from People where name not in ('"+choices.get(2)+"')",null);
                }
                else if (choice.equals("Event") || choice.equals("Location"))
                {
                    cursor1=db.rawQuery("select distinct "+choice.toLowerCase()+" from Images",null);
                }
                listView3.setAdapter(new FilterCheckboxListAdapter(this,cursor1));
                arrayList=new ArrayList<>();
                list3btn=(Button)findViewById(R.id.fa_list3_btn);
                list3btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cursor cursor2=null;
                        if(arrayList.size()==0)
                            Toast.makeText(FilterActivity.this,"Select atleast one option",Toast.LENGTH_SHORT).show();
                        else {
                            Intent intent=new Intent(FilterActivity.this,ViewAlbumActivity.class);
                            if(choice.equals("People"))
                            {
                                intent.putExtra("type","peoplepeople");
                                arrayList.add(choices.get(2));
                            }
                            else if (choice.equals("Event"))
                            {
                                intent.putExtra("type","peopleevent");
                            }
                            else if (choice.equals("Location"))
                            {
                                intent.putExtra("type","peoplelocation");
                            }
                            intent.putExtra("pname",choices.get(2));
                            intent.putExtra("name",getPlaceHolder(arrayList));

                            Toast.makeText(FilterActivity.this,getPlaceHolder(arrayList),Toast.LENGTH_SHORT).show();

                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }

        }


    }
    private String getPlaceHolder(ArrayList<String> stringArrayList)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("'"+stringArrayList.get(0)+"'");

        for(int i=1;i<stringArrayList.size();i++)
        {
            sb.append(",'"+stringArrayList.get(i)+"'");
        }
        return sb.toString();
    }

    private String getPlaceHolder2(ArrayList<String> stringArrayList)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(stringArrayList.get(0));

        for(int i=1;i<stringArrayList.size();i++)
        {
            sb.append(","+stringArrayList.get(i));
        }
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        setViewFlipperPosition(vfpos-1);
    }
}
