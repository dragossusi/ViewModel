package ro.dragossusi

actual class KViewModelStore {
    private val mMap: HashMap<String, KViewModel> = HashMap<String, KViewModel>()

    fun put(key: String, viewModel: KViewModel) {
        mMap.put(key, viewModel)?.clear()
    }

    operator fun get(key: String): KViewModel? {
        return mMap[key]
    }

    fun keys(): Set<String> {
        return HashSet(mMap.keys)
    }

    /**
     * Clears internal storage and notifies ViewModels that they are no longer used.
     */
    fun clear() {
        for (vm in mMap.values) {
            vm.clear()
        }
        mMap.clear()
    }
}
