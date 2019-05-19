package me.tankery.justnote.utils
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.toLiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Helper method for testing LiveData objects, from
 * https://github.com/googlesamples/android-architecture-components.
 *
 * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
 * Once we got a notification via onChanged, we stop observing.
 */
@Throws(InterruptedException::class)
fun <T> LiveData<T>.getValueBlocking(): T {
    val data = arrayOfNulls<Any>(1)
    val latch = CountDownLatch(1)
    observeForever { o ->
        data[0] = o
        latch.countDown()
    }
    latch.await(2, TimeUnit.SECONDS)

    @Suppress("UNCHECKED_CAST")
    return data[0] as T
}

fun <T> DataSource.Factory<Int, T>.getValueBlocking(): List<T> =
        toLiveData(20).getValueBlocking()

fun <T, R> DataSource.Factory<Int, T>.mapValueBlocking(mapping: T.() -> R): List<R> =
        getValueBlocking().map(mapping)

fun <T> DataSource.Factory<Int, T>.findValueBlocking(finding: T.() -> Boolean): T? =
        getValueBlocking().find(finding)
