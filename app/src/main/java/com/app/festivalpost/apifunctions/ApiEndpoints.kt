package com.app.festivalpost.apifunctions

/**
 */
object ApiEndpoints {
    const val API_KEY = "Authkey"
    const val API_SECRET = "stock@123"

    // URLs
    // Dump Condition
    const val ConnectionTimeout = 100000 // = 100 seconds
    const val ConnectionSoTimeout = 150000 // = 150 seconds

    // ApiEndpoints Names

    const val addbusiness = "userapi/v3/addbusiness"
    const val updatebusiness = "userapi/v3/updatebusiness"
    const val removemybusiness = "userapi/v2/removemybusiness"
    const val getfestivalimages = "userapi/v2/getfestivalimages"
    const val savephotos = "userapi/v3/savephotos"
    const val getTemplates = "userapi/v2/getTemplates"

    //    public static final String getmycurrentbusiness = "userapi/getmycurrentbusiness";
    const val purchaseplan = "userapi/v3/purchaseplan"

    object ResultCodes {
        const val ResultOk = 200
        const val ResultCreatedOk = 201
        const val ResultFail = 404
        const val ResultBadFail = 404
    }
}