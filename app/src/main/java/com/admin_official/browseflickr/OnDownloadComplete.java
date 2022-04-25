package com.admin_official.browseflickr;

/** This is an interface used for call back from DownloadThread. */
public interface OnDownloadComplete {
    public void onDownloadComplete(String s, DownloadStatus downloadStatus);
}
