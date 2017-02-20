package com.wada811.rxmvvm.rxviewmodel.messages

import com.wada811.rxmvvm.rxviewmodel.functions.Action
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

object RxMessenger {
    private val messenger: Subject<Action> = PublishSubject.create<Action>().toSerialized()
    fun send(action: Action) = messenger.onNext(action)
    fun <T> observe(clazz: Class<T>): Observable<T> = messenger.ofType(clazz).observeOn(AndroidSchedulers.mainThread())
}