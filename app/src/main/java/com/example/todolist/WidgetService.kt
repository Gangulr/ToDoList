package com.example.todolist

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.todolist.R

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetRemoteViewsFactory(this.applicationContext, intent)
    }
}

class WidgetRemoteViewsFactory(
    private val context: Context,
    private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    // Implement methods to handle the data for the ListView
    override fun onCreate() {}
    override fun onDataSetChanged() {}
    override fun onDestroy() {}

    override fun getCount(): Int {
        // Return the number of items in the ListView
        return 0
    }

    override fun getViewAt(position: Int): RemoteViews? {
        // Return the RemoteViews for each item
        val rv = RemoteViews(context.packageName, R.layout.widget_list_item)
        // Populate the widget item with data here
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        // Return a view displayed while data is loading
        return null
    }

    override fun getViewTypeCount(): Int {
        // Return the number of different view types
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}
