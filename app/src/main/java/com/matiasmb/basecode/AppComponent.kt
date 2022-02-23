package com.matiasmb.basecode

import com.matiasmb.basecode.data.di.DataModule
import com.matiasmb.basecode.data.di.NetworkingModule
import com.matiasmb.basecode.domain.di.DomainModule
import com.matiasmb.basecode.presentation.di.MainActivityComponent
import com.matiasmb.basecode.presentation.di.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        NetworkingModule::class,
        MainActivityModule::class,
        DomainModule::class,
        DataModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }

    fun mainActivityComponent(): MainActivityComponent.Factory
    fun inject(application: MyApplication)
}
