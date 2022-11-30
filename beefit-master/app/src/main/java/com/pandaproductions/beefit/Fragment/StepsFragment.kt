package com.pandaproductions.beefit.Fragment

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import at.grabner.circleprogress.CircleProgressView
import com.pandaproductions.beefit.Database.GetPetTask
import com.pandaproductions.beefit.Database.LoadDatabaseTask
import com.pandaproductions.beefit.ENUM.PetType
import com.pandaproductions.beefit.R
import java.util.function.Function


class StepsFragment : Fragment(), SensorEventListener {
    var sensorManager: SensorManager? = null
    val STEP_COUNT = "step_count"
    val PREFS_FILENAME = "com.pandaproductions.beefit.stepinfo"
    val GOAL_PREF = "step_goal_pref"

    var progressBar: CircleProgressView? = null
    var stepProgressText: TextView? = null
    private lateinit var pet_photo: ImageView
    private lateinit var pet_name: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_steps, container, false)
        pet_photo = view.findViewById(R.id.Pet_Photo)
        pet_name = view.findViewById(R.id.stepPetText)

        LoadDatabaseTask(container!!.context, Function { database ->
            GetPetTask(database!!, Function { pet ->
                if (pet.type == PetType.DEER) {
                    pet_photo.setImageResource(R.drawable.deer)
                } else if (pet.type == PetType.WOLF) {
                    pet_photo.setImageResource(R.drawable.wolf)
                } else if (pet.type == PetType.OWL) {
                    pet_photo.setImageResource(R.drawable.owl)
                } else if (pet.type == PetType.FOX) {
                    pet_photo.setImageResource(R.drawable.fox)
                }
                pet_name.text = pet.name
            }).execute()
        }).execute()



        progressBar = view.findViewById<CircleProgressView>(R.id.stepProgressBar)
        stepProgressText = view.findViewById(R.id.stepPoints)

        sensorManager = getActivity()?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        return view
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        val prefs = this.activity!!.getSharedPreferences(PREFS_FILENAME, 0)
        val stepCount = prefs.getInt(STEP_COUNT, 0)
        val stepGoal = prefs.getInt(GOAL_PREF, 6000)
        progressBar!!.maxValue = stepGoal.toFloat()
        progressBar!!.setValue(stepCount.toFloat())

        stepProgressText!!.text = String.format(getString(R.string.stepProgress), stepCount.toString())
    }

    override fun onResume() {
        super.onResume()
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }
}