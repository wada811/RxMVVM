package com.wada811.sample.rxmvvm

import com.wada811.rxmvvm.rxmodel.collections.ObservableSynchronizedArrayList
import com.wada811.rxmvvm.rxviewmodel.RxViewModel
import com.wada811.rxmvvm.rxviewmodel.collections.RxArrayList
import com.wada811.rxmvvm.rxviewmodel.collections.toRxArrayList
import io.reactivex.Observable

class MainViewModel : RxViewModel() {
    val items: ObservableSynchronizedArrayList<String> = ObservableSynchronizedArrayList<String>().apply {
        addAll(Observable.range(1, 100).map { "Item $it" }.blockingIterable().toList())
    }
    val itemViewModels: RxArrayList<MainItemViewModel> = items.toRxArrayList { MainItemViewModel("ViewModel $it") }.asManaged()
}