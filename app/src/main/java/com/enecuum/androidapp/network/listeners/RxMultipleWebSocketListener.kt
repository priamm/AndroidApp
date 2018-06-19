package com.enecuum.androidapp.network.listeners

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class RxMultipleWebSocketListener : WebSocketListener() {

    internal val listeners : MutableList<WebSocketListener> = mutableListOf()

    fun addListener(listener: WebSocketListener) {
        synchronized(this,{
            listeners.add(listener)
        })
    }

    fun removeListener(listener: WebSocketListener) {
        synchronized(this,{
            listeners.remove(listener)
        })
    }

    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        toAll { it.onOpen(webSocket, response) }
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        toAll { it.onFailure(webSocket, t, response) }
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        toAll { it.onClosing(webSocket, code, reason) }
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        toAll { it.onMessage(webSocket, text) }
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        toAll { it.onMessage(webSocket, bytes) }
    }

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
        toAll { it.onClosed(webSocket, code, reason) }
    }

    private inline fun toAll(f: (WebSocketListener) -> Unit) {
        synchronized(this,{
            listeners.forEach {
                f(it)
            }
        })
    }
}