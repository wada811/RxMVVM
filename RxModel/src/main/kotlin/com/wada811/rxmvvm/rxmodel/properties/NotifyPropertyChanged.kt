package com.wada811.rxmvvm.rxmodel.properties

import com.wada811.rxmvvm.rxmodel.RxModelSubjects
import io.reactivex.Observable

interface NotifyPropertyChanged {
    @Suppress("UNCHECKED_CAST")
    fun <T> ObserveProperty(propertyName: String): Observable<T> = RxModelSubjects.propertyChangedSubject
        .filter { it.thisRef == this }
        .filter { it.propertyName == propertyName }
        .map { it.value as T }
}