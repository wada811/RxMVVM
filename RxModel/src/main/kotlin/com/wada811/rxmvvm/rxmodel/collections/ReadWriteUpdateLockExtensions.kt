package com.wada811.rxmvvm.rxmodel.collections

import com.googlecode.concurentlocks.ReadWriteUpdateLock

fun <TResult> ReadWriteUpdateLock.readLock(action: () -> TResult): TResult {
    this.readLock().lock()
    try {
        return action()
    } finally {
        this.readLock().unlock()
    }
}

fun ReadWriteUpdateLock.writeLock(writeAction: () -> Unit, readAfterWriteAction: () -> Unit) {
    this.writeLock().lock()
    try {
        writeAction()
    } finally {
        this.writeLock().unlock()
    }
    this.readLock().lock()
    try {
        readAfterWriteAction()
    } finally {
        this.readLock().unlock()
    }
}

fun <TResult> ReadWriteUpdateLock.updateLock(readBeforeWriteAction: () -> TResult, writeAction: (TResult) -> Unit, readAfterWriteAction: (TResult) -> Unit): TResult {
    this.updateLock().lock()
    try {
        val readActionResult = readBeforeWriteAction()
        this.writeLock().lock()
        try {
            writeAction(readActionResult)
        } finally {
            this.writeLock().unlock()
        }
        readAfterWriteAction(readActionResult)
        return readActionResult
    } finally {
        this.updateLock().unlock()
    }
}
