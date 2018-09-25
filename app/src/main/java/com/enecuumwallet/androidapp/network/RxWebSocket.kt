package com.enecuumwallet.androidapp.network

import io.reactivex.Flowable
import okhttp3.Request
import okhttp3.WebSocket

interface RxWebSocket : WebSocket {

    fun observe(): Flowable<WebSocketEvent>

    companion object {
        fun createAutoManagedRxWebSocket(request: Request): RxWebSocket {
            return AutoManagedWebSocket(request)
        }

        fun createManualRxWebSocket(request: Request): ManualRxWebSocket {
            return RealManualRxWebSocket(request)
        }
    }
}