package com.admin_official.browseflickr;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>>
        implements OnDownloadComplete {

    private static final String TAG = "De_GetFlickrJsonData";

    private List<Photo> photoList = null;
    private String baseURL;
    private String language;
    private boolean matchAll;
    private boolean runningOnSameThread = false;

    private final OnDataAvailable callBack;

    public GetFlickrJsonData
            (String baseURL, String language,
             boolean matchAll, OnDataAvailable callBack) {

        this.baseURL = baseURL;
        this.language = language;
        this.matchAll = matchAll;
        this.callBack = callBack;
    }

    void executeOnSameThread (String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: in");

        runningOnSameThread = true;
        DownloadThread downloadThread = new DownloadThread(this);
        downloadThread.execute(createUri(searchCriteria));

        Log.d(TAG, "executeOnSameThread: out");
    }

    @Override
    protected void onPostExecute(List<Photo> photoList) {
        Log.d(TAG, "onPostExecute: in");
        if(callBack != null) {
            callBack.onDataAvailable(photoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: out");
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: in");
        String destinationUri = createUri(strings[0]);
        DownloadThread downloadThread = new DownloadThread(this);
        downloadThread.runOnSameThread(destinationUri);
        Log.d(TAG, "doInBackground: out");
        return photoList;
    }

    String createUri (String searchCriteria) {
        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: in");
        photoList = new ArrayList<>();

        if(status == DownloadStatus.OK) {
            photoList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPhoto = jsonArray.getJSONObject(i);
                    Photo photo = new Photo(
                            jsonPhoto.getString("title"),
                            jsonPhoto.getString("author"),
                            jsonPhoto.getString("author_id"),
                            jsonPhoto.getString("tags"),
                            jsonPhoto.getJSONObject("media").getString("m")
                    );
                    photoList.add(photo);
                    Log.d(TAG, "onDownloadComplete: result " + photo.toString());
                }

            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: JSON PARSE ERROR" + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
                e.printStackTrace();
            }
        }

        if(callBack != null && runningOnSameThread) {
            // now inform the caller that processing is done - possibly returning null if there
            callBack.onDataAvailable(photoList, status);
        }

        Log.d(TAG, "onDownloadComplete: out");
    }
}
