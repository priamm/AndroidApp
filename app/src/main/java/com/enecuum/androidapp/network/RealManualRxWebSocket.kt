package com.enecuum.androidapp.network

import com.enecuum.androidapp.network.listeners.RxMultipleWebSocketListener
import com.enecuum.androidapp.network.listeners.RxWebSocketListener
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.ws.RealWebSocket
import okio.ByteString
import java.security.SecureRandom

class RealManualRxWebSocket(request: Request,
                            private val client: OkHttpClient = OkHttpClient()) : ManualRxWebSocket {
    private val baseListener = RxMultipleWebSocketListener()

    private val pingInterval = 10000

    private val webSocket = RealWebSocket(request, baseListener, SecureRandom(), pingInterval.toLong())

    override fun connect() {
        webSocket.connect(client)
    }

    override fun cancel() {
        webSocket.cancel()
    }

    override fun queueSize(): Long {
        return webSocket.queueSize()
    }

    override fun send(text: String?): Boolean {
        return webSocket.send(text)
    }

    override fun send(bytes: ByteString?): Boolean {
        return webSocket.send(bytes)
    }

    override fun close(code: Int, reason: String?): Boolean {
        return webSocket.close(code, reason)
    }

    override fun request(): Request {
        return webSocket.request()
    }

    override fun observe(): Flowable<WebSocketEvent> {
        val a = Flowable.create<WebSocketEvent>({ e ->
            val listener = RxWebSocketListener(e)
            e.setDisposable(object : Disposable {
                var disposed = false

                override fun isDisposed(): Boolean {
                    return disposed
                }

                override fun dispose() {
                    baseListener.removeListener(listener)
                    disposed = true
                }
            })
            baseListener.addListener(listener)
        }, BackpressureStrategy.BUFFER)
        return a
    }
}