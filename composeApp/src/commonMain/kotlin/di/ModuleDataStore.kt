package di

import org.koin.core.module.Module

expect object ModuleDataStore {
    val repositories: Module
}