package com.pandaproductions.beefit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.pandaproductions.beefit.Database.AddStepDataTask
import com.pandaproductions.beefit.Database.GetPetTask
import com.pandaproductions.beefit.Database.LoadDatabaseTask
import com.pandaproductions.beefit.Database.UpdatePetTask
import com.pandaproductions.beefit.Models.StepData
import java.text.DateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.function.Function

class AlarmReceiver : BroadcastReceiver() {
    val STEP_COUNT = "step_count"
    val GOAL_PREF = "step_goal_pref"
    val PREFS_FILENAME = "com.pandaproductions.beefit.stepinfo"

    override fun onReceive(context: Context?, intent: Intent?) {
        val prefs = context!!.getSharedPreferences(PREFS_FILENAME, 0)
        val stepCount = prefs.getInt(STEP_COUNT, 0)
        val stepGoal = prefs.getInt(GOAL_PREF, 10000)
        val editor = prefs.edit()
        editor.putInt(STEP_COUNT, 0)
        editor.apply()
        Log.d("STEPS", "AlarmReceiver triggered to check step count")

        val currentDate = Date()
        val dateString = DateFormat.getDateInstance().format(currentDate)

        val stepData = StepData(dateString, stepCount)

        LoadDatabaseTask(context, Function { database ->
            AddStepDataTask(database!!, stepData).execute()


            var increment: Int = -10
            if (stepCount >= stepGoal) {
                increment = 10
            }

            GetPetTask(database, Function { pet ->
                pet.incrementHealth(increment)
                Log.d("DATABASE", pet.toString())
                UpdatePetTask(database, pet).execute()
            }).execute()
        }).execute()
    }
}