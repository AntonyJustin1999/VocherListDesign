package com.test.app.LoadMaps.Utils

import com.google.gson.annotations.SerializedName

class ReverseGeoCodeApi {
        @SerializedName("results")
        var results: ArrayList<Results>? = null
    }

    class Results {
        @SerializedName("formatted_address")
        var formatted_address: String? = null
    }