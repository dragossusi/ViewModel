package ro.dragossusi

import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel constructor() {

}

expect val ViewModel.coroutineScope: CoroutineScope