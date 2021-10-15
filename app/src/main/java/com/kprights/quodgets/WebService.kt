package com.kprights.quodgets

import android.provider.DocumentsContract
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Copyright (c) 2021 for KPrights
 *
 * User : Kiran Pande
 * Date : 16/10/21
 * Time : 2:24 AM
 */

private const val BASE_URL = "https://zenquotes.io/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

val WebService: Api by lazy { retrofit.create(Api::class.java) }

interface Api {
    @GET("api/random")
    fun getRandomQuote(): Deferred<List<Quote>>
}