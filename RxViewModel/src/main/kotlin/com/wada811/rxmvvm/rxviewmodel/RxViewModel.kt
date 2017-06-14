package com.wada811.rxmvvm.rxviewmodel

import com.wada811.rxmvvm.rxviewmodel.collections.RxArrayList
import com.wada811.rxmvvm.rxviewmodel.commands.RxCommand
import com.wada811.rxmvvm.rxviewmodel.properties.ReadOnlyRxProperty
import com.wada811.rxmvvm.rxviewmodel.properties.RxProperty
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class RxViewModel(private val disposables: CompositeDisposable = CompositeDisposable()) : Disposable by disposables {
    protected fun <T> RxProperty<T>.asManaged(): RxProperty<T> {
        disposables.add(this)
        return this
    }

    protected fun <T> ReadOnlyRxProperty<T>.asManaged(): ReadOnlyRxProperty<T> {
        disposables.add(this)
        return this
    }
    
    protected fun <T> RxCommand<T>.asManaged(): RxCommand<T> {
        disposables.add(this)
        return this
    }
    
    protected fun <T> RxArrayList<T>.asManaged(): RxArrayList<T> {
        disposables.add(this)
        return this
    }
    
    protected fun <T> T.asManaged() where T : Disposable {
        disposables.add(this)
    }
}