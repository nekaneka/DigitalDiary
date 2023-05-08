package com.spba.ediary.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spba.ediary.R;

/**
 * Horizontal RecyclerView idea taken from
 * https://stackoverflow.com/questions/28460300/how-to-build-a-horizontal-listview-with-recyclerview
 */
import java.util.List;

public class RecycleGalleryAdapter extends RecyclerView.Adapter<RecycleGalleryAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ItemClickListener clickListener;
    private List<Bitmap> images;

    public RecycleGalleryAdapter(Context context, List<Bitmap> images){
        this.layoutInflater = LayoutInflater.from(context);
        this.images = images;

    }

    @NonNull
    @Override
    public RecycleGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.gallery_recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleGalleryAdapter.ViewHolder holder, int position) {

        Bitmap image = images.get(position);
        holder.myView.setImageBitmap(image);


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myView;

        ViewHolder(View itemView) {
            super(itemView);
            myView = itemView.findViewById(R.id.recycle_imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }


    public Bitmap getItem(int id) {
        return images.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
