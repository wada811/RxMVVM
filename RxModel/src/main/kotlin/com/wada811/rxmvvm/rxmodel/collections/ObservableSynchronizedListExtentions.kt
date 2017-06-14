package com.wada811.rxmvvm.rxmodel.collections

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun <T, R> Observable<CollectionChangedEventArgs<T>>.map(
    target: ObservableMutableList<R>,
    converter: (T) -> R): Disposable {
    return this.subscribe {
        when (it.action) {
            CollectionChangedEventArgs.CollectionChangedEventAction.Insert -> {
                val e = it as CollectionChangedEventArgs.Insert<T>
                target.add(e.index, converter(e.item))
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.InsertRange -> {
                val e = it as CollectionChangedEventArgs.InsertRange<T>
                target.addAll(e.startIndex, e.items.map(converter))
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.Remove -> {
                val e = it as CollectionChangedEventArgs.Remove<T>
                target.removeAt(e.index)
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.RemoveRange -> {
                val e = it as CollectionChangedEventArgs.RemoveRange<T>
                target.removeRange(e.startIndex, e.startIndex + e.items.size)
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.Replace -> {
                val e = it as CollectionChangedEventArgs.Replace<T>
                target[e.index] = converter(e.itemPair.newItem)
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.ReplaceRange -> {
                val e = it as CollectionChangedEventArgs.ReplaceRange<T>
                e.itemPairs.forEachIndexed { index, itemPair -> target[e.startIndex + index] = converter(itemPair.newItem) }
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.Change -> {
                val e = it as CollectionChangedEventArgs.Change<T>
                val item = @Suppress("UNCHECKED_CAST") converter(e.propertyChangedEventArgs.thisRef as T)
                target[e.index] = item
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.ChangeRange -> {
                val e = it as CollectionChangedEventArgs.ChangeRange<T>
                val items = e.propertyChangedEventArgsList.map { @Suppress("UNCHECKED_CAST") converter(it.thisRef as T) }
                items.forEachIndexed { index, item -> target[e.startIndex + index] = item }
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.Move -> {
                val e = it as CollectionChangedEventArgs.Move<T>
                target.move(e.fromIndex, e.toIndex)
            }
            CollectionChangedEventArgs.CollectionChangedEventAction.Reset -> {
                target.clear()
            }
        }
    }
}
    