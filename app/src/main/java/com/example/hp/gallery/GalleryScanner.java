package com.example.hp.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessingConstants;

import java.util.Arrays;

public class GalleryScanner {

    private FacialProcessing mFacialProcessing;
    private Context context;

    public GalleryScanner(Context context) {
        this.context = context;
    }

    public void initFacialProcessing() throws UnsupportedOperationException
    {
        if(!FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING) || !FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_RECOGNITION))
            throw new UnsupportedOperationException("Operation not supprted");

        mFacialProcessing=FacialProcessing.getInstance();
        if(mFacialProcessing!=null)
        {
            mFacialProcessing.setRecognitionConfidence(50);
            mFacialProcessing.setProcessingMode(FacialProcessing.FP_MODES.FP_MODE_STILL);
            loadAlbum();
        }
        else
            throw new UnsupportedOperationException("instance already in use");
    }

    private void loadAlbum()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Gallery",Context.MODE_PRIVATE);
        String arrayOfString = sharedPreferences.getString("facedata", null);

        byte[] albumArray = null;
        if (arrayOfString != null) {
            String[] splitStringArray = arrayOfString.substring(1,arrayOfString.length() - 1).split(", ");

            albumArray = new byte[splitStringArray.length];
            for (int i = 0; i < splitStringArray.length; i++) {
                albumArray[i] = Byte.parseByte(splitStringArray[i]);
            }
            mFacialProcessing.deserializeRecognitionAlbum(albumArray);
        }
    }

    public void deinitFacialProcessing(){
        if(mFacialProcessing != null){
            saveAlbum();
            mFacialProcessing.release();
            mFacialProcessing = null;
        }
    }

    private void saveAlbum()
    {
        byte[] albumBuffer = mFacialProcessing.serializeRecogntionAlbum();
        SharedPreferences sharedPreferences = context.getSharedPreferences("Gallery", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("facedata", Arrays.toString(albumBuffer));
        editor.apply();
    }

    public void processImage(String path,String id) throws Exception
    {
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        if(bitmap!=null)
        {
            if(!mFacialProcessing.setBitmap(bitmap))
                return;

            int numfaces=mFacialProcessing.getNumFaces();

            if(numfaces>0)
            {
                FaceData faceDataArray[]=mFacialProcessing.getFaceData();

                if(faceDataArray==null)
                    return;

                for(int i=0;i<faceDataArray.length;i++)
                {
                    FaceData faceData=faceDataArray[i];
                    if(faceData==null)
                        continue;

                    int personId=faceData.getPersonId();
                    if(personId== FacialProcessingConstants.FP_PERSON_NOT_REGISTERED)
                    {
                        personId=mFacialProcessing.addPerson(i);
                        Person person=new Person(context,personId+"","unnamed"+personId,id);
                        person.create();
                        person.link();
                    }
                    else
                    {
                        mFacialProcessing.updatePerson(personId,i);
                        Person person=new Person(context,personId+"","unnamed"+personId,id);
                        person.link();
                    }
                }

            }

        }
    }

}
