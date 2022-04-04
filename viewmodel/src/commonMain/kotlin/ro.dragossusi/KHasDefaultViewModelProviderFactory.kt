package ro.dragossusi

expect interface KHasDefaultViewModelProviderFactory {
    /**
     * Returns the default [androidx.lifecycle.ViewModelProvider.Factory] that should be
     * used when no custom `Factory` is provided to the
     * [androidx.lifecycle.ViewModelProvider] constructors.
     *
     * @return a `ViewModelProvider.Factory`
     */
    fun getDefaultViewModelProviderFactory(): KViewModelProviderFactory
}