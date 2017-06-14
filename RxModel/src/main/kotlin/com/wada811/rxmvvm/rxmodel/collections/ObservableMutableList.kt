package com.wada811.rxmvvm.rxmodel.collections

import io.reactivex.disposables.Disposable

interface ObservableMutableList<T> : NotifyCollection<T>, MutableList<T>, Disposable {
    fun removeRange(fromIndex: Int, toIndex: Int)
    fun move(fromIndex: Int, toIndex: Int)
}