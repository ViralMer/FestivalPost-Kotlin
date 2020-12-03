package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserItem : Serializable {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("name")
    @Expose
    var name = ""

    @SerializedName("email")
    @Expose
    var email = ""

    @SerializedName("mobile")
    @Expose
    var mobile = ""

    @SerializedName("email_verified_at")
    @Expose
    private var emailVerifiedAt = ""

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("created_at")
    @Expose
    var createdAt = ""

    @SerializedName("updated_at")
    @Expose
    var updatedAt = ""

    @SerializedName("ref_code")
    @Expose
    var refCode = ""

    @SerializedName("device_id")
    @Expose
    var deviceId = ""

    @SerializedName("device_token")
    @Expose
    var deviceToken = ""

    @SerializedName("user_credit")
    @Expose
    var userCredit = 0

    @SerializedName("ref_users")
    @Expose
    private var refUsers = ""

    @SerializedName("default_business_id")
    @Expose
    var default_business_id = ""
    fun getEmailVerifiedAt(): Any {
        return emailVerifiedAt
    }

    fun setEmailVerifiedAt(emailVerifiedAt: String) {
        this.emailVerifiedAt = emailVerifiedAt
    }

    fun getRefUsers(): Any {
        return refUsers
    }

    fun setRefUsers(refUsers: String) {
        this.refUsers = refUsers
    }
}