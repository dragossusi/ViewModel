package ro.dragossusi

/**
 * A scope that owns {@link ViewModelStore}.
 * <p>
 * A responsibility of an implementation of this interface is to retain owned ViewModelStore
 * during the configuration changes and call {@link ViewModelStore#clear()}, when this scope is
 * going to be destroyed.
 */
actual interface KViewModelStoreOwner {
    actual fun getViewModelStore(): KViewModelStore
}