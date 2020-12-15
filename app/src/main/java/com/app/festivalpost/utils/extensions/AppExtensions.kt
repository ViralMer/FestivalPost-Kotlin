package com.emegamart.lelys.utils.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.FestivalPost
import com.app.festivalpost.FestivalPost.Companion.getAppInstance
import com.app.festivalpost.FestivalPost.Companion.noInternetDialog
import com.app.festivalpost.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.emegamart.lelys.models.*
import com.emegamart.lelys.utils.Constants
import com.emegamart.lelys.utils.Constants.AppBroadcasts.ADDRESS_UPDATE
import com.emegamart.lelys.utils.Constants.AppBroadcasts.CART_COUNT_CHANGE
import com.emegamart.lelys.utils.Constants.AppBroadcasts.ORDER_COUNT_CHANGE
import com.emegamart.lelys.utils.Constants.AppBroadcasts.PROFILE_UPDATE
import com.emegamart.lelys.utils.Constants.AppBroadcasts.WISHLIST_UPDATE
import com.emegamart.lelys.utils.Constants.KeyIntent.CURRENT_DATE
import com.emegamart.lelys.utils.Constants.KeyIntent.DATA
import com.emegamart.lelys.utils.Constants.PLAY_STORE_URL_PREFIX
import com.emegamart.lelys.utils.Constants.SharedPref.CART_DATA
import com.emegamart.lelys.utils.Constants.SharedPref.CATEGORY_DATA
import com.emegamart.lelys.utils.Constants.SharedPref.COUPON_DATA
import com.emegamart.lelys.utils.Constants.SharedPref.DEFAULT_CURRENCY
import com.emegamart.lelys.utils.Constants.SharedPref.IS_LOGGED_IN
import com.emegamart.lelys.utils.Constants.SharedPref.IS_SOCIAL_LOGIN
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_ADDRESS
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_CART_COUNT
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_CATEGORY_COUNT
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_DASHBOARD
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_ORDERS
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_RECENTS
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_USER_ADDRESS
import com.emegamart.lelys.utils.Constants.SharedPref.KEY_WISHLIST_COUNT
import com.emegamart.lelys.utils.Constants.SharedPref.PRODUCT_LIST_DATA
import com.emegamart.lelys.utils.Constants.SharedPref.REDEEM_POINTS
import com.emegamart.lelys.utils.Constants.SharedPref.SHIPPING_METHODS
import com.emegamart.lelys.utils.Constants.SharedPref.SLIDER_IMAGES_DATA
import com.emegamart.lelys.utils.Constants.SharedPref.TESTIMONIALS_DATA
import com.emegamart.lelys.utils.Constants.SharedPref.THEME_COLOR
import com.emegamart.lelys.utils.Constants.SharedPref.USER_DISPLAY_NAME
import com.emegamart.lelys.utils.Constants.SharedPref.USER_EMAIL
import com.emegamart.lelys.utils.Constants.SharedPref.USER_FIRST_NAME
import com.emegamart.lelys.utils.Constants.SharedPref.USER_ID
import com.emegamart.lelys.utils.Constants.SharedPref.USER_LAST_NAME
import com.emegamart.lelys.utils.Constants.SharedPref.USER_NICE_NAME
import com.emegamart.lelys.utils.Constants.SharedPref.USER_PASSWORD
import com.emegamart.lelys.utils.Constants.SharedPref.USER_PROFILE
import com.emegamart.lelys.utils.Constants.SharedPref.USER_ROLE
import com.emegamart.lelys.utils.Constants.SharedPref.USER_TOKEN
import com.emegamart.lelys.utils.Constants.SharedPref.USER_USERNAME
import com.emegamart.lelys.utils.Constants.SharedPref.WISHLIST_DATA
import com.emegamart.lelys.utils.SharedPrefUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_no_internet.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

fun isLoggedIn(): Boolean = getSharedPrefInstance().getBooleanValue(IS_LOGGED_IN)
fun getUserId(): String = getSharedPrefInstance().getStringValue(USER_ID)
fun getDefaultCurrency(): String = getSharedPrefInstance().getStringValue(DEFAULT_CURRENCY)
fun getThemeColor(): String = getSharedPrefInstance().getStringValue(THEME_COLOR)
fun getCurrentDate(): String = getSharedPrefInstance().getStringValue(CURRENT_DATE)


fun getUserName(): String = getSharedPrefInstance().getStringValue(USER_USERNAME)
fun getFirstName(): String = getSharedPrefInstance().getStringValue(USER_FIRST_NAME)
fun getLastName(): String = getSharedPrefInstance().getStringValue(USER_LAST_NAME)
fun getUserProfile(): String = getSharedPrefInstance().getStringValue(USER_PROFILE)
fun getEmail(): String = getSharedPrefInstance().getStringValue(USER_EMAIL)
fun getPassword(): String = getSharedPrefInstance().getStringValue(USER_PASSWORD)
fun getApiToken(): String = getSharedPrefInstance().getStringValue(USER_TOKEN)
fun getCartCount(): String = getSharedPrefInstance().getIntValue(KEY_CART_COUNT, 0).toString()
fun getRewardPoints(): String = getSharedPrefInstance().getStringValue(REDEEM_POINTS)

fun clearLoginPref() {
    getSharedPrefInstance().removeKey(IS_LOGGED_IN)
    getSharedPrefInstance().removeKey(USER_ID)
    getSharedPrefInstance().removeKey(USER_DISPLAY_NAME)
    getSharedPrefInstance().removeKey(USER_EMAIL)
    getSharedPrefInstance().removeKey(USER_NICE_NAME)
    getSharedPrefInstance().removeKey(USER_TOKEN)
    getSharedPrefInstance().removeKey(USER_FIRST_NAME)
    getSharedPrefInstance().removeKey(USER_LAST_NAME)
    getSharedPrefInstance().removeKey(USER_PROFILE)
    getSharedPrefInstance().removeKey(USER_ROLE)
    getSharedPrefInstance().removeKey(USER_USERNAME)
    getSharedPrefInstance().removeKey(WISHLIST_DATA)
    getSharedPrefInstance().removeKey(TESTIMONIALS_DATA)
    getSharedPrefInstance().removeKey(CART_DATA)
    getSharedPrefInstance().removeKey(KEY_RECENTS)
    getSharedPrefInstance().removeKey(KEY_DASHBOARD)
    getSharedPrefInstance().removeKey(KEY_ADDRESS)
    getSharedPrefInstance().removeKey(KEY_USER_ADDRESS)
    getSharedPrefInstance().removeKey(KEY_CART_COUNT)
}

fun getSharedPrefInstance(): SharedPrefUtils {
    return if (FestivalPost.sharedPrefUtils == null) {
        FestivalPost.sharedPrefUtils = SharedPrefUtils()
        FestivalPost.sharedPrefUtils!!
    } else {
        FestivalPost.sharedPrefUtils!!
    }
}

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

fun Context.fontMedium(): Typeface? {
    return Typeface.createFromAsset(assets, getString(R.string.font_bold))
}

fun Context.fontSemiBold(): Typeface? {
    return Typeface.createFromAsset(assets, getString(R.string.font_medium))
}

fun Context.fontBold(): Typeface? {
    return Typeface.createFromAsset(assets, getString(R.string.font_semibold))
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.makeTransparentStatusBar() {
    val window = this.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = this.resources.getColor(R.color.addTextBackgroundColor)
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
/*    noInternetDialog?.lottieNoInternet?.setAnimation(jsonFile)
    if (!noInternetDialog!!.isShowing) {
        noInternetDialog?.show()
    }*/
}







