package com.testproject.bingimageviewer.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.testproject.bingimageviewer.R;
import com.testproject.bingimageviewer.model.ImageDetail;

import java.util.List;

/**
 * Created by prachi on 11/02/17.
 */

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ImageViewHolder> {


    private List<ImageDetail> imageDetailList;
    private Context context;

    public ImageGridAdapter(List<ImageDetail> imageDetails, Context context) {
        this.imageDetailList = imageDetails;
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row, parent, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return imageDetailList.size();
    }

    public void setImageList(List<ImageDetail> imageDetails) {
        this.imageDetailList = imageDetails;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final ImageDetail imageDetail = imageDetailList.get(position);
        Picasso.with(context).load(imageDetail.getContentUrl()).into(holder.image);

    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ImageViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.resultImageView);

        }
    }
}
