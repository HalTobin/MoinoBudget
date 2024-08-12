package di

import org.koin.core.context.startKoin

actual class KoinInitializer {
    actual fun init() {
        startKoin {
            modules(Module.modules,
                ModuleVM.viewModels,
                ModuleDAO.dao,
                ModuleRepositories.repositories)
        }
    }
}