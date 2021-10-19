package com.kprights.quodgets

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers


/**
 * The configuration screen for the [QAppWidget] AppWidget.
 */
class QAppWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var onClickListener = View.OnClickListener {
        val context = this
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val myWidget = ComponentName(context, QAppWidget::class.java)
        if(appWidgetManager.getAppWidgetIds(myWidget).isEmpty()) addWidget()

        getNewRandomQuote(context)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setResult(RESULT_CANCELED)
        setContentView(R.layout.q_app_widget_configure)
        findViewById<View>(R.id.add_button).setOnClickListener(onClickListener)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        addAdMob()
        getNewRandomQuote(this)
    }

    private fun addWidget(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mAppWidgetManager = AppWidgetManager.getInstance(this)
            val myProvider = ComponentName(this, QAppWidget::class.java)
            val b = Bundle()
            b.putString("string", "string")
            if (mAppWidgetManager.isRequestPinAppWidgetSupported) {
                val pinnedWidgetCallbackIntent =
                    Intent(this, QAppWidget::class.java)
                val successCallback = PendingIntent.getBroadcast(
                    this, 0,
                    pinnedWidgetCallbackIntent, 0
                )
                mAppWidgetManager.requestPinAppWidget(myProvider, b, successCallback)
            }
        }
    }

    private fun addAdMob(){
        MobileAds.initialize(this) {}

        val favAdViewTop = findViewById<AdView>(R.id.favAdViewTop)
        favAdViewTop.loadAd(AdRequest.Builder().build())

        val favAdViewBottom = findViewById<AdView>(R.id.favAdViewBottom)
        favAdViewBottom.loadAd(AdRequest.Builder().build())
    }
}

internal fun getNewRandomQuote(context: Context){
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val myWidget = ComponentName(context, QAppWidget::class.java)

    for (appWidgetId in appWidgetManager.getAppWidgetIds(myWidget)) {
        QuotesRepository(Dispatchers.Main).quotes.observeForever {
                quotes ->
            if (quotes.isNullOrEmpty()){
                val quote = context.resources.getStringArray(R.array.default_random_quotes).random()
                updateTextAppWidget(context, appWidgetManager, appWidgetId, quote)
            } else {
                val quote = quotes.random().html
                updateTextAppWidget(context, appWidgetManager, appWidgetId, quote)
            }
        }
    }
}