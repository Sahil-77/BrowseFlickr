package com.admin_official.browseflickr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener{

    private static final String TAG = "De_MainActivity";
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        // initializing recycler view
        RecyclerView recyclerView = this.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // initializing and adding adapter
        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter
                (new ArrayList<Photo>(), this);

        recyclerView.setAdapter(flickrRecyclerViewAdapter);


        // adding onClickListener on recycler view
        recyclerView.addOnItemTouchListener
                (new RecyclerItemClickListener(this, recyclerView, this));


        // download directly from download thread without parsing from get json data class
        /*DownloadThread downloadThread = new DownloadThread(this);
        downloadThread.execute
                ("https://www.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1");*/

        Log.d(TAG, "onCreate: out");
    }

    // using onResume so that we have the downloaded data before the mean activity is created
    // since we will be using different activities and shifting to different activities leads
    // to onPause method which then leads to onResume method.
    // Also when we go to the search activity we pause the main activity and when we return
    // onResume method starts
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: in");
        super.onResume();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferences.getString(FLICKR_QUERY, "");

        if(queryResult.length() > 0) {
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData
                    ("https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true, this);

            // executeOnSameThread will parse the json data on the UI thread
            // while download will done on different thread

            //getFlickrJsonData.executeOnSameThread("android, nougat");

            // execute will parse and download the json data on different thread

            getFlickrJsonData.execute(queryResult);
        }
        Log.d(TAG, "onResume: out");
    }

    @Override
    public void onDataAvailable(List<Photo> photoList, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDataAvailable: in");
        if(downloadStatus == DownloadStatus.OK) {
            flickrRecyclerViewAdapter.loadNewData(photoList);
        } else {
            Log.d(TAG, "onDownloadComplete: download failed with status " + downloadStatus);
        }
        Log.d(TAG, "onDataAvailable: out");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: in");
        Toast.makeText
                (this, "Press and hold to view in large size", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: in");
        //Toast.makeText
          //      (this, "long Click on recycler view at position " + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(FLICKR_TRANSFER, flickrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}