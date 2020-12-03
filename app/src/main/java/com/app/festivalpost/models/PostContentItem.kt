package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PostContentItem : Serializable {
    @SerializedName("post_content")
    @Expose
    var post_content = ""

    @SerializedName("post_id")
    @Expose
    var post_id = ""

    @SerializedName("post_category_id")
    @Expose
    var fest_id = ""

    constructor(post_content: String, post_id: String, fest_id: String) {
        this.post_content = post_content
        this.post_id = post_id
        this.fest_id = fest_id
    }

    constructor() {}
}