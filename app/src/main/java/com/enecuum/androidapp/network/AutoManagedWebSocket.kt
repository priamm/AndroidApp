package com.enecuum.androidapp.network

import com.enecuum.androidapp.network.listeners.RxWebSocketListener
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.internal.ws.RealWebSocket
import okio.ByteString
import java.security.SecureRandom

class AutoManagedWebSocket(request: Request,
                           client: OkHttpClient = OkHttpClient()) : RxWebSocket {

    private lateinit var webSocket: WebSocket
    private val pingInterval = 10000

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

    private val flowable: Flowable<WebSocketEvent> = Flowable.create({ e ->
        val listener = RxWebSocketListener(e)
        e.setDisposable(object : Disposable {
            var disposed = false

            val code = 1000
            val reason = "finished"

            override fun isDisposed(): Boolean {
                return disposed
            }

            override fun dispose() {
                disposed = webSocket.close(code, reason)
                e.onNext(WebSocketEvent.ClosedEvent(webSocket, code, reason))
            }
        })
        val real = RealWebSocket(request, listener, SecureRandom(), pingInterval.toLong())
        real.connect(client)
        webSocket = real
    }, BackpressureStrategy.BUFFER)

    override fun observe(): Flowable<WebSocketEvent> {
        return flowable
    }
}