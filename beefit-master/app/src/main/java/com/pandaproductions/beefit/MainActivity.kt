package com.pandaproductions.beefit

import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.pandaproductions.beefit.Database.AppDatabase
import com.pandaproductions.beefit.Fragment.HomeFragment
import com.pandaproductions.beefit.Fragment.PreferencesFragment
import com.pandaproductions.beefit.Fragment.StepsFragment
import com.pandaproductions.beefit.Models.User
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private val LAUNCH_PET_PAGE = "android.intent.action.LAUNCH_PET_PAGE"
    private val LAUNCH_STEPS_PAGE = "android.intent.action.LAUNCH_STEPS_PAGE"
    var sensorManager: SensorManager? = null
    var running = false
    var nSteps: Int = 0
    val STEP_COUNT = "step_count"
    val PREFS_FILENAME = "com.pandaproductions.beefit.stepinfo"
    val GOAL_PREF = "step_goal_pref"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val stepIntent = Intent(this, StepCounterService::class.java)
        startService(stepIntent)

//        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val alarmIntent = Intent(this, AlarmReceiver::class.java)
//        val pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.set(Calendar.HOUR_OF_DAY, 13)
//        calendar.set(Calendar.MINUTE, 21)
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
////            AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingAlarmIntent)
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime(),
//            2*60*1000,
//            pendingAlarmIntent)


        doAsync {
            // check if user exists, run setup if they do not
            val users: List<User> = AppDatabase.getInstance(this@MainActivity).userDao().getUsers()
            if (users.isEmpty()) { // setup user if no user info in db
                val intent = Intent(this@MainActivity, AppSetupActivity::class.java)
                startActivityForResult(intent, 0)
            }
        }


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        when (intent.action) {
            LAUNCH_STEPS_PAGE -> {
                openFragment(R.id.nav_steps)
            }
            else -> {
                openFragment(R.id.nav_home)
            }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.shareAchievement -> {
                val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
                val stepCount = prefs.getInt(STEP_COUNT, 0)
                val stepGoal = prefs.getInt(GOAL_PREF, 10000)
                var message = ""
                if (stepCount >= stepGoal) {
                    message = String.format(getString(R.string.stepMessageGoal), stepCount.toString(), stepGoal.toString())
                } else {
                    message = String.format(getString(R.string.stepMessage), stepCount.toString(), stepGoal.toString())
                }
                sendMessage(message)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun openFragment(fragmentId: Int) {
        // todo change titles for pages
        when (fragmentId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frament_container,
                    StepsFragment()
                ).commit()
            }
            R.id.nav_steps -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frament_container,
                    HomeFragment()
                ).commit()
            }
            R.id.nav_settings -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frament_container,
                    PreferencesFragment()
                ).commit()
            }
        }
    }

    //TODO link this up to the the action bar
    private fun sendMessage(message: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        openFragment(item.itemId)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
