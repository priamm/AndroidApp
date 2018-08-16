
package com.enecuum.androidapp.websocketNew

import com.appunite.websocket.rx.`object`.messages.RxObjectEvent

import rx.Observable

interface SocketConnection {
    fun connection(): Observable<RxObjectEvent>
}
