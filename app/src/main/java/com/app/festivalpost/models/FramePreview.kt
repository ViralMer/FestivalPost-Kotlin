package com.app.festivalpost.models

import com.app.festivalpost.R
import java.io.Serializable

class FramePreview : Serializable {
    var layout_id = R.layout.custom_frame_1
    var images: Int? = null
    var dynamic_images: String? = null
    fun setIs_selected(is_selected: Boolean) {
        isIs_selected = is_selected
    }

    var isIs_selected = false
        private set

    constructor(layout_id: Int, images: Int?) {
        this.layout_id = layout_id
        this.images = images
    }

    constructor(layout_id: Int, images: String?) {
        this.layout_id = layout_id
        dynamic_images = images
    }
}