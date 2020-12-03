package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SliderItem : Serializable {
    @SerializedName("custom_cateogry_id")
    @Expose
    var custom_cateogry_id = 0

    @SerializedName("slider_img")
    @Expose
    var slider_img: String? = null

    @SerializedName("slider_img_position")
    @Expose
    var slider_img_position = 0

    constructor(custom_cateogry_id: Int, slider_img: String?, slider_img_position: Int) {
        this.custom_cateogry_id = custom_cateogry_id
        this.slider_img = slider_img
        this.slider_img_position = slider_img_position
    }

    constructor() {}
}