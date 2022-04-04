package ro.dragossusi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import java.lang.reflect.Method

actual abstract class KViewModel : ViewModel() {

    private val clearMethod = ViewModel::class.java.getDeclaredMethod("clear")
        .apply { isAccessible = true }

    actual override fun onCleared() {
        super.onCleared()
    }

    internal actual fun clear() {
        clearMethod.invoke(this)
    }
}

actual val KViewModel.coroutineScope: CoroutineScope
    get() = this.viewModelScope
