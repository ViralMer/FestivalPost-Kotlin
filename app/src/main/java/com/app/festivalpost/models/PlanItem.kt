package com.app.festivalpost.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class PlanItem : Serializable {
    @SerializedName("plan_sku")
    @Expose
    var sku = ""

    @SerializedName("plan_id")
    @Expose
    var planId = 0

    @SerializedName("plan_name")
    @Expose
    var planName = ""

    @SerializedName("plan_actual_price")
    @Expose
    var planActualPrice = ""

    @SerializedName("plan_discount_price")
    @Expose
    var planDiscountPrice = ""

    @SerializedName("plan_descount")
    @Expose
    var planDescount = ""

    @SerializedName("plan_information")
    @Expose
    var planInformation: List<String> = ArrayList()

    @SerializedName("plan_validity")
    @Expose
    var planValidity = ""

    @SerializedName("plan_validity_type")
    @Expose
    var planValidityType = ""
}