package ro.dragossusi

import kotlinx.coroutines.CoroutineScope

expect abstract class KViewModel() {
    protected open fun onCleared()
    internal fun clear()
}

expect val KViewModel.coroutineScope: CoroutineScope