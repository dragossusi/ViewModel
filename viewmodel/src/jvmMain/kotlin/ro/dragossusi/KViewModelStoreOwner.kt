package ro.dragossusi

actual interface KViewModelStoreOwner {
    /**
     * Returns owned [ViewModelStore]
     *
     * @return a `ViewModelStore`
     */
    fun getViewModelStore(): KViewModelStore
}