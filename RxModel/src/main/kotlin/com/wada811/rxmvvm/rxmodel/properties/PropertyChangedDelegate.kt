package com.wada811.rxmvvm.rxmodel.properties

import com.wada811.rxmvvm.rxmodel.RxModelSubjects
import kotlin.reflect.KProperty

class PropertyChangedDelegate<T>(initialValue: T) {
    private var value: T = initialValue
    
    operator fun getValue(thisRef: NotifyPropertyChanged, property: KProperty<*>): T = value
    
    operator fun setValue(thisRef: NotifyPropertyChanged, property: KProperty<*>, value: T) {
        if (this.value != value) {
            this.value = value
            notifyPropertyChanged(thisRef, property, value)
        }
    }
    
    private fun notifyPropertyChanged(thisRef: NotifyPropertyChanged, property: KProperty<*>, value: T) {
        RxModelSubjects.propertyChangedSubject
            .onNext(PropertyChangedEventArgs(thisRef, property.name, value))
    }
}
