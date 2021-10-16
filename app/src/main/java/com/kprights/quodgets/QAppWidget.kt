package com.kprights.quodgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.text.Html
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import kotlinx.coroutines.Dispatchers


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [QAppWidgetConfigureActivity]
 */
class QAppWidget : AppWidgetProvider() {
    private var appWidgetTarget: AppWidgetTarget? = null

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            QuotesRepository(Dispatchers.Main).quotes.observeForever { list ->
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
        QuotesRepository(Dispatchers.Main).quotes.observeForever { list ->
            val myWidget = ComponentName(context, QAppWidget::class.java)
            val manager = AppWidgetManager.getInstance(context)
            updateAppWidget(context, manager, manager.getAppWidgetIds(myWidget)[0], list[0].html)
        }
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        fun pushWidgetUpdate(context: Context?, remoteViews: RemoteViews?) {
            val myWidget = ComponentName(context!!, QAppWidget::class.java)
            val manager = AppWidgetManager.getInstance(context)
            manager.updateAppWidget(myWidget, remoteViews)
        }
    }

    fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        widgetText: String?
    ) {
        val views = RemoteViews(context.packageName, R.layout.q_app_widget)
        views.setTextViewText(R.id.appwidget_text, Html.fromHtml(widgetText))
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}