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
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daniel.gallery.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    private EditText update_title,update_desc;
    private ImageView update_select_img;
    private Bitmap image;

    private Button update_save,update_cancel;
    private int REQ_CODE=1;

    private ImageDecoder.Source my_image_update;

    private Bitmap my_image_bit;
    private byte [] imagei;
    private String title;
    private int id;
    String desc;
    private ActivityResultLauncher<Intent> updateActivityLauncher;

    private String titleText,descText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Gallery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_update);
        update_select_img = (ImageView) findViewById(R.id.update_select_img);
        update_title = (EditText) findViewById(R.id.update_title);
        update_desc = (EditText) findViewById(R.id.update_desc);

        update_save = (Button) findViewById(R.id.update_save);
        update_cancel = (Button) findViewById(R.id.update_cancel);
        updateActivityLauncherM();

        Intent intentData =  getIntent();
        if(intentData!=null){
            id =  intentData.getIntExtra("id",-1);
            title = intentData.getStringExtra("title");
            desc =  intentData.getStringExtra("desc");
            imagei =  intentData.getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagei,0,imagei.length);
            update_select_img.setImageBitmap(bitmap);
            update_title.setText(title);
            update_desc.setText(desc);




        }
        update_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String perm;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                    perm= Manifest.permission.READ_MEDIA_IMAGES;
                }else{
                    perm =  Manifest.permission.READ_EXTERNAL_STORAGE;
                }

                if(ContextCompat.checkSelfPermission(UpdateActivity.this,perm) !=PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(UpdateActivity.this,new String[]{perm},REQ_CODE);
                }else{
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    updateActivityLauncher.launch(i);
                }
            }
        });



        update_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(my_image_bit!=null){
                    ByteArrayOutputStream bous =  new ByteArrayOutputStream();

                    AddActivity.
                            getResizedBitmap(my_image_bit,100,100).
                            compress(Bitmap.CompressFormat.PNG,50,bous);
                    byte [] byteImage=  bous.toByteArray();
                    titleText = String.valueOf(update_title.getText());
                    descText =  update_desc.getText().toString();
                    Intent intent =  new Intent();
                    intent.putExtra("id",id);
                    intent.putExtra("title",titleText);
                    intent.putExtra("desc",descText);
                    intent.putExtra("image",byteImage);

                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                            Toast.
                            makeText(UpdateActivity.this,"Can't update with empty image",Toast.LENGTH_SHORT).
                            show();
                }

            }
        });




        update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }



//    called when initially the user is requested permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQ_CODE && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            updateActivityLauncher.launch(i);

        }
    }

    public  void updateActivityLauncherM(){
        updateActivityLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult res) {
                 int resCode =  res.getResultCode();
                 Intent data  = res.getData();

                 if(resCode==RESULT_OK && data !=null){
                     my_image_update = ImageDecoder.createSource(getContentResolver(),data.getData());
                     try {
                         my_image_bit= ImageDecoder.decodeBitmap(my_image_update);
                         update_select_img.setImageBitmap(my_image_bit);
                     } catch (IOException e) {
                         Toast.makeText(UpdateActivity.this,"The was an error",Toast.LENGTH_SHORT).show();
                     }
                 }
            }
        });
    }

}