package com.wada811.sample.rxmvvm

import android.databinding.BindingAdapter
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.wada811.rxmvvm.rxviewmodel.RxViewModel

@BindingAdapter("itemLayout", "items")
fun <TBinding : ViewDataBinding, TViewModel : RxViewModel> RecyclerView.setAdapter(layoutId: Int, items: List<TViewModel>) {
    @Suppress("UNCHECKED_CAST")
    val adapter = this.adapter as RecyclerViewBindingAdapter<TBinding, TViewModel>?
    val position = this.verticalScrollbarPosition
    if (adapter == null) {
        this.adapter = RecyclerViewBindingAdapter<TBinding, TViewModel>(this.context, layoutId, items)
    } else {
        adapter.notifyDataSetChanged()
        this.verticalScrollbarPosition = position
    }
}
