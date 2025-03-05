package ru.gb.multithreading_dz4.di

import dagger.Component
import ru.gb.multithreading_dz4.presentation.MainActivity

@Component(modules = [MainActivityModule::class])
interface MainActivityComponent {
    // Аннотация для возможности ручного создания экземпляра компонента
    @Component.Builder
    interface Builder {
        fun build(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}