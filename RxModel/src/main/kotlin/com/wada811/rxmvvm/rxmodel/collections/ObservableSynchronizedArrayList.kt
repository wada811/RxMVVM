package com.wada811.rxmvvm.rxmodel.collections

import com.googlecode.concurentlocks.ReentrantReadWriteUpdateLock
import com.wada811.rxmvvm.rxmodel.properties.NotifyPropertyChanged
import com.wada811.rxmvvm.rxmodel.properties.PropertyChangedEventArgs
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.util.*

class ObservableSynchronizedArrayList<T>(source: Collection<T> = listOf(), private var sourceDisposable: Disposable = Disposables.empty()) : ObservableMutableList<T>, Disposable by sourceDisposable {
    private val list = ArrayList(source)
    private val disposableList = PropertyChangedDisposableList(source, { index, propertyChangedEventArgs -> notifyItemChanged(index, propertyChangedEventArgs) })
    private val lock = ReentrantReadWriteUpdateLock()
    
    override val size: Int
        get() = lock.readLock { list.size }
    
    override fun add(element: T): Boolean {
        var result = false
        lock.updateLock({
            list.lastIndex
        }, {
            index ->
            result = list.add(element)
            if (result) {
                disposableList.add(index, element)
            }
        }, {
            index ->
            if (result) {
                notifyItemInserted(index, element)
            }
        })
        return result
    }
    
    override fun add(index: Int, element: T) {
        lock.writeLock({
            list.add(index, element)
            disposableList.add(index, element)
        }, {
            notifyItemInserted(index, element)
        })
    }
    
    override fun addAll(elements: Collection<T>): Boolean {
        var result = false
        lock.updateLock({
            Math.max(list.lastIndex, 0)
        }, {
            index ->
            result = list.addAll(elements)
            if (result) {
                disposableList.addAll(index, elements)
            }
        }, {
            index ->
            if (result) {
                notifyItemRangeInserted(index, elements)
            }
        })
        return result
    }
    
    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        var result = false
        lock.writeLock({
            result = list.addAll(index, elements)
            if (result) {
                disposableList.addAll(index, elements)
            }
        }, {
            if (result) {
                notifyItemRangeInserted(index, elements)
            }
        })
        return result
    }
    
    override fun clear() {
        lock.updateLock({
            list.size
        }, {
            size ->
            if (size != 0) {
                list.clear()
                disposableList.clear()
            }
        }, {
            size ->
            if (size != 0) {
                notifyReset()
            }
        })
    }
    
    override fun contains(element: T): Boolean = lock.readLock { list.contains(element) }
    
    override fun containsAll(elements: Collection<T>): Boolean = lock.readLock { list.containsAll(elements) }
    
    override fun get(index: Int): T = lock.readLock { list[index] }
    
    override fun indexOf(element: T): Int = lock.readLock { list.indexOf(element) }
    
    override fun isEmpty(): Boolean = lock.readLock { list.isEmpty() }
    
    override fun iterator(): MutableIterator<T> = lock.readLock { list.iterator() }
    
    override fun lastIndexOf(element: T): Int = lock.readLock { list.lastIndexOf(element) }
    
    override fun listIterator(): MutableListIterator<T> = lock.readLock { list.listIterator() }
    
    override fun listIterator(index: Int): MutableListIterator<T> = lock.readLock { list.listIterator(index) }
    
    override fun remove(element: T): Boolean {
        var result = false
        lock.updateLock({
            list.indexOf(element)
        }, {
            index ->
            result = list.remove(element)
            if (result) {
                disposableList.removeAt(index)
            }
        }, {
            index ->
            if (result) {
                notifyItemRemoved(index, element)
            }
        })
        return result
    }
    
    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        lock.updateLock({
            elements.map { element -> Pair(list.indexOf(element), element) }
        }, { pairs ->
            result = list.removeAll(elements)
            if (result) {
                pairs.forEach {
                    disposableList.removeAt(it.first)
                }
            }
        }, { pairs ->
            if (result) {
                pairs.forEach {
                    notifyItemRemoved(it.first, it.second)
                }
            }
        })
        return result
    }
    
    override fun removeAt(index: Int): T {
        return lock.updateLock({
            list[index]
        }, {
            item ->
            list.removeAt(index)
            disposableList.removeAt(index)
        }, {
            item ->
            notifyItemRemoved(index, item)
        })
    }
    
    override fun removeRange(fromIndex: Int, toIndex: Int) {
        lock.updateLock({
            ArrayList(list.subList(fromIndex, toIndex))
        }, {
            items ->
            list.subList(fromIndex, toIndex).clear()
            disposableList.removeRange(fromIndex, toIndex)
        }, {
            items ->
            notifyItemRangeRemoved(fromIndex, items)
        })
    }
    
    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        lock.updateLock({
            list.filter { !elements.contains(it) }.mapIndexed { index, item -> Pair(index, item) }
        }, {
            pairs ->
            result = list.retainAll(elements)
            if (result) {
                pairs.forEach {
                    disposableList.removeAt(it.first)
                }
            }
        }, {
            pairs ->
            if (result) {
                pairs.forEach {
                    notifyItemRemoved(it.first, it.second)
                }
            }
        })
        return true
    }
    
    override fun set(index: Int, element: T): T {
        return lock.updateLock({
            list[index]
        }, {
            oldItem ->
            list[index] = element
            disposableList.set(index, element)
        }, {
            oldItem ->
            notifyItemReplaced(index, CollectionChangedEventArgs.ItemPair(oldItem, element))
        })
    }
    
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = lock.readLock { list.subList(fromIndex, toIndex) }
    
    override fun move(fromIndex: Int, toIndex: Int) {
        lock.updateLock({
            list[fromIndex]
        }, {
            item ->
            list.removeAt(fromIndex)
            list.add(toIndex, item)
            disposableList.move(fromIndex, toIndex)
        }, {
            item ->
            notifyItemMoved(fromIndex, toIndex, item)
        })
    }
    
    private class PropertyChangedDisposableList<T>(source: Collection<T>, private val notifyItemChanged: (Int, PropertyChangedEventArgs<*>) -> Unit) {
        private val disposables: MutableList<Disposable>
            = source.mapIndexed { index, element -> subscribeItem(index, element) }.toMutableList()
        
        private fun subscribeItem(index: Int, element: T): Disposable {
            return if (element is NotifyPropertyChanged) {
                element.ObserveProperty().subscribe({
                    notifyItemChanged(index, it)
                })
            } else {
                Disposables.disposed()
            }
        }
        
        fun add(index: Int, element: T) {
            disposables.add(index, subscribeItem(index, element))
        }
        
        fun addAll(startIndex: Int, elements: Collection<T>) {
            disposables.addAll(elements.mapIndexed { index, element -> subscribeItem(startIndex + index, element) })
        }
        
        fun clear() {
            disposables.forEach(Disposable::dispose)
            disposables.clear()
        }
        
        fun removeAt(index: Int) {
            disposables[index].dispose()
            disposables.removeAt(index)
        }
        
        fun removeRange(fromIndex: Int, toIndex: Int) {
            val subList = disposables.subList(fromIndex, toIndex)
            subList.forEach(Disposable::dispose)
            subList.clear()
        }
        
        fun set(index: Int, element: T) {
            disposables[index].dispose()
            disposables[index] = subscribeItem(index, element)
        }
        
        fun move(fromIndex: Int, toIndex: Int) {
            disposables.add(toIndex, disposables.removeAt(fromIndex))
        }
    }
    
    fun <R> toObservableSynchronizedList(converter: (T) -> R): ObservableSynchronizedArrayList<R> {
        return lock.readLock {
            val result = ObservableSynchronizedArrayList(map(converter))
            result.sourceDisposable = this.ObserveCollection<T>().map(result, converter)
            return@readLock result
        }
    }
}
