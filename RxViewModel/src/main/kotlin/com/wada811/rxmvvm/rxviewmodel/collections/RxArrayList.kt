package com.wada811.rxmvvm.rxviewmodel.collections

import android.databinding.ObservableArrayList
import com.wada811.rxmvvm.rxmodel.collections.ObservableMutableList
import com.wada811.rxmvvm.rxmodel.collections.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

class RxArrayList<T>(source: Collection<T> = listOf(), internal var sourceDisposable: Disposable = Disposables.empty())
    : ObservableArrayList<T>(), ObservableMutableList<T>, Disposable by sourceDisposable {
    init {
        addAll(source)
    }
    
    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex)
    }
    
    override fun move(fromIndex: Int, toIndex: Int) {
        add(toIndex, removeAt(fromIndex))
    }
}

fun <T, R> ObservableMutableList<T>.toRxArrayList(converter: (T) -> R): RxArrayList<R> {
    val list = RxArrayList(this.map(converter))
    list.sourceDisposable = this.ObserveCollection<T>().observeOn(AndroidSchedulers.mainThread()).map(list, converter)
    return list
}
