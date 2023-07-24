package com.test.app.loadmaps.data.dataSets

import Flags
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class CountryDetailsApi {
    @SerializedName("flags")
    var flags: Flags? = null

    @SerializedName("coatOfArms")
    var coatOfArms: CoatOfArms? = null

    @SerializedName("name")
    var name: Name? = null

    @SerializedName("startOfWeek")
    var startOfWeek: String? = null

    @SerializedName("currencies")
    var currencies: JSONObject? = null

    @SerializedName("capital")
    var capital: ArrayList<String>? = null

    @SerializedName("languages")
    var languages: JSONObject? = null

    @SerializedName("latlng")
    var latLng: ArrayList<Float>? = null

    @SerializedName("borders")
    var borders: ArrayList<String>? = null

    @SerializedName("area")
    var area: Double? = null

    @SerializedName("population")
    var population: Long? = null

    @SerializedName("continents")
    var continents: ArrayList<String>? = null
}
