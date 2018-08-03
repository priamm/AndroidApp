package com.enecuum.androidapp.presentation.presenter.balance

import com.segment.jsonrpc.JsonRPC

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RPCService {
//    {"jsonrpc":"2.0","params":{"address":"B026nJ7HB2nPtPjxkTrH2V5PzhgsWWab1gpi29oLsfiTKv"},"method":"enq_getBalance","id":1}
//    {"result":0,"jsonrpc":"2.0","id":1}


    @JsonRPC("2.0")
    @POST("/rpc")
    fun getBalance(@Body args: Params): Call<JsonRPCResponse>
}
