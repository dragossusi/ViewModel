package ro.dragossusi


/**
 * Implementations of `Factory` interface are responsible to instantiate ViewModels.
 */
actual interface KViewModelProviderFactory {
    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass a `Class` whose instance is requested
     * @return a newly created ViewModel
     */
    fun <T : KViewModel> create(modelClass: Class<T>): T
}

actual open class KViewModelProvider actual constructor(
    private val store: KViewModelStore,
    private val factory: KViewModelProviderFactory
) {

    /**
     * @suppress
     */
//    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public open class OnRequeryFactory {
        public open fun onRequery(viewModel: KViewModel) {}
    }

    /**
     * Implementations of `Factory` interface are responsible to instantiate ViewModels.
     *
     *
     * This is more advanced version of [Factory] that receives a key specified for requested
     * [ViewModel].
     *
     * @suppress
     */
//    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public abstract class KeyedFactory : OnRequeryFactory(), KViewModelProviderFactory {
        /**
         * Creates a new instance of the given `Class`.
         *
         * @param key a key associated with the requested ViewModel
         * @param modelClass a `Class` whose instance is requested
         * @return a newly created ViewModel
         */
        public abstract fun <T : KViewModel> create(
            key: String,
            modelClass: Class<T>
        ): T

        override fun <T : KViewModel> create(modelClass: Class<T>): T {
            throw UnsupportedOperationException(
                "create(String, Class<?>) must be called on implementations of KeyedFactory"
            )
        }
    }

    /**
     * Creates `ViewModelProvider`. This will create `ViewModels`
     * and retain them in a store of the given `ViewModelStoreOwner`.
     *
     *
     * This method will use the
     * [default factory][KHasDefaultViewModelProviderFactory.getDefaultViewModelProviderFactory]
     * if the owner implements [KHasDefaultViewModelProviderFactory]. Otherwise, a
     * [NewInstanceFactory] will be used.
     */
    public constructor(
        owner: KViewModelStoreOwner
    ) : this(owner.getViewModelStore(), defaultFactory(owner))

    /**
     * Creates `ViewModelProvider`, which will create `ViewModels` via the given
     * `Factory` and retain them in a store of the given `ViewModelStoreOwner`.
     *
     * @param owner   a `ViewModelStoreOwner` whose [ViewModelStore] will be used to
     * retain `ViewModels`
     * @param factory a `Factory` which will be used to instantiate
     * new `ViewModels`
     */
    public constructor(owner: KViewModelStoreOwner, factory: KViewModelProviderFactory) : this(
        owner.getViewModelStore(),
        factory
    )

    /**
     * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this `ViewModelProvider`.
     *
     *
     * The created ViewModel is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param modelClass The class of the ViewModel to create an instance of it if it is not
     * present.
     * @return A ViewModel that is an instance of the given type `T`.
     * @throws IllegalArgumentException if the given [modelClass] is local or anonymous class.
     */
//    @MainThread
    public open operator fun <T : KViewModel> get(modelClass: Class<T>): T {
        val canonicalName = modelClass.canonicalName
            ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")
        return get("$DEFAULT_KEY:$canonicalName", modelClass)
    }

    /**
     * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this `ViewModelProvider`.
     *
     * The created ViewModel is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param key        The key to use to identify the ViewModel.
     * @param modelClass The class of the ViewModel to create an instance of it if it is not
     * present.
     * @return A ViewModel that is an instance of the given type `T`.
     */
    @Suppress("UNCHECKED_CAST")
//    @MainThread
    public open operator fun <T : KViewModel> get(key: String, modelClass: Class<T>): T {
        var viewModel = store[key]
        if (modelClass.isInstance(viewModel)) {
            (factory as? OnRequeryFactory)?.onRequery(viewModel!!)
            return viewModel as T
        } else {
            @Suppress("ControlFlowWithEmptyBody")
            if (viewModel != null) {
                // TODO: log a warning.
            }
        }
        viewModel = if (factory is KeyedFactory) {
            factory.create(key, modelClass)
        } else {
            factory.create(modelClass)
        }
        store.put(key, viewModel)
        return viewModel
    }

    /**
     * Simple factory, which calls empty constructor on the give class.
     */
    // actually there is getInstance()
    @Suppress("SingletonConstructor")
    public open class NewInstanceFactory : KViewModelProviderFactory {
        @Suppress("DocumentExceptions")
        override fun <T : KViewModel> create(modelClass: Class<T>): T {
            return try {
                modelClass.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        }

        public companion object {
            private var sInstance: NewInstanceFactory? = null

            /**
             * @suppress
             * Retrieve a singleton instance of NewInstanceFactory.
             *
             * @return A valid [NewInstanceFactory]
             */
            @JvmStatic
            public val instance: NewInstanceFactory
                //                @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
                get() {
                    if (sInstance == null) {
                        sInstance = NewInstanceFactory()
                    }
                    return sInstance!!
                }
        }
    }

    companion object {
        internal const val DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey"

        internal fun defaultFactory(owner: KViewModelStoreOwner): KViewModelProviderFactory =
            if (owner is KHasDefaultViewModelProviderFactory)
                owner.getDefaultViewModelProviderFactory() else NewInstanceFactory.instance
    }
}

/**
 * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
 * an activity), associated with this `ViewModelProvider`.
 *
 * @see ViewModelProvider.get(Class)
 */
public inline fun <reified VM : KViewModel> KViewModelProvider.get(): VM = get(VM::class.java)
