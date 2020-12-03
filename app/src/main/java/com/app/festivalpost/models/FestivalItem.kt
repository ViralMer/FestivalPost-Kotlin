package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FestivalItem : Serializable {
    @SerializedName("fest_id")
    @Expose
    var festId = ""

    @SerializedName("fest_name")
    @Expose
    var festName = ""

    @SerializedName("fest_date")
    @Expose
    var festDate = ""

    @SerializedName("fest_info")
    @Expose
    var festInfo = ""

    @SerializedName("fest_is_delete")
    @Expose
    var festIsDelete = ""

    @SerializedName("fest_image")
    @Expose
    var festImage = ""

    @SerializedName("fest_type")
    @Expose
    var festType = ""

    @SerializedName("fest_day")
    @Expose
    var festDay = ""

    @SerializedName("current_date")
    @Expose
    var isCurrentDate = false
}