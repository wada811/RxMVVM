package com.wada811.sample.rxmvvm

import com.wada811.rxmvvm.rxviewmodel.RxViewModel
import com.wada811.rxmvvm.rxviewmodel.properties.ReadOnlyRxProperty
import io.reactivex.subjects.PublishSubject

class MainItemViewModel(text: String) : RxViewModel() {
    val text = ReadOnlyRxProperty(PublishSubject.create(), text).asManaged()
}