package com.example.camscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class camscanner extends AppCompatActivity {
    public int REQUEST_CODE=1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String tempUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camscanner);

        Button photoButton = (Button) this.findViewById(R.id.button2);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                }
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);



                }
            }
        });


    }
    @Override
     protected  void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST) {
            //2
            System.out.println("Doneeeeee");
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //3
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            //4
            File file = new File("/sdcard/source1.png");
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                //5
                fo.write(bytes.toByteArray());
                fo.close();

                Intent intent = new Intent("com.intsig.camscanner.ACTION_SCAN");
                // Or content uri picked from gallery
                Uri uri = Uri.fromFile(new File("/sdcard/source1.png"));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra("scanned_image", "/sdcard/scanned.jpg");
                intent.putExtra("pdf_path", "/sdcard/processed.jpg");
                intent.putExtra("org_image", "/sdcard/org.jpg");
                startActivityForResult(intent, REQUEST_CODE);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_CODE) {
            //int responseCode = data.getIntExtra("RESULT_OK", -1);
            if (requestCode == Activity.RESULT_OK) {
                // Success
                Toast.makeText(this,"Success",Toast.LENGTH_LONG);
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                // Fail
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User canceled
            }
        }
    }
}
