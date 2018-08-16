package com.enecuum.androidapp.websocketNew

import java.util.logging.Level
import java.util.logging.Logger

import rx.Observable
import rx.Observer

object LoggingObservables {

    fun logging(logger: Logger, tag: String): Observer<Any> {
        return object : Observer<Any> {
            override fun onCompleted() {
                logger.log(Level.INFO, "$tag - onCompleted")
            }

            override fun onError(e: Throwable) {
                logger.log(Level.SEVERE, "$tag - onError", e)
            }

            override fun onNext(o: Any?) {
                logger.log(Level.INFO, "$tag - onNext: {0}", o?.toString() ?: "null")
            }
        }
    }

    fun loggingOnlyError(logger: Logger, tag: String): Observer<Any> {
        return object : Observer<Any> {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                logger.log(Level.SEVERE, "$tag - onError", e)
            }

            override fun onNext(o: Any) {}
        }
    }
//
//    fun <T> loggingLift(logger: Logger, tag: String): Observable.Operator<T, T> {
//        return OperatorDoOnEach()
//    }
//
//    fun <T> loggingOnlyErrorLift(logger: Logger, tag: String): Observable.Operator<T, T> {
//        return OperatorDoOnEach(loggingOnlyError(logger, tag))
//    }
}
