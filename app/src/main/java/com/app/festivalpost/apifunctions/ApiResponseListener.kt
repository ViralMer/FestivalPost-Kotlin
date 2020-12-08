package com.app.festivalpost.apifunctions

interface ApiResponseListener {
    fun isConnected(requestService: String?, isConnected: Boolean)
    fun onSuccessResponse(requestService: String?, responseString: String?, responseCode: Int)
    fun onErrorResponse(requestService: String?, responseString: String?, responseCode: Int)

}