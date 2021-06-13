package ro.dragossusi

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual typealias ViewModel = androidx.lifecycle.ViewModel

actual val ViewModel.coroutineScope: CoroutineScope
    get() = this.viewModelScope
