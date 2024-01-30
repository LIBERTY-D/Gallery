package com.daniel.gallery.Adapters;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.gallery.Events.OnItemClickListener;
import com.daniel.gallery.Models.GalleryModel;
import com.daniel.gallery.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {


    private OnItemClickListener onclickListerner;
    List<GalleryModel> galleryModelList  = new ArrayList<>();
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        GalleryModel galleryModel  = galleryModelList.get(position);

        holder.item_img.setImageBitmap(BitmapFactory.decodeByteArray(galleryModel.getImage(),0,galleryModel.getImage().length));
        holder.item_title.setText(galleryModel.getTitle());
        holder.item_desc.setText(galleryModel.getGallery_desc());

    }


     public GalleryModel getGallery(int position) {
          return galleryModelList.get(position);
    }

    public void setGalleryModelList(List<GalleryModel> galleryModelList) {
        this.galleryModelList = galleryModelList;
        notifyDataSetChanged();
    }

    public  void setOnclickListerner(OnItemClickListener onclickListerner){
        this.onclickListerner=onclickListerner;
    }
    @Override
    public int getItemCount() {
        return galleryModelList.size();
    }

    public  class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_img;

        private TextView  item_title, item_desc;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_desc = itemView.findViewById(R.id.item_desc);
            item_title = itemView.findViewById(R.id.item_title);
            item_img = itemView.findViewById(R.id.item_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryModel galleryModel = galleryModelList.get(getAdapterPosition());
                    onclickListerner.onClickImage(galleryModel);
                }
            });

        }
    }
}
