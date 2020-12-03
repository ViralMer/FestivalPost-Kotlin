package com.app.festivalpost.models

import android.view.View

class ViewData {
    var view: View? = null
    var text: String? = null
    var position = 0
    var color = 0

    constructor(view: View?, text: String?, position: Int, color: Int) {
        this.view = view
        this.text = text
        this.position = position
        this.color = color
    }

    constructor() {}
    constructor(text: String?) {
        this.text = text
    }

    constructor(color: Int) {
        this.color = color
    }
}