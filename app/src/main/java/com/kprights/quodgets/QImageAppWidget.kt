package com.kprights.quodgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [QImageAppWidgetConfigureActivity]
 */
class QImageAppWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        val myWidget = ComponentName(context!!, QImageAppWidget::class.java)
        val manager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, manager, manager.getAppWidgetIds(myWidget)[0])
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, text: String? = "") {
    val widgetText = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.q_image_app_widget)

    val appWidgetTarget = AppWidgetTarget(context, R.id.QAppWidgetImageView, views, appWidgetId)
    Glide
        .with(context.applicationContext)
        .asBitmap()
        .load("http://openweathermap.org/img/wn/10n@4x.png")
        .into(appWidgetTarget as AppWidgetTarget)
    // QAppWidget.pushWidgetUpdate(context, views)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}