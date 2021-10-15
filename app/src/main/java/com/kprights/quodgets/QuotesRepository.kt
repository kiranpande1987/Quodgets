package com.kprights.quodgets

import android.provider.DocumentsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.net.UnknownHostException


/**
 * Copyright (c) 2021 for KPrights
 *
 * User : Kiran Pande
 * Date : 16/10/21
 * Time : 2:38 AM
 */

enum class ApiStatus { NO_INTERNET, LOADING, ERROR, DONE }

class QuotesRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Main
) {

    private val job = Job()
    private val scope = CoroutineScope(job + ioDispatcher)

    var quotes: MutableLiveData<List<Quote>> = MutableLiveData<List<Quote>>()
    val status: MutableLiveData<ApiStatus> = MutableLiveData<ApiStatus>()

    init {
        getQuotesFromRemote()
    }

    private fun getQuotesFromRemote(){
        scope.launch(ioDispatcher) {

            val quote = fetchQuotesFromRemote()

            quote.let {
                status.postValue(ApiStatus.DONE)
                quotes.postValue(quote)
            }
        }
    }

    private suspend fun fetchQuotesFromRemote(): List<Quote>? {
        try {
            status.postValue(ApiStatus.LOADING)
            val deferred = WebService.getRandomQuote()
            return deferred.await()
        } catch (e: Exception) {
            status.postValue(ApiStatus.ERROR)
        }

        return null
    }

    fun cancel() {
        job.cancel()
    }
}