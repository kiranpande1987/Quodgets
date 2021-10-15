package com.kprights.quodgets

import com.google.gson.annotations.SerializedName


/**
 * Copyright (c) 2021 for KPrights
 *
 * User : Kiran Pande
 * Date : 16/10/21
 * Time : 2:32 AM
 */

data class Quote (
    @SerializedName("q") var quote: String? = "",
    @SerializedName("a") var auther: String? = "",
    @SerializedName("h") var html: String? = ""
)