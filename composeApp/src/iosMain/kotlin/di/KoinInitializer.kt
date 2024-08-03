package di

import org.koin.core.context.startKoin

actual class KoinInitializer {
    actual fun init() {
        startKoin {
            modules(ModuleDb.module, ModulesDataStore.repositories, ModuleVM.viewModels)
        }
    }
}