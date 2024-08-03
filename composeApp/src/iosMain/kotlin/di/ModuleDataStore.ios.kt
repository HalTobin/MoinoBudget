package di

import data.createDatastore
import data.repository.PreferenceRepository
import data.repository.PreferenceRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

actual object ModuleDataStore {
    actual val repositories = module {
        single { PreferenceRepositoryImpl(createDatastore()) }.bind<PreferenceRepository>()
    }
}