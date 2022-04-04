package ro.dragossusi

import kotlinx.coroutines.CoroutineScope

expect abstract class KViewModel() {

//    protected open fun onCleared()
//    fun clear()

}

expect val KViewModel.coroutineScope: CoroutineScope