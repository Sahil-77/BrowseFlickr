package com.admin_official.browseflickr;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "De_ReItemClickListener";

    interface OnRecyclerClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private final OnRecyclerClickListener callBack;
    private final GestureDetectorCompat gestureDetector;

    public RecyclerItemClickListener
            (Context context, RecyclerView recyclerView, OnRecyclerClickListener callBack) {
        this.callBack = callBack;
        this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: initiated");
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(view != null) {
                    Log.d(TAG, "onSingleTapUp: view found --> " + ((TextView)view.findViewById(R.id.title)).getText());
                    callBack.onItemClick(view, recyclerView.getChildAdapterPosition(view));
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: initiated");
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(view != null) {
                    Log.d(TAG, "onLongPress: view found --> " + ((TextView)view.findViewById(R.id.title)).getText());
                    callBack.onItemLongClick(view, recyclerView.getChildAdapterPosition(view));
                }
                super.onLongPress(e);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: in");
        if(gestureDetector != null) {
            boolean result = gestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: result --> " + result);
            return result;
        } else {
            Log.d(TAG, "onInterceptTouchEvent: returned --> " + false);
            return false;
        }
    }
}
