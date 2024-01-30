package com.daniel.gallery.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.daniel.gallery.DOA.GalleryDoa;
import com.daniel.gallery.Database.GalleryDb;
import com.daniel.gallery.Models.GalleryModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GalleryRepository {



    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private GalleryDoa galleryDoa;

    private LiveData<List<GalleryModel>> galleryModelLiveData;

    public  GalleryRepository(Context context){
        GalleryDb  db =  GalleryDb.getInstance(context);
        galleryDoa = db.galleryDoa();
        galleryModelLiveData = galleryDoa.GetAllGallery();

    }

    public  void insert(GalleryModel  galleryModel){

         executorService.execute(new Runnable() {
             @Override
             public void run() {
                 galleryDoa.insert(galleryModel);
             }
         });
    }

    public  void update(GalleryModel  galleryModel){
       executorService.execute(new Runnable() {
           @Override
           public void run() {
               galleryDoa.update(galleryModel);
           }
       });
    }


    public  void  delete(GalleryModel  galleryModel){
      executorService.execute(new Runnable() {
          @Override
          public void run() {
              galleryDoa.delete(galleryModel);
          }
      });
    }


    public  LiveData<List<GalleryModel>> getGalleryModelLiveData(){

        return  galleryModelLiveData;
    }

}
