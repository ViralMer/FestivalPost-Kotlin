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
    const val register = "userapi/v2/register"
    const val login = "userapi/v2/login"
    const val plans = "plan/plans"
    const val test_plans = "userapi/v2/testplan"
    const val getmyallbusiness = "userapi/v2/getmyallbusiness"
    const val addbusiness = "userapi/v2/addbusiness"
    const val updatebusiness = "userapi/v2/updatebusiness"
    const val removemybusiness = "userapi/v2/removemybusiness"
    const val getmyprofile = "userapi/v2/getmyprofile"
    const val editmyprofile = "userapi/v2/editmyprofile"
    const val gethomepage = "userapi/v2/gethomepage"
    const val getdays = "userapi/v2/getdays"
    const val setpreference = "userapi/v2/setpreference"
    const val getfestivalimages = "userapi/v2/getfestivalimages"
    const val getfestivalvideos = "userapi/v2/getVideoPosts"
    const val getCustomCategoryImages = "userapi/v2/getCustomCategoryImages"
    const val markascurrentbusiness = "userapi/v2/markascurrentbusiness"
    const val savephotos = "userapi/v2/savephotos"
    const val getphotos = "userapi/v2/getphotos"
    const val getTemplates = "userapi/v2/getTemplates"
    const val getcustomcategorypost = "userapi/v2/getcustomcategorypost"

    //    public static final String getmycurrentbusiness = "userapi/getmycurrentbusiness";
    const val purchaseplan = "userapi/v2/purchaseplan"

    object ResultCodes {
        const val ResultOk = 200
        const val ResultCreatedOk = 201
        const val ResultFail = 404
        const val ResultBadFail = 404
    }
}