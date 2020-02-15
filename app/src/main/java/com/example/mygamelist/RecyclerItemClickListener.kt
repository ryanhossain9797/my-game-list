//---------------------------This item click listener is ADDED to the recyclerview
package com.example.mygamelist

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView


//interface to ensure main activity has these features
interface OnRecyclerClickListener{
    fun onItemClick(view: View, position: Int)
    fun onItemLongClick(view: View, position: Int)
}

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView,                //of type SimpleOnItemTouchListener
                                private val listener: OnRecyclerClickListener): RecyclerView.SimpleOnItemTouchListener(){
    private val TAG = "RecyclerItemClickLis"
                                                                //Anonymous implementation of SimpleOnGestureListener
    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            Log.d(TAG,"gestureDetector: single tap confirmed")
            val childView = recyclerView.findChildViewUnder(e.x,e.y)!!
            listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))//calling onItemClick callback with view and position
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            Log.d(TAG,"gestureDetector: long press")
            val childView = recyclerView.findChildViewUnder(e.x,e.y)!!
            listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))//calling onItemLongClick callback with view and position
        }
    })

    //overriding the touch event interceptor
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterruptTouchEvent: called")
        return gestureDetector.onTouchEvent(e)//sending event to simpleongesturelistener so it can consume events
    }
}