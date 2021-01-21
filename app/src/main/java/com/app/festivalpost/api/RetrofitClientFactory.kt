package com.emegamart.lelys.network

import com.app.festivalpost.FestivalPost.Companion.okHttpClient
import com.app.festivalpost.utils.Constants.Config.DEFAULT_URL
import com.app.festivalpost.utils.Constants.Config.VERSION
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClientFactory {

    fun getRetroFitClient(): Retrofit {

        val builder = OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)

        okHttpClient =
            builder.addInterceptor(ResponseInterceptor()).build()


        val gson = GsonBuilder().setLenient().disableHtmlEscaping().create()

        return Retrofit.Builder().baseUrl(DEFAULT_URL+ VERSION).client(okHttpClient!!).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder().body(ResponseBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), response.body!!.bytes())).build()
    }
}

fun cancelAllApi() {
    //okHttpClient?.dispatcher()?.cancelAll()
}