package hofy.flicker.app

import android.app.Application
import hofy.data.di.dataModule
import hofy.domain.di.domainModule
import hofy.presentation.ui.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FlickerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FlickerApp)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}