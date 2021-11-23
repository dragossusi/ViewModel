package ro.dragossusi

import kotlinx.coroutines.CoroutineScope

expect abstract class CommonViewModel() {

}

expect val CommonViewModel.coroutineScope: CoroutineScope