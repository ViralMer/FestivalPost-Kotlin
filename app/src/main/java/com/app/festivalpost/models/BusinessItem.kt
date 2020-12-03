package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BusinessItem : Serializable {
    @SerializedName("busi_id")
    @Expose
    var busiId = 0

    @SerializedName("busi_name")
    @Expose
    var busiName = ""

    @SerializedName("busi_email")
    @Expose
    var busiEmail = ""

    @SerializedName("busi_address")
    @Expose
    var busiAddress = ""

    @SerializedName("busi_mobile")
    @Expose
    var busiMobile = ""

    @SerializedName("busi_logo")
    @Expose
    var busiLogo = ""

    @SerializedName("busi_website")
    @Expose
    var busiWebsite = ""

    @SerializedName("plan_name")
    @Expose
    var planName = ""

    @SerializedName("purc_start_date")
    @Expose
    var purcStartDate = ""

    @SerializedName("purc_end_date")
    @Expose
    var purcEndDate = ""

    @SerializedName("need_to_upgrade")
    @Expose
    var need_to_upgrade = ""

    @SerializedName("plan_id")
    @Expose
    var plan_id = ""
    var isCurrentBusiness = false
}