package com.daniel.gallery.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daniel.gallery.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddActivity extends AppCompatActivity {

    private ImageDecoder.Source selectedImage;
    private EditText title,desc;
    private ImageView select_img;

    Button save,cancel;
    private static  final int REQCODE =1;
    private String titleText,descText;


    private  Bitmap imgBitmap;
    private  ActivityResultLauncher<Intent> pickImageLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        getSupportActionBar().setTitle("Add Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        select_img = (ImageView) findViewById(R.id.select_img);
        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);

        pickImageLauncer();
        select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String permissions;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                    permissions = Manifest.permission.READ_MEDIA_IMAGES;
                }else{
                    permissions=  Manifest.permission.READ_EXTERNAL_STORAGE;
                }
                if(ContextCompat.checkSelfPermission(AddActivity.this,permissions)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{permissions},REQCODE);
                }else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickImageLauncher.launch(i);

                }
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imgBitmap==null){
                    Toast.makeText(AddActivity.this, "Please Select image",Toast.LENGTH_SHORT).show();
                }else{
                    titleText = String.valueOf(title.getText());
                    descText =  desc.getText().toString();

                    ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();

                    getResizedBitmap(imgBitmap, 100,100).compress(Bitmap.CompressFormat.PNG,50,outputStream);
                    byte [] byteImage = outputStream.toByteArray();
                    Intent intent  =  new Intent();
                    intent.putExtra("title",titleText);
                    intent.putExtra("desc",descText);
                    intent.putExtra("image",byteImage);
                    setResult(RESULT_OK,intent);

                    finish();


                }



            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQCODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(i);
        }


    }

    public  void pickImageLauncer(){
        pickImageLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult res) {
                Intent i =  res.getData();
                int reqcode =  res.getResultCode();
                if(reqcode==RESULT_OK && i!=null){
                    selectedImage = ImageDecoder.createSource(getContentResolver(),i.getData());
                    try {
                        imgBitmap =  ImageDecoder.decodeBitmap(selectedImage);
                        select_img.setImageBitmap(imgBitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        });
    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }
}