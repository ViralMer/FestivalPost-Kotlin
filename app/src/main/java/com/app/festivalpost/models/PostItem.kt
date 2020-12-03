package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PostItem : Serializable {
    @SerializedName("photo_url")
    @Expose
    var photo_id = ""

    @SerializedName("photo_url")
    @Expose
    var photo_user_id = ""

    @SerializedName("photo_url")
    @Expose
    var name = ""

    @SerializedName("photo_url")
    @Expose
    var photo_url = ""
}