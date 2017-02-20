package com.wada811.rxmvvm.rxviewmodel.properties

import io.reactivex.Observable
import java.util.*

class RxProperty<T>(source: Observable<T>, initialValue: T, mode: EnumSet<RxPropertyMode> = RxPropertyMode.DEFAULT) : ReadOnlyRxProperty<T>(source, initialValue, mode) {
    override var value: T = initialValue
        get
        set(value) {
            if (field != value) {
                field = value
                super.value = value
                valueEmitter.onNext(value)
            } else if (!isDistinctUntilChanged) {
                valueEmitter.onNext(value)
            }
        }
    
    @Deprecated("For Data-Binding", ReplaceWith("value"), DeprecationLevel.HIDDEN)
    override fun get(): T = value
    
    @Deprecated("For Data-Binding", ReplaceWith("this.value = value"), DeprecationLevel.HIDDEN)
    override fun set(value: T) {
        this.value = value
    }
}

fun <T> Observable<T>.toRxProperty(initialValue: T, mode: EnumSet<RxPropertyMode> = RxPropertyMode.DEFAULT) = RxProperty(this, initialValue, mode)
