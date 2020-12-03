package com.app.festivalpost.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class FrameContentItem : Serializable {
    @SerializedName("banner_image")
    @Expose
    var banner_image: String? = null

    @SerializedName("images")
    @Expose
    var images: Images? = null

    @SerializedName("custom_cateogry_id")
    @Expose
    var custom_cateogry_id: String? = null

    constructor(banner_image: String?, images: Images?, custom_cateogry_id: String?) {
        this.banner_image = banner_image
        this.images = images
        this.custom_cateogry_id = custom_cateogry_id
    }

    constructor() {}

    class Images : Serializable {
        @SerializedName("image_one")
        @Expose
        var image_one: String? = null

        @SerializedName("position_x")
        @Expose
        var position_x: String? = null

        @SerializedName("position_y")
        @Expose
        var position_y: String? = null

        @SerializedName("img_position_x")
        @Expose
        var img_position_x: String? = null

        @SerializedName("img_position_y")
        @Expose
        var img_position_y: String? = null

        @SerializedName("img_height")
        @Expose
        var img_height: String? = null

        @SerializedName("img_width")
        @Expose
        var img_width: String? = null

        constructor(
            image_one: String?,
            position_x: String?,
            position_y: String?,
            img_position_x: String?,
            img_position_y: String?,
            img_height: String?,
            img_width: String?
        ) {
            this.image_one = image_one
            this.position_x = position_x
            this.position_y = position_y
            this.img_position_x = img_position_x
            this.img_position_y = img_position_y
            this.img_height = img_height
            this.img_width = img_width
        }

        constructor() {}
    }
}