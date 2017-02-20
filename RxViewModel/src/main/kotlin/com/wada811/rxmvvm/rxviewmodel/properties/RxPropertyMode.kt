package com.wada811.rxmvvm.rxviewmodel.properties

import java.util.*

enum class RxPropertyMode {
    /**
     * If next value is same as current, not set and not notify.
     */
    DISTINCT_UNTIL_CHANGED,
    /**
     * Sends notification on the instance created and subscribed.
     */
    RAISE_LATEST_VALUE_ON_SUBSCRIBE;
    
    internal companion object {
        internal val DEFAULT = EnumSet.allOf(RxPropertyMode::class.java)
    }
}