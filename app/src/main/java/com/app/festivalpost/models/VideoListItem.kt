package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class VideoListItem : Serializable {
    @SerializedName("video_post_id")
    @Expose
    var video_post_id = 0

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    @SerializedName("video_url")
    @Expose
    var video_url = ""

    @SerializedName("date")
    @Expose
    var date = ""

    @SerializedName("is_deleted")
    @Expose
    var is_deleted = 0

    @SerializedName("date_added")
    @Expose
    var date_added = ""

    @SerializedName("color")
    @Expose
    var color = ""
    fun setIs_selected(is_selected: Boolean) {
        isIs_selected = is_selected
    }

    var isIs_selected = false
        private set

    constructor() {}
    constructor(
        video_post_id: Int,
        thumbnail: String?,
        video_url: String,
        date: String,
        is_deleted: Int,
        date_added: String
    ) {
        this.video_post_id = video_post_id
        this.thumbnail = thumbnail
        this.video_url = video_url
        this.date = date
        this.is_deleted = is_deleted
        this.date_added = date_added
    }
}