package com.wada811.rxmvvm.rxviewmodel.functions

interface Function
interface Function0<out R> : Function {
    @Throws(Exception::class)
    fun invoke(): R
}

interface Function1<in T1, out R> : Function {
    @Throws(Exception::class)
    fun invoke(t1: T1): R
}

interface Function2<in T1, in T2, out R> : Function {
    @Throws(Exception::class)
    fun invoke(t1: T1, t2: T2): R
}

interface Function3<in T1, in T2, in T3, out R> : Function {
    @Throws(Exception::class)
    fun invoke(t1: T1, t2: T2, t3: T3): R
}

interface Function4<in T1, in T2, in T3, in T4, out R> : Function {
    @Throws(Exception::class)
    fun invoke(t1: T1, t2: T2, t3: T3, t4: T4): R
}

interface Action : Function
interface Action0 : Action, Function0<Unit>
interface Action1<in T1> : Action, Function1<T1, Unit>
interface Action2<in T1, in T2> : Action, Function2<T1, T2, Unit>
interface Action3<in T1, in T2, in T3> : Action, Function3<T1, T2, T3, Unit>
interface Action4<in T1, in T2, in T3, in T4> : Action, Function4<T1, T2, T3, T4, Unit>
