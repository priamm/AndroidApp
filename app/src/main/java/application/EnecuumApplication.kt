package application

import android.app.Application
import android.content.Context
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

/**
 * Created by oleg on 22.01.18.
 */
class EnecuumApplication : Application() {
    companion object {
        private lateinit var appContext: Context
        fun applicationContext() : Context = appContext

        private lateinit var cicerone: Cicerone<Router>
        fun cicerone() : Cicerone<Router> = cicerone
    }

    public override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        cicerone = Cicerone.create()
    }


}