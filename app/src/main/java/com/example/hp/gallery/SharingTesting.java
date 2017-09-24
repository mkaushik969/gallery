package com.example.hp.gallery;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SharingTesting extends AppCompatActivity {

    Button button;
    Uri imageURI=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,21);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==21 && resultCode==RESULT_OK)
        {
            imageURI=data.getData();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri screenshotUri = Uri.parse(imageURI.toString());

            sharingIntent.setType("image/png");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            startActivity(Intent.createChooser(sharingIntent, "Share image using"));
            Toast.makeText(this, imageURI.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
