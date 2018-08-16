package com.enecuum.androidapp.websocketNew

import com.appunite.websocket.rx.`object`.RxObjectWebSockets
import com.appunite.websocket.rx.`object`.messages.RxObjectEvent
import rx.Observable
import rx.Scheduler
import rx.functions.Func1
import java.util.concurrent.TimeUnit

class SocketConnectionImpl(private val sockets: RxObjectWebSockets, private val scheduler: Scheduler) : SocketConnection {

    override fun connection(): Observable<RxObjectEvent> {
        return sockets.webSocketObservable()
                .retryWhen(repeatDuration(1, TimeUnit.SECONDS))
    }

    private fun repeatDuration(delay: Long,
                               timeUnit: TimeUnit): Func1<Observable<out Throwable>, Observable<*>> {
        return Func1 { attemps ->
            attemps
                    .flatMap<Any> { Observable.timer(delay, timeUnit, scheduler) }
        }
    }
}
