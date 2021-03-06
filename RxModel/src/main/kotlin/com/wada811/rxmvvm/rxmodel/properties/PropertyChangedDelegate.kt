package com.wada811.rxmvvm.rxmodel.properties

import com.wada811.rxmvvm.rxmodel.RxModelSubjects
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PropertyChangedDelegate<T>(initialValue: T) : ReadWriteProperty<NotifyPropertyChanged, T> {
    private var value: T = initialValue
    
    override operator fun getValue(thisRef: NotifyPropertyChanged, property: KProperty<*>): T = value
    
    override operator fun setValue(thisRef: NotifyPropertyChanged, property: KProperty<*>, value: T) {
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
