package baseActivities

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import application.EnecuumApplication
import com.arellomobile.mvp.MvpAppCompatActivity
import com.enecuum.androidapp.navigation.ActivityNavigator

/**
 * Created by oleg on 22.01.18.
 */
open class BackActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}