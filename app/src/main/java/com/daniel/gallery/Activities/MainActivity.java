package com.daniel.gallery.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daniel.gallery.Adapters.ItemAdapter;
import com.daniel.gallery.Events.OnItemClickListener;
import com.daniel.gallery.Models.GalleryModel;
import com.daniel.gallery.R;
import com.daniel.gallery.ViewModels.GalleryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton open_gallery;

    private RecyclerView rv;



    private GalleryViewModel galleryViewModel;


    ActivityResultLauncher<Intent>   addActivityLauncher;

    ActivityResultLauncher<Intent> updateActivityLuancher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv);
        open_gallery = (FloatingActionButton) findViewById(R.id.open_gallery);

        galleryViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(GalleryViewModel.class);

        openAddActivityLauncher();
        updateActivityLuancherM();
        ItemAdapter itemAdapter =  new ItemAdapter();





        galleryViewModel.getListLiveData().observe(MainActivity.this, new Observer<List<GalleryModel>>() {
            @Override
            public void onChanged(List<GalleryModel> galleryModels) {
                System.out.println("changed");
              //update ui when theres a change
                itemAdapter.setGalleryModelList(galleryModels);
            }
        });

        rv.setAdapter(itemAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));




        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =  new Intent(MainActivity.this,AddActivity.class);
                addActivityLauncher.launch(i);
            }
        });




        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos =  viewHolder.getAdapterPosition();
                galleryViewModel.delete(itemAdapter.getGallery(pos));
            }
        }).attachToRecyclerView(rv);



        itemAdapter.setOnclickListerner(new OnItemClickListener() {
            @Override
            public void onClickImage(GalleryModel galleryModel) {
               int id =  galleryModel.getGallery_id();
               String title  = galleryModel.getTitle();
               String desc=  galleryModel.getGallery_desc();
               byte [] image =  galleryModel.getImage();
               Intent  i =  new Intent(MainActivity.this,UpdateActivity.class);
               i.putExtra("id",id);
               i.putExtra("title",title);
               i.putExtra("desc",desc);
               i.putExtra("image",image);
               updateActivityLuancher.launch(i);


            }
        });
    }




    public  void updateActivityLuancherM(){
        updateActivityLuancher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult res) {
                int reqCode =  res.getResultCode();
                Intent data  =  res.getData();
                    if(reqCode==RESULT_OK && data !=null) {
                        int id =  data.getIntExtra("id",-1);
                        String title = data.getStringExtra("title");
                        String desc = data.getStringExtra("desc");
                        byte[] image = data.getByteArrayExtra("image");
                        GalleryModel  galleryModel =  new GalleryModel(title,desc,image);
                        galleryModel.setGallery_id(id);
                        galleryViewModel.update(galleryModel);



                    }

            }
        });
    }
    public void openAddActivityLauncher(){
          addActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
              @Override
              public void onActivityResult(ActivityResult res) {
                   int resCode =  res.getResultCode();
                    Intent data =  res.getData();

                    if(resCode==RESULT_OK && data !=null){
                        String title = data.getStringExtra("title");
                        String desc =  data.getStringExtra("desc");
                        byte [] image  = data.getByteArrayExtra("image");

                        GalleryModel galleryModel = new GalleryModel(title,desc,image);
                        galleryViewModel.insert(galleryModel);

                    }
              }
          });
    }
}