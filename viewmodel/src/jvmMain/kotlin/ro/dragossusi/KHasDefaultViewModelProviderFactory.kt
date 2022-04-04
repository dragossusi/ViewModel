package ro.dragossusi

actual interface KHasDefaultViewModelProviderFactory {
    /**
     * Returns the default [androidx.lifecycle.ViewModelProvider.Factory] that should be
     * used when no custom `Factory` is provided to the
     * [androidx.lifecycle.ViewModelProvider] constructors.
     *
     * @return a `ViewModelProvider.Factory`
     */
    actual fun getDefaultViewModelProviderFactory(): KViewModelProviderFactory
}