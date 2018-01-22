package baseActivities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import application.EnecuumApplication
import com.arellomobile.mvp.MvpAppCompatActivity
import com.enecuum.androidapp.navigation.ActivityNavigator

/**
 * Created by oleg on 22.01.18.
 */
open class BaseActivity : MvpAppCompatActivity() {
    override fun onResume() {
        super.onResume()
        EnecuumApplication.cicerone().navigatorHolder.setNavigator(ActivityNavigator(this))
    }

    override fun onPause() {
        super.onPause()
        EnecuumApplication.cicerone().navigatorHolder.removeNavigator()
    }
}