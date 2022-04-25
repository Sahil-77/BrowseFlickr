package com.admin_official.browseflickr;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressLint("StaticFieldLeak")
class DownloadThread extends AsyncTask<String, Void, String> {
    private static final String TAG = "De_DownloadThread";
    private DownloadStatus downloadStatus;
    private final OnDownloadComplete callBack;

    public DownloadThread (OnDownloadComplete onDownloadComplete) {
        this.downloadStatus = DownloadStatus.IDLE;
        this.callBack = onDownloadComplete;
    }

    public void runOnSameThread (String urlPath) {
        Log.d(TAG, "runOnSameThread: in");

        // we shouldn't use onPostExecute here since it calls the super class
        // which can result in weird behaviour
//        onPostExecute(doInBackground(urlPath));

        callBack.onDownloadComplete(doInBackground(urlPath), downloadStatus);

        Log.d(TAG, "runOnSameThread: out");
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: in");
//        super.onPostExecute(s);
        if(callBack != null) {
            callBack.onDownloadComplete(s, downloadStatus);
        }
        Log.d(TAG, "onPostExecute: out");
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: in");
        if(strings[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED;
        }

        String rawDownloadData = download(strings[0]);
        if(rawDownloadData == null) {
            Log.e(TAG, "doInBackground: Error Downloading Data");
        }
        Log.d(TAG, "doInBackground: out");
        return rawDownloadData;
    }

    private String download(String urlPath) {
        Log.d(TAG, "download: in");

        HttpURLConnection connection = null;
        BufferedReader br = null;

        try {
            this.downloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d (TAG, "download: Connection Response Code: " + responseCode);

            StringBuilder sb = new StringBuilder();
            br = new BufferedReader
                    (new InputStreamReader(connection.getInputStream()));
            String nextLine = br.readLine();

            while(nextLine != null) {
                sb.append(nextLine).append('\n');
                nextLine = br.readLine();
            }

            this.downloadStatus = DownloadStatus.OK;
            Log.d(TAG, "download: out");
            return sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "download: An error occurred while Downloading!");
            Log.e(TAG, "download: Error: " + e.getMessage() );
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }

            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.e(TAG, "download: Error: " + e.getMessage() );
                    e.printStackTrace();
                }
            }
        }

        this.downloadStatus = DownloadStatus.FAILED_OR_EMPTY;

        Log.d(TAG, "download: out");
        return null;
    }
}
