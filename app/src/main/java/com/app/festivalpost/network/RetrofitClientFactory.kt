package com.emegamart.lelys.network

import com.google.gson.GsonBuilder
import com.emegamart.lelys.WooBoxApp.Companion.okHttpClient
import com.emegamart.lelys.utils.Constants.Config.DEFAULT_URL
import com.emegamart.lelys.utils.Constants.Config.consumerKey
import com.emegamart.lelys.utils.Constants.Config.consumerSecret
import com.emegamart.lelys.utils.Constants.Config.token
import com.emegamart.lelys.utils.Constants.Config.tokenSecret
import com.emegamart.lelys.utils.oauthInterceptor.OAuthInterceptor
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClientFactory {

    fun getRetroFitClient(b: Boolean = true): Retrofit {
        val oauth1WooCommerce = OAuthInterceptor.Builder().consumerKey(consumerKey).consumerSecret(consumerSecret).token(token).tokenSecret(tokenSecret).build()

        val builder = OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)

        okHttpClient = if (b) {
            builder.addInterceptor(ResponseInterceptor()).addInterceptor(oauth1WooCommerce).build()
        } else {
            builder.addInterceptor(ResponseInterceptor()).build()
        }

        val gson = GsonBuilder().setLenient().disableHtmlEscaping().create()

        return Retrofit.Builder().baseUrl(DEFAULT_URL).client(okHttpClient!!).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder().body(ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), response.body()!!.bytes())).build()
    }
}

fun cancelAllApi() {
    okHttpClient?.dispatcher()?.cancelAll()
}