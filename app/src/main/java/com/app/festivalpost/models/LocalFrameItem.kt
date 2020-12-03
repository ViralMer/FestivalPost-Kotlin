package com.app.festivalpost.models

import com.app.festivalpost.R
import java.io.Serializable

class LocalFrameItem(layout_id: Int, preview_id: Int) : Serializable {
    var layout_id = R.layout.custom_frame_1
    var preview_id = R.layout.custom_frame_preview_1

    init {
        this.layout_id = layout_id
        this.preview_id = preview_id
    }
}