package com.wada811.rxmvvm.rxviewmodel.commands

import android.databinding.ObservableBoolean
import com.wada811.rxmvvm.rxviewmodel.extensions.addTo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxCommand<T>(canExecuteSource: Observable<Boolean> = Observable.just(true), canExecuteInitially: Boolean = true) : Disposable {
    private val executor: Subject<T> = PublishSubject.create<T>().toSerialized()
    val canExecute: ObservableBoolean = ObservableBoolean(canExecuteInitially)
    private val disposables = CompositeDisposable()
    
    init {
        canExecuteSource
            .distinctUntilChanged()
            .doAfterTerminate { dispose() }
            .subscribe({ canExecute.set(it) }, { executor.onError(it) }, { executor.onComplete() })
            .addTo(disposables)
    }
    
    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose() {
        if (!isDisposed) {
            disposables.dispose()
            canExecute.set(false)
            executor.onComplete()
        }
    }
    
    fun toObservable(): Observable<T> = executor
    
    fun execute(parameter: T) = executor.onNext(parameter)
    
    fun bindTrigger(observable: Observable<T>) {
        observable
            .filter { canExecute.get() }
            .doAfterTerminate { dispose() }
            .subscribe({ execute(it) }, { executor.onError(it) }, { executor.onComplete() })
            .addTo(disposables)
    }
}

fun <T> Observable<Boolean>.toRxCommand(canExecuteInitially: Boolean = true) = RxCommand<T>(this, canExecuteInitially)
