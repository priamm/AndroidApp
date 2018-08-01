package com.enecuum.androidapp.presentation.presenter.balance

import com.segment.jsonrpc.JsonRPC

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MultiplicationService {
    @JsonRPC("Arith.Multiply")
    @POST("/rpc")
    fun multiply(@Body args: MultiplicationArgs): Call<Int>
}
