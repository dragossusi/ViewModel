package ro.dragossusi

expect interface KViewModelProviderFactory {

}

expect class KViewModelProvider constructor(
    store: KViewModelStore,
    factory: KViewModelProviderFactory
)