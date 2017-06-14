package com.wada811.sample.rxmvvm

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.wada811.rxmvvm.rxviewmodel.extensions.addTo
import com.wada811.rxmvvm.rxviewmodel.functions.Action1
import com.wada811.rxmvvm.rxviewmodel.messages.RxMessenger
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {
    
    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityBindingAdapter(this, R.layout.activity_main, MainViewModel()).addTo(disposables)
        RxMessenger.observe(ToastAction::class.java).subscribe { it.invoke(this) }.addTo(disposables)
    }
    
    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
    
    class ToastAction(private val message: String) : Action1<Activity> {
        override fun invoke(activity: Activity) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    
    }
}
