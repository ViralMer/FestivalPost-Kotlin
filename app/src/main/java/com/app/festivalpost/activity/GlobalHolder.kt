package com.app.festivalpost.activity

import com.app.festivalpost.activity.PickerManager

class GlobalHolder private constructor() {
    var pickerManager: PickerManager? = null

    companion object {
        val instance = GlobalHolder()
    }
}