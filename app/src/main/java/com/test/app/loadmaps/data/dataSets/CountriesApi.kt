package com.test.app.loadmaps.data.dataSets

import Flags
import com.google.gson.annotations.SerializedName

class CountriesApi {
    @SerializedName("flags")
    var flags: Flags? = null

    @SerializedName("name")
    var name: Name? = null

}