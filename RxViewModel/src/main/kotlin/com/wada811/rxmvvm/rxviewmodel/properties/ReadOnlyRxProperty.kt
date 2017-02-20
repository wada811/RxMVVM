package com.wada811.rxmvvm.rxviewmodel.properties

import android.databinding.ObservableField
import com.wada811.rxmvvm.rxviewmodel.extensions.addTo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*

open class ReadOnlyRxProperty<T>(source: Observable<T>, initialValue: T, mode: EnumSet<RxPropertyMode> = RxPropertyMode.DEFAULT) : ObservableField<T>(initialValue), Disposable {
    open var value: T = initialValue
        get() = super.get()
        protected set(value) {
            if (field != value) {
                field = value
                super.set(value)
                valueEmitter.onNext(value)
            } else if (!isDistinctUntilChanged) {
                valueEmitter.onNext(value)
            }
        }
    
    @Deprecated("For Data-Binding", ReplaceWith("value"), DeprecationLevel.HIDDEN)
    override fun get(): T = value
    
    @Deprecated("For Data-Binding", ReplaceWith("this.value = value"), DeprecationLevel.HIDDEN)
    override fun set(value: T) {
        throw UnsupportedOperationException("DO NOT USE ReadOnlyRxProperty for two-way binding, Please use RxProperty instead.")
    }
    
    protected val isDistinctUntilChanged: Boolean = mode.contains(RxPropertyMode.DISTINCT_UNTIL_CHANGED)
    protected val valueEmitter: Subject<T> =
        if (mode.contains(RxPropertyMode.RAISE_LATEST_VALUE_ON_SUBSCRIBE)) {
            BehaviorSubject.createDefault(initialValue).toSerialized()
        } else {
            PublishSubject.create<T>().toSerialized()
        }
    protected val disposables = CompositeDisposable()
    
    init {
        source
            .doAfterTerminate { dispose() }
            .subscribe({ value = it }, { valueEmitter.onError(it) }, { valueEmitter.onComplete() })
            .addTo(disposables)
    }
    
    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose() {
        if (!isDisposed) {
            disposables.dispose()
            valueEmitter.onComplete()
        }
    }
    
    fun toObservable(): Observable<T> = valueEmitter
}

fun <T> Observable<T>.toReadOnlyRxProperty(initialValue: T, mode: EnumSet<RxPropertyMode> = RxPropertyMode.DEFAULT)
    = ReadOnlyRxProperty(this, initialValue, mode)