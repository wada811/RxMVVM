package com.wada811.rxmvvm.rxmodel.collections

import com.wada811.rxmvvm.rxmodel.RxModelSubjects
import com.wada811.rxmvvm.rxmodel.properties.PropertyChangedEventArgs
import io.reactivex.Observable

interface NotifyCollectionChanged<in T> {
    @Suppress("UNCHECKED_CAST")
    fun <T> ObserveCollection(): Observable<CollectionChangedEventArgs<T>> = RxModelSubjects.collectionChangedSubject
        .filter { it.thisRef == this }
        .map { it as CollectionChangedEventArgs<T> }
    
    fun notifyItemInserted(index: Int, item: T) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.Insert(this, index, item))
    }
    
    fun notifyItemRangeInserted(startIndex: Int, items: Collection<T>) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.InsertRange(this, startIndex, items))
    }
    
    fun notifyItemRemoved(index: Int, item: T) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.Remove(this, index, item))
    }
    
    fun notifyItemRangeRemoved(startIndex: Int, items: Collection<T>) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.RemoveRange(this, startIndex, items))
    }
    
    fun notifyItemReplaced(index: Int, itemPair: CollectionChangedEventArgs.ItemPair<T>) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.Replace(this, index, itemPair))
    }
    
    fun notifyItemRangeReplaced(startIndex: Int, itemPairs: Collection<CollectionChangedEventArgs.ItemPair<T>>) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.ReplaceRange(this, startIndex, itemPairs))
    }
    
    fun notifyItemChanged(index: Int, propertyChangedEventArgs: PropertyChangedEventArgs<*>) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.Change(this, index, propertyChangedEventArgs))
    }
    
    fun notifyItemRangeChanged(startIndex: Int, propertyChangedEventArgsList: Collection<PropertyChangedEventArgs<*>>) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.ChangeRange(this, startIndex, propertyChangedEventArgsList))
    }
    
    fun notifyItemMoved(fromIndex: Int, toIndex: Int, item: T) {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.Move(this, fromIndex, toIndex, item))
    }
    
    fun notifyReset() {
        RxModelSubjects.collectionChangedSubject.onNext(CollectionChangedEventArgs.Reset(this))
    }
}