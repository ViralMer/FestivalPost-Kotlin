package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class  FrameListItem : Serializable {
    @SerializedName("user_frames_id")
    @Expose
    var user_frames_id = 0

    @SerializedName("user_id")
    @Expose
    var user_id = 0

    @SerializedName("frame_url")
    @Expose
    var frame_url = ""

    constructor(user_frames_id: Int, user_id: Int, frame_url: String) {
        this.user_frames_id = user_frames_id
        this.user_id = user_id
        this.frame_url = frame_url
    }

    constructor() {}
}