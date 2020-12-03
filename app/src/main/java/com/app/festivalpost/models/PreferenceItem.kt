package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PreferenceItem : Serializable {
    @SerializedName("user_preference_id")
    @Expose
    var user_preference_id = 0

    @SerializedName("user_id")
    @Expose
    var user_id = 0

    @SerializedName("preference_value")
    @Expose
    var preference_value = 0

    constructor(user_preference_id: Int, user_id: Int, preference_value: Int) {
        this.user_preference_id = user_preference_id
        this.user_id = user_id
        this.preference_value = preference_value
    }

    constructor() {}
}