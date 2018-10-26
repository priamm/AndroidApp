package com.enecuumwallet.androidapp.network.api

import com.enecuumwallet.androidapp.network.api.response.OtpResponse
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Path

interface OtpApi  {
    @POST("getOtp/{publicKey}/{sign}")
    fun getOtpCode(@Path("publicKey") publicKey : String,
                   @Path("sign") sign : String) : Flowable<OtpResponse>
}