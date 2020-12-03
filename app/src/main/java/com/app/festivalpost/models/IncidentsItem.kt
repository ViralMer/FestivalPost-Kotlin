package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class IncidentsItem : Serializable {
    @SerializedName("title")
    @Expose
    var title = ""

    @SerializedName("information")
    @Expose
    var information = ""

    @SerializedName("fest_image")
    @Expose
    var fest_image = ""

    @SerializedName("fest_id")
    @Expose
    var fest_id = ""

    @SerializedName("img_url")
    @Expose
    var postContentArrayList = ArrayList<PostContentItem>()

    constructor(title: String, postContentArrayList: ArrayList<PostContentItem>) {
        this.title = title
        this.postContentArrayList = postContentArrayList
    }

    constructor() {}
}