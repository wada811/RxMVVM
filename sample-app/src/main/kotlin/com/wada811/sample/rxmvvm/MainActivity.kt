package com.wada811.sample.rxmvvm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wada811.rxmvvm.rxviewmodel.extensions.addTo
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {
    
    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityBindingAdapter(this, R.layout.activity_main, MainViewModel()).addTo(disposables)
    }
    
    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
