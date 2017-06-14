package com.wada811.sample.rxmvvm

import com.wada811.rxmvvm.rxmodel.collections.ObservableSynchronizedArrayList
import com.wada811.rxmvvm.rxviewmodel.RxViewModel
import com.wada811.rxmvvm.rxviewmodel.collections.RxArrayList
import com.wada811.rxmvvm.rxviewmodel.collections.toRxArrayList
import com.wada811.rxmvvm.rxviewmodel.commands.RxCommand
import com.wada811.rxmvvm.rxviewmodel.messages.RxMessenger
import com.wada811.rxmvvm.rxviewmodel.properties.RxProperty
import io.reactivex.Observable

class MainViewModel : RxViewModel() {
    val text: RxProperty<String> = RxProperty<String>(Observable.just("text"), "text").asManaged()
    val items: ObservableSynchronizedArrayList<String> = ObservableSynchronizedArrayList<String>().apply {
        addAll(Observable.range(1, 100).map { "Item $it" }.blockingIterable().toList())
    }
    val itemViewModels: RxArrayList<MainItemViewModel> = items.toRxArrayList { MainItemViewModel("ViewModel $it") }.asManaged()
    
    val clickCommand: RxCommand<Unit> = RxCommand<Unit>().asManaged()
    
    init {
        text.value = "aaa"
        clickCommand.toObservable().subscribe {
            RxMessenger.send(MainActivity.ToastAction(text.value))
        }
    }
    
}