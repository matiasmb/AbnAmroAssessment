package com.matiasmb.basecode.presentation.di

import com.matiasmb.basecode.presentation.MainActivity
import com.matiasmb.basecode.PerActivity
import dagger.Module
import dagger.Subcomponent

@Subcomponent (modules = [MainActivityModule::class])
@PerActivity
interface MainActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}

@Module(subcomponents = [MainActivityComponent::class])
class MainActivitySubcomponentModule
