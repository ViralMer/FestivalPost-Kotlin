package com.app.festivalpost.apifunctions

import android.content.Context
import android.util.Log


import com.app.festivalpost.activity.MyApplication
import com.app.festivalpost.R
import com.app.festivalpost.globals.BuildConfig
import com.app.festivalpost.globals.BuildConfig.URL_NAME
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.emegamart.lelys.utils.extensions.getApiToken
import com.squareup.okhttp.*
import java.io.File
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 *
 */
class ApiManager {
    private var client: OkHttpClient
    private var context: Context
    private var acListener: ApiResponseListener

    constructor(context: Context) {
        client = OkHttpClient()
        this.context = context
        acListener = context as ApiResponseListener
        try {
            client.setConnectTimeout(ApiEndpoints.ConnectionTimeout.toLong(), TimeUnit.SECONDS)
            client.setWriteTimeout(ApiEndpoints.ConnectionTimeout.toLong(), TimeUnit.SECONDS)
            client.setReadTimeout(ApiEndpoints.ConnectionTimeout.toLong(), TimeUnit.SECONDS)
        } catch (e: Exception) {
        }
        val cacheDirectory = File(context.cacheDir, "https")
        val cacheSize = 10 * 1024 * 1024
        try {
            val cache = Cache(cacheDirectory, cacheSize.toLong())
            client.cache = cache
        } catch (e: Exception) {
            Log.d("Exception", e.message!!)
        }
        try {
            client.hostnameVerifier = NullHostNameVerifier()
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<X509TrustManager>(NullX509TrustManager()), SecureRandom())
            client.sslSocketFactory = sslContext.socketFactory
            client.sslSocketFactory = TLSSocketFactory()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }

    constructor(context: Context, acListener: ApiResponseListener) {
        client = OkHttpClient()
        this.context = context
        this.acListener = acListener
        try {
            client.setConnectTimeout(ApiEndpoints.ConnectionTimeout.toLong(), TimeUnit.SECONDS)
            client.setWriteTimeout(ApiEndpoints.ConnectionTimeout.toLong(), TimeUnit.SECONDS)
            client.setReadTimeout(ApiEndpoints.ConnectionTimeout.toLong(), TimeUnit.SECONDS)
        } catch (e: Exception) {
        }
        val cacheDirectory = File(context.cacheDir, "https")
        val cacheSize = 10 * 1024 * 1024
        try {
            val cache = Cache(cacheDirectory, cacheSize.toLong())
            client.cache = cache
        } catch (e: Exception) {
            Log.d("Exception", e.message!!)
        }
        try {
            client.hostnameVerifier = NullHostNameVerifier()
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<X509TrustManager>(NullX509TrustManager()), SecureRandom())
            client.sslSocketFactory = sslContext.socketFactory
            client.sslSocketFactory = TLSSocketFactory()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }

    fun executeRequest(servicename: String?, request: Request?) {
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(request: Request, e: IOException) {
                    Log.d("result - failure", "fail")
                    acListener.onErrorResponse(
                        servicename,
                        context.resources.getString(R.string.txt_unable_to_connect),
                        ApiEndpoints.ResultCodes.ResultFail
                    )
                }

                @Throws(IOException::class)
                override fun onResponse(response: Response) {
                    Log.d("Response : ", response.toString())
                    if (response.code() == ApiEndpoints.ResultCodes.ResultOk || response.code() == ApiEndpoints.ResultCodes.ResultCreatedOk) {
                        val responseCode = response.code()
                        val responseString = response.body().string()
                        Log.d("result - result ok", responseString)
                        acListener.onSuccessResponse(servicename, responseString, responseCode)
                    } else {
                        if (response.code() == ApiEndpoints.ResultCodes.ResultBadFail) {
                            val responseCode = response.code()
                            val responseString = response.body().string()
                            Log.d("result - result ok", responseString)
                            acListener.onSuccessResponse(servicename, responseString, responseCode)
                        } else {
                            Log.d("result - result cancel", "error")
                            acListener.onErrorResponse(
                                servicename,
                                context.resources.getString(R.string.txt_some_thing_wrong),
                                ApiEndpoints.ResultCodes.ResultFail
                            )
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun get_customer_address(servicename: String, id_customer: String?) {
        if (ConnectivityReceiver.isConnected) {
            val params = "?id=%s"
            val formatted_string = String.format(params, id_customer)
            val url: String = com.app.festivalpost.globals.BuildConfig.URL_NAME.toString() + servicename + formatted_string
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .get().build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun plans(servicename: String) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = com.app.festivalpost.globals.BuildConfig.URL_NAME.toString() + servicename
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .get().build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun testplans(servicename: String, business_id: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("business_id", business_id)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun gethomepage(servicename: String, token: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getdays(servicename: String, dateVal: String?, token: String?) {
        if (ConnectivityReceiver.isConnected) {
            Log.d("Date Val", dateVal!!)
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("date", dateVal)
                .add("token", token)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun setPrefernces(
        servicename: String,
        token: String?,
        is_delete: String?,
        preference_value: String?
    ) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .add("is_delete", is_delete)
                .add("preference_value", preference_value)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getcustomcategorypost(servicename: String) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .get().build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getphotos(servicename: String, token: String?, page: Int) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            Log.d("PREFETOKEN", "" + Global.getPreference(Constant.PREF_TOKEN, ""))
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .add("page", page.toString())
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun register(
        servicename: String,
        name: String?,
        email: String?,
        mobile: String?,
        referral_code: String?
    ) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val deviceToken = deviceToken
            val formBody = FormEncodingBuilder()
                .add("name", name)
                .add("email", email)
                .add("mobile", mobile)
                .add("ref_code", referral_code)
                .add("device_id", "MyApplication.deviceInfo!!.deviceUDID")
                .add("device_token", deviceToken)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    val deviceToken: String
        get() = Global.getPreference(Constant.PREF_DEVICE_TOKEN, "NA")!!

    fun login(servicename: String, mobile: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val deviceToken = deviceToken
            val formBody = FormEncodingBuilder()
                .add("mobile", mobile)
                .add("device_token", deviceToken)
                .add("device_id", "MyApplication.deviceInfo!!.deviceUDID")
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun purchaseplan(
        servicename: String,
        token: String?,
        business_id: String?,
        plan_id: String?,
        order_id: String?,
        amount: String?
    ) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .add("business_id", business_id)
                .add("plan_id", plan_id)
                .add("order_id", order_id)
                .add("purchase_id", amount)
                .add("device", "android")
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
            Log.d("RequestParam",""+request.toString())
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getfestivalimages(servicename: String, postcategoryid: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("postcategoryid", postcategoryid)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getFestivalVideos(servicename: String, token: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getcustomframeimages(servicename: String, token: String?, custom_cateogry_id: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .add("custom_cateogry_id", custom_cateogry_id)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun markascurrentbusiness(servicename: String, token: String?, business_id: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .add("business_id", business_id)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getmyallbusiness(servicename: String, token: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getTemplates(servicename: String, token: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getmycurrentbusiness(servicename: String, token: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun removemybusiness(servicename: String, id: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("id", id)
                .add("token", Global.getPreference(Constant.PREF_TOKEN, ""))
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun addbusiness(
        servicename: String,
        name: String?,
        email: String?,
        mobile: String?,
        mobile2: String?,
        website: String?,
        address: String?,
        category: String?,
        profilePath: String
    ) {
        if (ConnectivityReceiver.isConnected) {
            var result = ""
            val charset = "UTF-8"
            try {
                val multipart =
                    MultipartUtility(BuildConfig.URL_NAME.toString() + servicename, charset)
                multipart.addFormField("name", name)
                multipart.addFormField("email", email)
                multipart.addFormField("mobile", mobile)
                multipart.addFormField("mobile_second", mobile2)
                multipart.addFormField("website", website)
                multipart.addFormField("address", address)
                multipart.addFormField("business_category", category)
                multipart.addFormField("token", getApiToken())
                if (profilePath != "") {
                    multipart.addFilePart("logo", File(profilePath))
                }
                result = multipart.finish()
                acListener.onSuccessResponse(servicename, result, ApiEndpoints.ResultCodes.ResultOk)
            } catch (ex: IOException) {
                ex.printStackTrace()
                acListener.onErrorResponse(servicename, result, ApiEndpoints.ResultCodes.ResultFail)
            }
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun updatebusiness(
        servicename: String,
        id: String?,
        name: String?,
        email: String?,
        mobile: String?,
        mobile2: String?,
        website: String?,
        address: String?,
        category: String?,
        profilePath: String
    ) {
        if (ConnectivityReceiver.isConnected) {
            var result = ""
            val charset = "UTF-8"
            try {
                val multipart =
                    MultipartUtility(BuildConfig.URL_NAME.toString() + servicename, charset)
                multipart.addFormField("id", id)
                multipart.addFormField("name", name)
                multipart.addFormField("email", email)
                multipart.addFormField("mobile", mobile)
                multipart.addFormField("mobile_second", mobile2)
                multipart.addFormField("website", website)
                multipart.addFormField("address", address)
                multipart.addFormField("business_category", category)
                multipart.addFormField("token", getApiToken())
                if (profilePath != "") {
                    multipart.addFilePart("logo", File(profilePath))
                }
                Log.d("IDNAME", "ID :$id NAme:$mobile2" + "toekn:"+ getApiToken())
                result = multipart.finish()
                acListener.onSuccessResponse(servicename, result, ApiEndpoints.ResultCodes.ResultOk)
            } catch (ex: IOException) {
                ex.printStackTrace()
                acListener.onErrorResponse(servicename, result, ApiEndpoints.ResultCodes.ResultFail)
            }
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun editmyprofile(
        servicename: String,
        token: String?,
        name: String?,
        email: String?,
        mobile: String?
    ) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .add("name", name)
                .add("email", email)
                .add("mobile", mobile)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun getmyprofile(servicename: String, token: String?) {
        if (ConnectivityReceiver.isConnected) {
            val url: String = BuildConfig.URL_NAME.toString() + servicename
            val formBody = FormEncodingBuilder()
                .add("token", token)
                .build()
            val request =
                Request.Builder().url(url).header(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
                    .post(formBody).build()
            executeRequest(servicename, request)
        } else {
            acListener.isConnected(servicename, false)
        }
    }

    fun savephotos(servicename: String, image: String, business_id: String?) {
        if (ConnectivityReceiver.isConnected) {
            var result = ""
            val charset = "UTF-8"
            try {
                val multipart =
                    MultipartUtility(BuildConfig.URL_NAME.toString() + servicename, charset)
                multipart.addFormField("token", Global.getPreference(Constant.PREF_TOKEN, ""))
                multipart.addFormField("business_id", business_id)
                if (image != "") {
                    multipart.addFilePart("image", File(image))
                }
                result = multipart.finish()
                acListener.onSuccessResponse(servicename, result, ApiEndpoints.ResultCodes.ResultOk)
            } catch (ex: IOException) {
                ex.printStackTrace()
                acListener.onErrorResponse(servicename, result, ApiEndpoints.ResultCodes.ResultFail)
            }
        } else {
            acListener.isConnected(servicename, false)
        }
    }
}