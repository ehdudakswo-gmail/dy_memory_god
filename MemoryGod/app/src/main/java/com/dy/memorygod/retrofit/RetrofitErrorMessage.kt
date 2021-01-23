package com.dy.memorygod.retrofit

object RetrofitErrorMessage {

    private const val RETROFIT_CLIENT = "RetrofitClient"
    private const val ON_RESPONSE = "onResponse"
    private const val ON_FAILURE = "onFailure"

    // common
    fun onResponseBodyNull(): String {
        return "$RETROFIT_CLIENT $ON_RESPONSE responseBody null"
    }

    fun onResponseException(ex: Exception): String {
        val error = ex.toString()
        return "$RETROFIT_CLIENT $ON_RESPONSE exception : $error"
    }

    fun onResponseFailed(responseCode: Int): String {
        return "$RETROFIT_CLIENT $ON_RESPONSE failed code : $responseCode"
    }

    fun onFailure(t: Throwable): String {
        val error = t.toString()
        return "$RETROFIT_CLIENT $ON_FAILURE : $error"
    }

    // custom
    fun onResponseError(error: String): String {
        return "$RETROFIT_CLIENT $ON_RESPONSE error : $error"
    }

    fun onResponseDataNull(): String {
        return "$RETROFIT_CLIENT $ON_RESPONSE data null"
    }

}