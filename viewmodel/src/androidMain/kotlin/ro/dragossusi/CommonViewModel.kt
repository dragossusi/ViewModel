package ro.dragossusi

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual typealias CommonViewModel = androidx.lifecycle.ViewModel

actual val CommonViewModel.coroutineScope: CoroutineScope
    get() = this.viewModelScope
