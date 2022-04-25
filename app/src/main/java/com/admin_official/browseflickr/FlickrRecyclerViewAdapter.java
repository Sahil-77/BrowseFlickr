package com.admin_official.browseflickr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends
        RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder>
{

    private static final String TAG = "De_RecyclerViewAdapter";
    private List<Photo> photoList;
    private Context context;

    public FlickrRecyclerViewAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: in");
        View view = LayoutInflater.from(context).inflate(R.layout.browse, parent, false);
        Log.d(TAG, "onCreateViewHolder: out");
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        // Called by the layout manager when it wants new data in an existing row

        if((photoList == null) || photoList.size() == 0) {
            holder.thumbnail.setImageResource(R.drawable.placeholderimg);
            holder.title.setText(R.string.no_match_placeholder_text);
        } else {
            Photo photoItem = photoList.get(position);
            //Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " ---> " + position);
            Picasso.get()
                    .load(photoItem.getImage())
                    .placeholder(R.drawable.placeholderimg)
                    .error(R.drawable.placeholderimg)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle());
        }
    }

    public void loadNewData(List<Photo> newPhotos) {
        photoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return photoList!=null&&photoList.size()!=0&&photoList.size()>position?
                photoList.get(position):null;
    }

    @Override
    public int getItemCount() {
        //Log.d(TAG, "getItemCount: called");
        return photoList==null?0:(photoList.size()==0)?1:photoList.size();
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;


        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
            this.title = itemView.findViewById(R.id.title);
        }
    }
}