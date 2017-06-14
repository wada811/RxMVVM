package com.wada811.rxmvvm.rxmodel.collections

import com.wada811.rxmvvm.rxmodel.EventArgs
import com.wada811.rxmvvm.rxmodel.properties.PropertyChangedEventArgs
import java.io.Serializable

@Suppress("unused")
sealed class CollectionChangedEventArgs<T>(val thisRef: NotifyCollectionChanged<T>, val action: CollectionChangedEventAction) : EventArgs {
    enum class CollectionChangedEventAction {
        Insert,
        InsertRange,
        Remove,
        RemoveRange,
        Replace,
        ReplaceRange,
        Change,
        ChangeRange,
        Move,
        Reset
    }
    
    /**
     * For RecyclerView.Adapter#notifyItemInserted(int position)
     *
     * @param thisRef
     * @param index
     * @param item
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemInserted(int)">RecyclerView.Adapter#notifyItemInserted(int position)</a>
     */
    class Insert<T>(thisRef: NotifyCollectionChanged<T>, val index: Int, val item: T)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.Insert)
    
    /**
     * For RecyclerView.Adapter#notifyItemRangeInserted(int positionStart, int itemCount)
     *
     * @param thisRef
     * @param startIndex
     * @param items
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemRangeInserted(int, int)">RecyclerView.Adapter#notifyItemRangeInserted(int positionStart, int itemCount)</a>
     */
    class InsertRange<T>(thisRef: NotifyCollectionChanged<T>, val startIndex: Int, val items: Collection<T>)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.InsertRange)
    
    /**
     * For RecyclerView.Adapter#notifyItemRemoved(int position)
     *
     * @param thisRef
     * @param index
     * @param item
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemRemoved(int)">RecyclerView.Adapter#notifyItemRemoved(int position)</a>
     */
    class Remove<T>(thisRef: NotifyCollectionChanged<T>, val index: Int, val item: T)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.Remove)
    
    /**
     * For RecyclerView.Adapter#notifyItemRangeRemoved(int positionStart, int itemCount)
     *
     * @param thisRef
     * @param startIndex
     * @param items
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemRangeRemoved(int, int)">RecyclerView.Adapter#notifyItemRangeRemoved(int positionStart, int itemCount)</a>
     */
    class RemoveRange<T>(thisRef: NotifyCollectionChanged<T>, val startIndex: Int, val items: Collection<T>)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.RemoveRange)
    
    /**
     * For Replace, ReplaceRange, Change and ChangeRange
     */
    data class ItemPair<out T>(val oldItem: T, val newItem: T) : Serializable {
        override fun toString(): String = "($oldItem, $newItem)"
    }
    
    /**
     * For RecyclerView.Adapter#notifyItemChanged(int position)
     *
     * @param thisRef
     * @param index
     * @param itemPair
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemChanged(int)">RecyclerView.Adapter#notifyItemChanged(int position)</a>
     */
    class Replace<T>(thisRef: NotifyCollectionChanged<T>, val index: Int, val itemPair: ItemPair<T>)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.Replace)
    
    /**
     * For RecyclerView.Adapter#notifyItemRangeChanged(int positionStart, int itemCount)
     *
     * @param thisRef
     * @param startIndex
     * @param itemPairs
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemRangeChanged(int, int)">RecyclerView.Adapter#notifyItemRangeChanged(int positionStart, int itemCount)</a>
     */
    class ReplaceRange<T>(thisRef: NotifyCollectionChanged<T>, val startIndex: Int, val itemPairs: Collection<ItemPair<T>>)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.ReplaceRange)
    
    /**
     * For RecyclerView.Adapter#notifyItemChanged(int position, Object payload)
     *
     * @param thisRef
     * @param index
     * @param propertyChangedEventArgs
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemChanged(int, java.lang.Object)">RecyclerView.Adapter#notifyItemChanged(int position, Object payload)</a>
     */
    class Change<T>(thisRef: NotifyCollectionChanged<T>, val index: Int, val propertyChangedEventArgs: PropertyChangedEventArgs<*>)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.Change)
    
    /**
     * For RecyclerView.Adapter#notifyItemRangeChanged(int positionStart, int itemCount, Object payload)
     *
     * @param thisRef
     * @param startIndex
     * @param propertyChangedEventArgsList
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemRangeChanged(int, int, java.lang.Object)">RecyclerView.Adapter#notifyItemRangeChanged(int positionStart, int itemCount, Object payload)</a>
     */
    class ChangeRange<T>(thisRef: NotifyCollectionChanged<T>, val startIndex: Int, val propertyChangedEventArgsList: Collection<PropertyChangedEventArgs<*>>)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.ChangeRange)
    
    /**
     * For RecyclerView.Adapter#notifyItemMoved(int fromPosition, int toPosition)
     *
     * @param thisRef
     * @param fromIndex
     * @param toIndex
     * @param item
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyItemMoved(int, int)">RecyclerView.Adapter#notifyItemMoved(int fromPosition, int toPosition)</a>
     */
    class Move<T>(thisRef: NotifyCollectionChanged<T>, val fromIndex: Int, val toIndex: Int, val item: T)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.Move)
    
    /**
     * For RecyclerView.Adapter#notifyDataSetChanged()
     *
     * @param thisRef
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyDataSetChanged()">RecyclerView.AdapternotifyDataSetChanged()</a>
     */
    class Reset<T>(thisRef: NotifyCollectionChanged<T>)
        : CollectionChangedEventArgs<T>(thisRef, CollectionChangedEventAction.Reset)
    
    
}