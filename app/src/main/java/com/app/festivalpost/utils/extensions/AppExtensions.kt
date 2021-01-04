package com.emegamart.lelys.utils.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi

import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.FestivalPost
import com.app.festivalpost.FestivalPost.Companion.getAppInstance
import com.app.festivalpost.FestivalPost.Companion.noInternetDialog
import com.app.festivalpost.R
import com.app.festivalpost.activity.AddBusinessActivity
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.FrameListItem1
import com.app.festivalpost.models.UserDataItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.app.festivalpost.utils.Constants.KeyIntent.CURRENT_DATE
import com.app.festivalpost.utils.Constants.KeyIntent.DEVICE_ID
import com.app.festivalpost.utils.Constants.KeyIntent.DEVICE_TOKEN
import com.app.festivalpost.utils.Constants.KeyIntent.DEVICE_TYPE
import com.app.festivalpost.utils.Constants.PLAY_STORE_URL_PREFIX
import com.app.festivalpost.utils.Constants.SharedPref.IS_LOGGED_IN
import com.app.festivalpost.utils.Constants.SharedPref.KEY_CURRENT_BUSINESS
import com.app.festivalpost.utils.Constants.SharedPref.KEY_CURRENT_BUSINESS_DATA
import com.app.festivalpost.utils.Constants.SharedPref.KEY_FRAME_LIST
import com.app.festivalpost.utils.Constants.SharedPref.USER_EMAIL
import com.app.festivalpost.utils.Constants.SharedPref.USER_ID
import com.app.festivalpost.utils.Constants.SharedPref.USER_NAME
import com.app.festivalpost.utils.Constants.SharedPref.USER_NUMBER
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.app.festivalpost.utils.SessionManager
import com.emegamart.lelys.utils.SharedPrefUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_no_internet.*


/*
fun isLoggedIn(): Boolean = getSharedPrefInstance().getBooleanValue(IS_LOGGED_IN)
fun getUserId(): String = getSharedPrefInstance().getStringValue(USER_ID)
fun getCurrentDate(): String = getSharedPrefInstance().getStringValue(CURRENT_DATE)
fun getDeviceID(): String = getSharedPrefInstance().getStringValue(DEVICE_ID)
fun getDeviceToken(): String = getSharedPrefInstance().getStringValue(DEVICE_TOKEN)
fun getDeviceType(): String = getSharedPrefInstance().getStringValue(DEVICE_TYPE)


fun getMobileNumber(): String = getSharedPrefInstance().getStringValue(USER_NUMBER)
fun getEmail(): String = getSharedPrefInstance().getStringValue(USER_EMAIL)
fun getUserName(): String = getSharedPrefInstance().getStringValue(USER_NAME)
fun getApiToken(): String = getSharedPrefInstance().getStringValue(USER_TOKEN)

fun clearLoginPref() {
    getSharedPrefInstance().removeKey(IS_LOGGED_IN)
    getSharedPrefInstance().removeKey(USER_ID)
    getSharedPrefInstance().removeKey(USER_EMAIL)
    getSharedPrefInstance().removeKey(USER_TOKEN)
    getSharedPrefInstance().removeKey(USER_NUMBER)
    getSharedPrefInstance().clear()


}
*/



fun RecyclerView.rvItemAnimation() {
    layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
}


fun ImageView.loadImageFromUrl(
    aImageUrl: String,
    aPlaceHolderImage: Int = R.drawable.placeholder_img,
    aErrorImage: Int = R.drawable.placeholder_img
) {
    try {
        if (!aImageUrl.checkIsEmpty()) {
            Glide.with(getAppInstance()).load(aImageUrl).placeholder(aPlaceHolderImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE).error(aErrorImage).into(this)
        } else {
            loadImageFromDrawable(aPlaceHolderImage)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ImageView.loadImageFromDrawable(@DrawableRes aPlaceHolderImage: Int) {
    Glide.with(getAppInstance()).load(aPlaceHolderImage).diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this)
}

fun shareMyApp(context: Context, subject: String, message: String) {
    try {
        val appUrl = PLAY_STORE_URL_PREFIX + context.packageName
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, subject)
        var leadingText = "\n" + message + "\n\n"
        leadingText += appUrl + "\n\n"
        i.putExtra(Intent.EXTRA_TEXT, leadingText)
        context.startActivity(Intent.createChooser(i, "Share using"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

enum class JsonFileCode {
    NO_INTERNET,
    LOADER
}



fun Activity.openLottieDialog(
    jsonFileCode: JsonFileCode = JsonFileCode.NO_INTERNET,
    onLottieClick: () -> Unit
) {
    val jsonFile: String = when (jsonFileCode) {
        JsonFileCode.NO_INTERNET -> "lottie/no_internet.json"
        JsonFileCode.LOADER -> "lottie/loader.json"
    }

    if (noInternetDialog == null) {
        noInternetDialog = Dialog(this, R.style.FullScreenDialog)
        noInternetDialog?.setContentView(R.layout.dialog_no_internet); noInternetDialog?.setCanceledOnTouchOutside(
            false
        ); noInternetDialog?.setCancelable(false)
        noInternetDialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        noInternetDialog?.rlLottie?.onClick {
            if (!isNetworkAvailable()) {
                snackBarError(getAppInstance().getString(R.string.error_no_internet))
                return@onClick
            }
            noInternetDialog?.dismiss()
            onLottieClick()
        }
    }
    noInternetDialog?.lottieNoInternet?.setAnimation(jsonFile)
    if (!noInternetDialog!!.isShowing) {
        noInternetDialog?.show()
    }
}

fun getCustomFrameList(context: Context): ArrayList<FrameListItem1> {
    val sessionManager=SessionManager(context)
    if (sessionManager.getValueString(KEY_FRAME_LIST) == "") {
        return ArrayList()
    }

    return Gson().fromJson<ArrayList<FrameListItem1>>(
        sessionManager.getValueString(
            KEY_FRAME_LIST
        ), object : TypeToken<ArrayList<FrameListItem1>>() {}.type
    )
}





fun <T> put (`object`:T,key:String,context: Context)
{
    val jsonString=GsonBuilder().create().toJson(`object`);
    val sessionManager=SessionManager(context)
    sessionManager.setStringValue(key,jsonString)

}

inline fun <reified T> get(key: String,context: Context): T?{
    val sessionManager=SessionManager(context)
    val value = sessionManager.getValueString(key)
    return GsonBuilder().create().fromJson(value,T::class.java)
}












