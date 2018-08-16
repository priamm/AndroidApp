package com.enecuum.androidapp.websocketNew

import com.appunite.websocket.rx.`object`.messages.RxObjectEvent

import rx.Observable
import rx.Subscriber
import rx.functions.Func0
import rx.functions.Func1
import rx.internal.operators.OperatorMulticast
import rx.subjects.BehaviorSubject
import rx.subjects.Subject

object MoreObservables {

    fun <T> behaviorRefCount(): Observable.Transformer<in T, out T> {
        return Observable.Transformer { tObservable -> OperatorMulticast(tObservable, Func0<Subject<in T, out T>> { BehaviorSubject.create() }).refCount() }
    }

    fun <T> filterAndMap(clazz: Class<T>): Observable.Transformer<Any, T> {
        return Observable.Transformer { observable ->
            observable
                    .filter { o -> o != null && clazz.isInstance(o) }
                    .map { o -> o as T }
        }
    }

    fun throwableToIgnoreError(): Func1<Throwable, Any> {
        return Func1 { Any() }
    }

    fun ignoreNext(): Observable.Operator<Any, RxObjectEvent> {
        return Observable.Operator { subscriber ->
            object : Subscriber<RxObjectEvent>(subscriber) {
                override fun onCompleted() {
                    subscriber.onCompleted()
                }

                override fun onError(e: Throwable) {
                    subscriber.onError(e)
                }

                override fun onNext(rxObjectEvent: RxObjectEvent) {}
            }
        }
    }
}
