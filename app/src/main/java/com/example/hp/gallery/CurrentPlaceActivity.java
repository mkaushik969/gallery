package com.example.hp.gallery;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.jar.*;

public class CurrentPlaceActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_current_place);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            Toast.makeText(CurrentPlaceActivity.this,"yes1",Toast.LENGTH_LONG).show();
       /*     googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .addOnConnectionFailedListener(this)
                    .enableAutoManage(this, 0, this)
                    .build();*/

            Toast.makeText(CurrentPlaceActivity.this,"yes2",Toast.LENGTH_LONG).show();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (googleApiClient.isConnected()) {
                        if (ContextCompat.checkSelfPermission(CurrentPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CurrentPlaceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        } else {
                            Toast.makeText(CurrentPlaceActivity.this,"call to pdapi",Toast.LENGTH_LONG).show();
                            //callPlaceDetectionApi();
                        }
                    }
                    else
                        Toast.makeText(CurrentPlaceActivity.this,"not conected",Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this,"currPlAC:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(googleApiClient!=null)
            googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient!=null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPlaceDetectionApi();
                }
                break;
        }
    }

    private void callPlaceDetectionApi() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Toast.makeText(CurrentPlaceActivity.this,"callPDapi():",Toast.LENGTH_LONG).show();

            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {

                    Toast.makeText(CurrentPlaceActivity.this,"hoods:"+placeLikelihoods.toString(),Toast.LENGTH_LONG).show();

                    for(PlaceLikelihood placeLikelihood:placeLikelihoods)
                    {
                        placeLikelihood.getPlace().getName();
                        placeLikelihood.getLikelihood();

                        Toast.makeText(CurrentPlaceActivity.this,placeLikelihood.getPlace().getName().toString(),Toast.LENGTH_LONG).show();

                    }
                    placeLikelihoods.release();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(this,"currPlAC:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
