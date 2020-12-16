package com.app.festivalpost.utils

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    val FULL_DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    val DATE_FORMAT = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
    val DD_MMM_YYYY = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    val YYYY_MM_DD = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    const val PLAY_STORE_URL_PREFIX = "https://play.google.com/store/apps/details?id="

    const val myPreferences = "FestivalPost"

    object Config {
        const  val DEFAULT_URL = "http://festivalpost.in/admin/api/"
        const val  VERSION = "userapi/v3/"


    }
    object SharedPref {
        const val KEY_FRAME_LIST = "frame_list"
        const val KEY_USER_DATA = "user_data"
        const val KEY_CURRENT_BUSINESS = "current_business"
        const val IS_LOGGED_IN = "isLoggedIn"
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_NUMBER = "user_number"
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"

    }

    object KeyIntent {
        const val LOG_OUT = "logout"
        const val IS_PREMIUM = "is_premium"
        const val CURRENT_DATE = "current_date"
        const val DEVICE_TYPE = "device_type"
        const val DEVICE_ID = "device_id"
        const val DEVICE_TOKEN = "device_token"
    }
    object DateFormatCodes {
        const val YMD_HMS = 0
        const val YMD = 1
    }
}
