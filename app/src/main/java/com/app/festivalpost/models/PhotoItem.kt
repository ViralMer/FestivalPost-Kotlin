package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PhotoItem : Serializable {
    @SerializedName("post_id")
    @Expose
    var post_id = 0

    @SerializedName("post_category_id")
    @Expose
    var post_category_id = 0

    @SerializedName("post_content")
    @Expose
    var post_content = ""
    var post_is_deleted = 0
    fun setIs_selected(is_selected: Boolean) {
        isIs_selected = is_selected
    }

    var isIs_selected = false
        private set
}