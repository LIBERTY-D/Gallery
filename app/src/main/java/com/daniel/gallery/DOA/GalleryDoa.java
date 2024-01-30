package com.daniel.gallery.DOA;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daniel.gallery.Models.GalleryModel;

import java.util.List;

@Dao
public interface GalleryDoa {


    @Insert
    public  void insert(GalleryModel galleryModel);

    @Update
    public  void update(GalleryModel galleryModel);

    @Delete
    public  void delete(GalleryModel galleryModel);


    @Query("SELECT * FROM gallery ORDER BY gallery_id ASC")
    public LiveData<List<GalleryModel>> GetAllGallery();



}
