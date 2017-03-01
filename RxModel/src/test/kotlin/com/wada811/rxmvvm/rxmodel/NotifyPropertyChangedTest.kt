package com.wada811.rxmvvm.rxmodel

import com.wada811.rxmvvm.rxmodel.properties.NotifyPropertyChanged
import com.wada811.rxmvvm.rxmodel.properties.PropertyChangedDelegate
import org.junit.Test

class NotifyPropertyChangedTest {
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
    fun testObserveProperty() {
        val person1 = Person("美墨なぎさ", 14)
        val person2 = Person("雪城ほのか", 14)
        person1.ObserveProperty<String>("name").subscribe(::println)
        person2.ObserveProperty<String>("name").subscribe(::println)
        person1.transform("キュアブラック")
        person2.transform("キュアホワイト")
    }
}