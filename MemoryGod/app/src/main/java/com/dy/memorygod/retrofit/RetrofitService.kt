package com.dy.memorygod.retrofit

import com.dy.memorygod.retrofit.RetrofitUrl.SHARE_BASE_URL
import com.dy.memorygod.retrofit.RetrofitUrl.SHARE_LIST_URL
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET(SHARE_LIST_URL)
    fun getShareList(): Call<ShareListResponse>
    data class ShareListResponse(val data: List<String>?, val error: String?)

    @GET(SHARE_BASE_URL)
    fun getShareData(@Query("sheetName") sheetName: String): Call<ShareDataResponse>
    data class ShareDataResponse(val data: List<List<String>>?, val error: String?)

}