package com.app.festivalpost.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class FrameContentItemDetail : Serializable {
    @SerializedName("custom_cateogry_data_id")
    @Expose
    var custom_cateogry_data_id: Int? = null

    @SerializedName("custom_cateogry_id")
    @Expose
    var custom_cateogry_id: Int? = null

    @SerializedName("banner_image")
    @Expose
    var banner_image: String? = null

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
    fun setIs_selected(is_selected: Boolean) {
        isIs_selected = is_selected
    }

    var isIs_selected = false
        private set

    constructor(
        custom_cateogry_data_id: Int?,
        custom_cateogry_id: Int?,
        banner_image: String?,
        image_one: String?,
        position_x: String?,
        position_y: String?,
        is_selected: Boolean
    ) {
        this.custom_cateogry_data_id = custom_cateogry_data_id
        this.custom_cateogry_id = custom_cateogry_id
        this.banner_image = banner_image
        this.image_one = image_one
        this.position_x = position_x
        this.position_y = position_y
        isIs_selected = is_selected
    }

    constructor(
        custom_cateogry_data_id: Int?,
        custom_cateogry_id: Int?,
        banner_image: String?,
        image_one: String?,
        position_x: String?,
        position_y: String?,
        img_position_x: String?,
        img_position_y: String?,
        img_height: String?,
        img_width: String?,
        is_selected: Boolean
    ) {
        this.custom_cateogry_data_id = custom_cateogry_data_id
        this.custom_cateogry_id = custom_cateogry_id
        this.banner_image = banner_image
        this.image_one = image_one
        this.position_x = position_x
        this.position_y = position_y
        this.img_position_x = img_position_x
        this.img_position_y = img_position_y
        this.img_height = img_height
        this.img_width = img_width
        isIs_selected = is_selected
    }

    constructor() {}
}