package com.example.todolist

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.todolist.R
import com.example.todolist.MainActivity

class MyWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Iterate through all widget instances
        for (appWidgetId in appWidgetIds) {
            // Create an Intent to launch MainActivity when the widget title is clicked
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Create the RemoteViews object for the widget
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setOnClickPendingIntent(R.id.widgetTitle, pendingIntent)

            // Set up the ListView in the widget
            val serviceIntent = Intent(context, WidgetService::class.java)
            views.setRemoteAdapter(R.id.widgetListView, serviceIntent)

            // Update the widget with new views
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
