package com.wada811.sample.rxmvvm

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import com.wada811.rxmvvm.rxviewmodel.RxViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class ActivityBindingAdapter<out TBinding, TViewModel>(activity: Activity, layoutId: Int, viewModel: TViewModel) : Disposable
where TBinding : ViewDataBinding, TViewModel : RxViewModel {
    protected val binding: TBinding = DataBindingUtil.setContentView<TBinding>(activity, layoutId)
    private val disposables = CompositeDisposable()
    
    init {
        binding.setVariable(BR.viewModel, viewModel)
        disposables.add(viewModel)
    }
    
    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose() {
        if (!isDisposed) {
            disposables.dispose()
            binding.unbind()
        }
    }
}
