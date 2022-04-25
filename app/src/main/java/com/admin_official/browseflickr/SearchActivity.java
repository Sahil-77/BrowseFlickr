package com.admin_official.browseflickr;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;

public class SearchActivity extends BaseActivity {
    private static final String TAG = "De_SearchActivity";

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activateToolbar(true);

        Log.d(TAG, "onCreate: out");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: in");
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // boiler plate code for search activity
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);

        // logging
        Log.d(TAG, "onCreateOptionsMenu: " + getComponentName().toString());
        Log.d(TAG, "onCreateOptionsMenu: searchable info --> " + searchableInfo.toString());
        Log.d(TAG, "onCreateOptionsMenu: hint is --> " + searchView.getQueryHint());

        // set setIconified field to false if we want to open up search box directly
        searchView.setIconified(false);

        Log.d(TAG, "onCreateOptionsMenu: returned" + true);
        Log.d(TAG, "onCreateOptionsMenu: out");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: called");

                // now we need to save the query entered by the user
                // into the search view widget in a place where all
                // our activities can see it
                // normally we use database for it but since this is
                // very small query we can use shared preferences
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(FLICKR_QUERY, query).apply();

                // we need to clear focus only if we are taking input
                // from a physical keyboard as typing enter is treated as
                // a click on the search icon
                searchView.clearFocus();

                // this method returns us to the previous activity
                // from which this intent was called
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: called");
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                finish();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}