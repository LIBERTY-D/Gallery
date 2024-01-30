package com.daniel.gallery.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.daniel.gallery.DOA.GalleryDoa;
import com.daniel.gallery.Models.GalleryModel;


@Database(entities = {GalleryModel.class},version = 1)
public  abstract  class GalleryDb extends RoomDatabase {
    public  abstract GalleryDoa galleryDoa();


    public  static  GalleryDb instance;

    public  static  synchronized  GalleryDb  getInstance(Context context){
         if (instance == null){

             instance = Room.databaseBuilder(context.getApplicationContext(), GalleryDb.class,"gallery_db").
                     // Use cautiously; it clears the database if migrations fail
                     fallbackToDestructiveMigration().build();

         }

         return instance;
    }
    public static void destroyInstance() {
        instance = null;
    }
}
