package com.kprights.quodgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.text.Html
import android.util.Log
import android.widget.RemoteViews
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [QAppWidgetConfigureActivity]
 */
class QAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            QuotesRepository(Dispatchers.Main).quotes.observeForever {
                list ->
                updateAppWidget(context, appWidgetManager, appWidgetId, list[0].html)
            }
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
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    text: String?
) {
    val widgetText = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.q_app_widget)
    views.setTextViewText(R.id.appwidget_text, Html.fromHtml(text))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}