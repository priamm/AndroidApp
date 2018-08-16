
package com.enecuum.androidapp.websocketNew

import rx.Observable.Operator
import rx.Observer
import rx.Subscriber

class OperatorDoOnNext<T>(private val doOnNextObserver: Observer<in T>) : Operator<T, T> {

    override fun call(observer: Subscriber<in T>): Subscriber<in T> {
        return object : Subscriber<T>(observer) {

            override fun onCompleted() {}

            override fun onError(e: Throwable) {}

            override fun onNext(value: T) {
                doOnNextObserver.onNext(value)
            }
        }
    }
}