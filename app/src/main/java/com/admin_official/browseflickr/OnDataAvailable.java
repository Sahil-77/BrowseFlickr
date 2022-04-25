package com.admin_official.browseflickr;

import java.util.List;

/** This is an interface which should be implemented by
 * the class getting call back from getFlickrJsonData class */

public interface OnDataAvailable {
    void onDataAvailable(List<Photo> data, DownloadStatus status);
}
