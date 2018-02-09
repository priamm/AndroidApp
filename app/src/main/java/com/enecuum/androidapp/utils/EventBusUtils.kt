package com.enecuum.androidapp.utils

import org.greenrobot.eventbus.EventBus

/**
 * Created by oleg on 09.02.18.
 */
object EventBusUtils {
    fun register(item: Any) {
        if(!EventBus.getDefault().isRegistered(item))
            EventBus.getDefault().register(item)
    }

    fun unregister(item: Any) {
        if(EventBus.getDefault().isRegistered(item))
            EventBus.getDefault().unregister(item)
    }
}