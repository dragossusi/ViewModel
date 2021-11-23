package ro.dragossusi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

actual abstract class CommonViewModel actual constructor() {

    protected val context = Job() + Dispatchers.IO

    val scope = CoroutineScope(context)

}

actual val CommonViewModel.coroutineScope: CoroutineScope
    get() = scope