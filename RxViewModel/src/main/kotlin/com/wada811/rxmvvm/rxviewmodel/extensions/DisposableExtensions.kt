package com.wada811.rxmvvm.rxviewmodel.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun <T> T.addTo(compositeDisposable: CompositeDisposable): T where T : Disposable {
    compositeDisposable.add(this)
    return this
}