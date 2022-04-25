package com.admin_official.browseflickr;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {
    private static final String TAG = "De_PhotoDetailAct.";

    ImageView imageView;
    Photo photo;
    TextView author, title, tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        photo = (Photo)getIntent().getSerializableExtra(FLICKR_TRANSFER);
        Resources resources = getResources();

        imageView = findViewById(R.id.photo_img);
        author = findViewById(R.id.photo_author);
        title = findViewById(R.id.photo_title);
        tags = findViewById(R.id.photo_tags);

        if(photo != null) {
            author.setText(resources.getString(R.string.photo_detail_author, photo.getAuthor()));
            title.setText(resources.getString(R.string.photo_detail_title, photo.getTitle()));
            tags.setText(resources.getString(R.string.photo_detail_tags, photo.getTags()));
            Picasso.get()
                    .load(photo.getLink())
                    .placeholder(R.drawable.placeholderimg)
                    .error(R.drawable.placeholderimg)
                    .into(imageView);
        }
    }
}