package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class FrameContentListItem : Serializable {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("custom_cateogry_id")
    @Expose
    var custom_cateogry_id: Int? = null

    @SerializedName("catdata")
    @Expose
    var contentItemArrayList: ArrayList<FrameContentItem>? = null

    constructor(
        name: String?,
        custom_cateogry_id: Int?,
        contentItemArrayList: ArrayList<FrameContentItem>?
    ) {
        this.name = name
        this.custom_cateogry_id = custom_cateogry_id
        this.contentItemArrayList = contentItemArrayList
    }

    constructor() {}
}