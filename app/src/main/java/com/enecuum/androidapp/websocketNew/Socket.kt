
package com.enecuum.androidapp.websocketNew

import com.appunite.websocket.rx.RxMoreObservables
import com.appunite.websocket.rx.`object`.messages.RxObjectEvent
import com.appunite.websocket.rx.`object`.messages.RxObjectEventConn
import com.appunite.websocket.rx.`object`.messages.RxObjectEventConnected
import com.appunite.websocket.rx.`object`.messages.RxObjectEventDisconnected
import com.appunite.websocket.rx.`object`.messages.RxObjectEventMessage
import com.enecuum.androidapp.websocketNew.model.DataMessage
import com.enecuum.androidapp.websocketNew.model.PingMessage
import com.enecuum.androidapp.websocketNew.model.RegisterMessage
import com.enecuum.androidapp.websocketNew.model.RegisteredMessage


import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.functions.Func1
import rx.functions.Func2
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject

class Socket(socketConnection: SocketConnection, private val scheduler: Scheduler) {

    private val events: Observable<RxObjectEvent>
    private val connection: Observable<Any>
    private val connectedAndRegistered: BehaviorSubject<RxObjectEventConn>

    private val lock = Any()
    private var counter = 0

    init {
        val events = PublishSubject.create<RxObjectEvent>()
        connection = socketConnection.connection()
                .lift(OperatorDoOnNext(events))
                .lift(MoreObservables.ignoreNext())
                .compose(MoreObservables.behaviorRefCount())
        this.events = events


        val registeredMessage = events
                .compose(MoreObservables.filterAndMap(RxObjectEventMessage::class.java))
                .filter(FilterRegisterMessage())

        val disconnectedMessage = events
                .compose(MoreObservables.filterAndMap(RxObjectEventDisconnected::class.java))

        connectedAndRegistered = BehaviorSubject.create(null as RxObjectEventConn?)
        disconnectedMessage
                .map(Func1<RxObjectEventDisconnected, RxObjectEventConn> { null })
                .mergeWith(registeredMessage)
                .subscribe(connectedAndRegistered)

        // Register on connected
        val connectedMessage = events
                .compose(MoreObservables.filterAndMap(RxObjectEventConnected::class.java))
//                .lift(LoggingObservables.loggingLift<RxObjectEventConnected>(LOGGER, "ConnectedEvent"))

        connectedMessage
                .flatMap(Func1<RxObjectEventConnected, Observable<*>> { rxEventConn ->
                    RxMoreObservables.sendObjectMessage(rxEventConn.sender(), RegisterMessage("asdf"))
                            .toObservable()
                })
//                .lift(LoggingObservables.loggingOnlyErrorLift<Any>(LOGGER, "SendRegisterEvent"))
                .onErrorReturn(MoreObservables.throwableToIgnoreError())
                .subscribe()

        // Log events
        LOGGER.level = Level.ALL
        RxMoreObservables.logger.level = Level.ALL
        events
                .subscribe(LoggingObservables.logging(LOGGER, "Events"))
        connectedAndRegistered
                .subscribe(LoggingObservables.logging(LOGGER, "ConnectedAndRegistered"))
    }

    fun events(): Observable<RxObjectEvent> {
        return events
    }

    fun connectedAndRegistered(): Observable<RxObjectEventConn> {
        return connectedAndRegistered
    }

    fun connection(): Observable<Any> {
        return connection
    }

    fun sendPingWhenConnected() {
        Observable.combineLatest(
                Observable.interval(5, TimeUnit.SECONDS, scheduler),
                connectedAndRegistered
        ) { aLong, rxEventConn -> rxEventConn }
                .compose(isConnected)
                .flatMap { rxEventConn ->
                    RxMoreObservables.sendObjectMessage(rxEventConn.sender(), PingMessage("send_only_when_connected"))
                            .toObservable()
                }
                .subscribe()
    }

    fun sendPingEvery5seconds() {
        Observable.interval(5, TimeUnit.SECONDS, scheduler)
                .flatMap(Func1<Long, Observable<*>> {
                    connectedAndRegistered
                            .compose(isConnected)
                            .first()
                            .flatMap<Any> { rxEventConn ->
                                RxMoreObservables.sendObjectMessage(rxEventConn.sender(), PingMessage("be_sure_to_send"))
                                        .toObservable()
                            }
                })
                .subscribe()
    }

    fun nextId(): Observable<String> {
        return Observable.create { subscriber ->
            var current: Int = 0
            synchronized(lock) {
                current = counter
                counter += 1
            }
            subscriber.onNext(current.toString())
            subscriber.onCompleted()
        }
    }

    fun sendMessageOnceWhenConnected(createMessage: Func1<String, Observable<Any>>): Observable<DataMessage> {
        return connectedAndRegistered
                .compose(isConnected)
                .first()
                .flatMap<DataMessage> { rxEventConn -> requestData(rxEventConn, createMessage) }
    }

    private fun requestData(rxEventConn: RxObjectEventConn,
                            createMessage: Func1<String, Observable<Any>>): Observable<DataMessage> {
        return nextId()
                .flatMap<DataMessage> { messageId ->
                    val sendMessageObservable = createMessage.call(messageId)
                            .flatMap { s ->
                                RxMoreObservables.sendObjectMessage(rxEventConn.sender(), s)
                                        .toObservable()
                            }

                    val waitForResponseObservable = events
                            .compose(MoreObservables.filterAndMap(RxObjectEventMessage::class.java))
                            .compose(RxObjectEventMessage.filterAndMap<DataMessage>(DataMessage::class.java))
                            .filter(Func1<DataMessage, Boolean> { dataMessage -> dataMessage.id().equals(messageId) })
                            .first()
                            .timeout(5, TimeUnit.SECONDS, scheduler)
                    Observable.combineLatest<DataMessage, Any, DataMessage>(waitForResponseObservable, sendMessageObservable
                    ) { dataResponse, o -> dataResponse }
                }
    }


    private class FilterRegisterMessage : Func1<RxObjectEventMessage, Boolean> {
        override fun call(rxEvent: RxObjectEventMessage): Boolean? {
            return rxEvent.message<Any>() is RegisteredMessage
        }
    }

    companion object {
        val LOGGER = Logger.getLogger("Rx")

        private val isConnected: Observable.Transformer<RxObjectEventConn, RxObjectEventConn>
            get() = Observable.Transformer { rxEventConnObservable -> rxEventConnObservable.filter { rxEventConn -> rxEventConn != null } }
    }


}
