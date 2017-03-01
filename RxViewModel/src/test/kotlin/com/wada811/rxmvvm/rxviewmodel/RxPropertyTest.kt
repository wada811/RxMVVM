package com.wada811.rxmvvm.rxviewmodel

import com.wada811.rxmvvm.rxmodel.properties.NotifyPropertyChanged
import com.wada811.rxmvvm.rxmodel.properties.PropertyChangedDelegate
import com.wada811.rxmvvm.rxviewmodel.properties.toReadOnlyRxProperty
import org.junit.Test

class RxPropertyTest {
    class Person(name: String, age: Int) : NotifyPropertyChanged {
        var name: String by PropertyChangedDelegate(name)
            get
            private set
        val age: Int by PropertyChangedDelegate(age)
        fun transform(name: String) {
            this.name = name
        }
    }
    
    @Test
    fun testReadOnlyRxProperty() {
        val person1 = Person("美墨なぎさ", 14)
        val person2 = Person("雪城ほのか", 14)
        person1.ObserveProperty<String>("name").toReadOnlyRxProperty(person1.name).toObservable().subscribe(::println)
        person2.ObserveProperty<String>("name").toReadOnlyRxProperty(person2.name).toObservable().subscribe(::println)
        person1.transform("キュアブラック")
        person2.transform("キュアホワイト")
    }
}