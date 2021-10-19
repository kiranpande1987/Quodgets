package com.kprights.quodgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.text.Html
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [QAppWidgetConfigureActivity]
 */
class QAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        getNewRandomQuote(context)
    }

    override fun onEnabled(context: Context) {
        getNewRandomQuote(context)
    }
}

internal fun updateTextAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    widgetText: String?
) {
    val views = RemoteViews(context.packageName, R.layout.q_app_widget)
    views.setTextViewText(R.id.appwidget_text, Html.fromHtml(widgetText))
    appWidgetManager.updateAppWidget(appWidgetId, views)
}