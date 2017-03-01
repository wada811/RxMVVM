package com.wada811.rxmvvm.rxmodel

import com.wada811.rxmvvm.rxmodel.properties.PropertyChangedEventArgs
import io.reactivex.subjects.PublishSubject

internal object RxModelSubjects {
    internal val propertyChangedSubject: PublishSubject<PropertyChangedEventArgs<*>> = PublishSubject.create<PropertyChangedEventArgs<*>>()
}
