package ro.dragossusi

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual typealias KViewModel = androidx.lifecycle.ViewModel

actual val KViewModel.coroutineScope: CoroutineScope
    get() = this.viewModelScope
