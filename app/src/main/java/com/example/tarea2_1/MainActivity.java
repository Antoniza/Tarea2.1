//SAUL ANTONIO IZAGUIRRE - 202010020281

package com.example.tarea2_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {



    public static final int recordVideo = 4;
    VideoView videoScreen;
    Spinner videosList;
    String[] videosSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videosSaved = fileList();
        videosList = findViewById(R.id.videosList);
        videoScreen = findViewById(R.id.videoScreen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videosSaved);
        videosList.setAdapter(adapter);

    }
    public void videoRecord(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, recordVideo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)

    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == recordVideo && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            videoScreen.setVideoURI(videoUri);
            videoScreen.start();

            try {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
                FileOutputStream file = openFileOutput(newVideoName(), Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0){
                    file.write(buf, 0, len);
                }
            }
            catch (IOException e){
                Toast.makeText(this, "Error: ", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void playSavedVideo(View v)
    {
        int videoSelected = videosList.getSelectedItemPosition();
        videoScreen.setVideoPath(getFilesDir()+"/"+videosSaved[videoSelected]);
        videoScreen.start();
    }

    private String newVideoName()
    {

        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = fecha + ".mp4";
        return nombre;
    }
}