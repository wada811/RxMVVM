package com.wada811.rxmvvm.rxmodel.properties

import com.wada811.rxmvvm.rxmodel.EventArgs

internal class PropertyChangedEventArgs<out T>(val thisRef: NotifyPropertyChanged, val propertyName: String = "", val value: T) : EventArgs