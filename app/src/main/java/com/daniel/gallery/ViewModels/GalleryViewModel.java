package com.daniel.gallery.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.gallery.Models.GalleryModel;
import com.daniel.gallery.Repository.GalleryRepository;

import java.util.List;

public class GalleryViewModel extends AndroidViewModel {


    private GalleryRepository galleryRepository;

    private LiveData<List<GalleryModel>> listLiveData;

    public GalleryViewModel(@NonNull Application application) {

        super(application);
         galleryRepository = new GalleryRepository(application);

         listLiveData =  galleryRepository.getGalleryModelLiveData();

    }

    public  void  insert(GalleryModel galleryModel){
         galleryRepository.insert(galleryModel);
    }

    public  void  delete(GalleryModel galleryModel){
           galleryRepository.delete(galleryModel);
    }

    public  void  update(GalleryModel galleryModel){
            galleryRepository.update(galleryModel);
    }
    public  LiveData<List<GalleryModel>> getListLiveData(){
        return listLiveData;

    }
}
