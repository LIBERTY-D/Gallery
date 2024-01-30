package com.daniel.gallery.Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gallery")
public class GalleryModel {




    @PrimaryKey(autoGenerate = true)
    private int gallery_id ;


    private String title;

    private String gallery_desc;

    private byte [] image;


    public GalleryModel(String title, String gallery_desc,byte[]image) {
        this.title = title;
        this.gallery_desc = gallery_desc;
        this.image = image;
    }


    public int getGallery_id() {
        return gallery_id;
    }

    public void setGallery_id(int gallery_id) {
        this.gallery_id = gallery_id;
    }

    public String getTitle() {
        return title;
    }

    public String getGallery_desc() {
        return gallery_desc;
    }

    public byte[] getImage() {
        return image;
    }
}
