package com.enecuumwallet.androidapp.ui.activity.testActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Flowable<? extends Throwable>, Flowable<?>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }

    @Override
    public Flowable<?> apply(final Flowable<? extends Throwable> attempts) {
        return attempts
                .flatMap(new Function<Throwable, Flowable<?>>() {
                    @Override
                    public Flowable<?> apply(final Throwable throwable) {
                        if (++retryCount < maxRetries) {
                            return Flowable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }
                        return Flowable.error(throwable);
                    }
                });
    }
}
