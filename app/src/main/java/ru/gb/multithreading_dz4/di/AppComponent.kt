package ru.gb.multithreading_dz4.di

import dagger.Component
import ru.gb.multithreading_dz4.presentation.MainActivity

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}