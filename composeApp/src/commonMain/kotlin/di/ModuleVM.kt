package di

import feature.settings.SettingsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ModuleVM {
    val viewModels = module {
        viewModelOf(::SettingsViewModel)
    }
}