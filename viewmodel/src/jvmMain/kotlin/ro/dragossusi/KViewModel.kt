package ro.dragossusi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import java.io.IOException
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

private const val JOB_KEY = "androidx.lifecycle.ViewModelCoroutineScope.JOB_KEY"

actual abstract class KViewModel actual constructor() {
    // Can't use ConcurrentHashMap, because it can lose values on old apis (see b/37042460)
    private val mBagOfTags: MutableMap<String, Any> = HashMap()

    @Volatile
    private var mCleared = false

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    protected open fun onCleared() {}

    fun clear() {
        mCleared = true
        // Since clear() is final, this method is still called on mock objects
        // and in those cases, mBagOfTags is null. It'll always be empty though
        // because setTagIfAbsent and getTag are not final so we can skip
        // clearing it
        synchronized(mBagOfTags) {
            mBagOfTags.values.forEach { value ->
                // see comment for the similar call in setTagIfAbsent
                closeWithRuntimeException(value)
            }
        }
        onCleared()
    }

    /**
     * Sets a tag associated with this viewmodel and a key.
     * If the given `newValue` is [Closeable],
     * it will be closed once [.clear].
     *
     *
     * If a value was already set for the given key, this call does nothing and
     * returns currently associated value, the given `newValue` would be ignored
     *
     *
     * If the ViewModel was already cleared then close() would be called on the returned object if
     * it implements [Closeable]. The same object may receive multiple close calls, so method
     * should be idempotent.
     */
    open fun <T> setTagIfAbsent(key: String, newValue: T): T {
        var previous: T?
        synchronized(mBagOfTags) {
            @Suppress("UNCHECKED_CAST")
            previous = mBagOfTags[key] as? T
            if (previous == null) {
                mBagOfTags.put(key, newValue as Any)
            }
        }
        val result: T = previous ?: newValue
        if (mCleared) {
            // It is possible that we'll call close() multiple times on the same object, but
            // Closeable interface requires close method to be idempotent:
            // "if the stream is already closed then invoking this method has no effect." (c)
            closeWithRuntimeException(result as Any)
        }
        return result
    }

    /**
     * Returns the tag associated with this viewmodel and the specified key.
     */
    open fun <T> getTag(key: String): T? {
        synchronized(mBagOfTags) {
            @Suppress("UNCHECKED_CAST")
            return mBagOfTags[key] as T?
        }
    }

    private fun closeWithRuntimeException(obj: Any) {
        if (obj is Closeable) {
            try {
                obj.close()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

}

actual val KViewModel.coroutineScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(
            JOB_KEY,
            CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}