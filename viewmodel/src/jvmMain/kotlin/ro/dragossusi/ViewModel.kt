package ro.dragossusi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

actual abstract class ViewModel {

    protected val context = Job() + Dispatchers.IO

    val scope = CoroutineScope(context)

}

actual val ViewModel.coroutineScope: CoroutineScope
    get() = scope