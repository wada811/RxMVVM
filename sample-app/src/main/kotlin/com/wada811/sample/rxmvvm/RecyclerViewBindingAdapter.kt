package com.wada811.sample.rxmvvm

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wada811.rxmvvm.rxviewmodel.RxViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class RecyclerViewBindingAdapter<TBinding, TViewModel>(
    context: Context,
    private val layoutId: Int,
    private val items: List<TViewModel>,
    private val disposables: Disposable = CompositeDisposable()
) : RecyclerView.Adapter<RecyclerViewBindingAdapter.BindingViewHolder<TBinding>>(), Disposable by disposables
where TBinding : ViewDataBinding, TViewModel : RxViewModel {
    
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    
    override fun getItemCount(): Int = items.size
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<TBinding> {
        val binding = DataBindingUtil.inflate<TBinding>(layoutInflater, layoutId, parent, false)
        return BindingViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: RecyclerViewBindingAdapter.BindingViewHolder<TBinding>, position: Int) {
        holder.binding.setVariable(BR.viewModel, items[position])
        holder.binding.executePendingBindings()
    }
    
    override fun onViewRecycled(holder: BindingViewHolder<TBinding>) {
        super.onViewRecycled(holder)
        holder.binding.unbind()
    }
    
    class BindingViewHolder<out TBinding>(val binding: TBinding) : RecyclerView.ViewHolder(binding.root) where TBinding : ViewDataBinding
}