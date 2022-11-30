package com.pandaproductions.beefit

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

class StepCounterService : IntentService("StepCounterService"), SensorEventListener {

    var sensorManager: SensorManager? = null
    val STEP_COUNT = "step_count"
    val PREFS_FILENAME = "com.pandaproductions.beefit.stepinfo"
    val GOAL_PREF = "step_goal_pref"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)

        // Use this stuff to test database midnight trigger
//        val currentTime = System.currentTimeMillis()
//        val oneMinute = (60 * 1000).toLong()
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP, currentTime + oneMinute,
//            oneMinute, pendingAlarmIntent
//        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingAlarmIntent
        )
    }

    private fun createNotificationChannel() {
        val channelId = "BeeFitStepNotifications"
        val channel =
            NotificationChannel(channelId, "step notification channel", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Notification of step goal reached"
        channel.setShowBadge(true)

        val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun stepGoalNotification() {
        Log.d("STEPS", "Should notify?")
        val pendingIntent = PendingIntent.getService(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val stepGoal = prefs.getInt(GOAL_PREF, 6000)
        val notification = NotificationCompat.Builder(applicationContext, "BeeFitStepNotifications")
            .setContentTitle(getString(R.string.stepGoalNotifTitle))
            .setContentText(String.format(getString(R.string.stepGoalNotifDescription), stepGoal.toString()))
            .setSmallIcon(R.drawable.ic_pets_black_24dp)
            .setContentIntent(pendingIntent).build()
        val notiManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notiManager.notify(12345, notification)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
            val stepCount = prefs.getInt(STEP_COUNT, 0)
            val stepGoal = prefs.getInt(GOAL_PREF, 6000)

            val preferences = this.defaultSharedPreferences
            val sendNotification = preferences.getBoolean("notificationPreference", false)

            if (stepCount == stepGoal && sendNotification) {
                stepGoalNotification()
            }
            val editor = prefs.edit()
            editor.putInt(STEP_COUNT, stepCount + 1)
            editor.apply()
            Log.d("STEPS", "Current step count: " + stepCount.toString())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("STEPS", "handle intent?")
    }
}