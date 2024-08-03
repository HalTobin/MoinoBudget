package di

import feature.settings.SettingsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import ui.MainViewModel

expect object Module {
    val modules: Module
}

object ModuleVM {
    val viewModels = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::SettingsViewModel)
    }
}