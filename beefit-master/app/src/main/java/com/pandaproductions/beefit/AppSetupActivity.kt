package com.pandaproductions.beefit

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.pandaproductions.beefit.Database.AppDatabase
import com.pandaproductions.beefit.Database.daos.PetDao
import com.pandaproductions.beefit.Database.daos.UserDao
import com.pandaproductions.beefit.ENUM.FairyBreadType
import com.pandaproductions.beefit.ENUM.PetType
import com.pandaproductions.beefit.Models.Pet
import com.pandaproductions.beefit.Models.User
import org.jetbrains.anko.doAsync
import java.text.NumberFormat
import java.util.*

class AppSetupActivity : AppCompatActivity() {
    private lateinit var stepGoalSlider: SeekBar
    private lateinit var stepGoalText: TextView
    private lateinit var nextButton: Button
    private lateinit var finishButton: Button

    private lateinit var birthDatePicker: EditText

    private lateinit var userName: String
    private var userStepGoal: Int = 0
    private lateinit var userBirthDate: Date

    private lateinit var petType: PetType
    private lateinit var petName: String
    private lateinit var Deer: ImageButton
    private lateinit var Fox: ImageButton
    private lateinit var Owl: ImageButton
    private lateinit var Wolf: ImageButton
    private lateinit var pet_name: EditText

    val PREFS_FILENAME = "com.pandaproductions.beefit.stepinfo"
    val GOAL_PREF = "step_goal_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_set_up)
        petType = PetType.DEER

        birthDatePicker = findViewById(R.id.birth_date)
        nextButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            onNextButton()
        }

        stepGoalSlider = findViewById(R.id.step_goal)
        stepGoalText = findViewById(R.id.step_goal_num)
        stepGoalText.text = NumberFormat.getIntegerInstance().format(calculateStepGoal(5))

        stepGoalSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of step goal slider
                stepGoalText.text = NumberFormat.getIntegerInstance().format(calculateStepGoal(i))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // do something?
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // do something?
            }
        })
    }

    fun calculateStepGoal(i: Int): Int {
        return 5000 + (i * 1000)
    }

    private fun onNextButton() {
        userName = findViewById<EditText>(R.id.user_name).text.toString()
        userStepGoal = calculateStepGoal(stepGoalSlider.progress)



        setContentView(R.layout.pet_set_up)
        Deer = findViewById(R.id.pet_1)
        Fox = findViewById(R.id.pet_2)
        Owl = findViewById(R.id.pet_3)
        Wolf = findViewById(R.id.pet_4)

        Deer.setOnClickListener {
            Deer.setBackgroundResource(R.drawable.border)
            Fox.setBackgroundResource(0)
            Owl.setBackgroundResource(0)
            Wolf.setBackgroundResource(0)
            petType = PetType.DEER
        }
        Fox.setOnClickListener {
            Deer.setBackgroundResource(0)
            Fox.setBackgroundResource(R.drawable.border)
            Owl.setBackgroundResource(0)
            Wolf.setBackgroundResource(0)
            petType = PetType.FOX
        }
        Owl.setOnClickListener {
            Deer.setBackgroundResource(0)
            Fox.setBackgroundResource(0)
            Owl.setBackgroundResource(R.drawable.border)
            Wolf.setBackgroundResource(0)
            petType = PetType.OWL

        }
        Wolf.setOnClickListener {
            Deer.setBackgroundResource(0)
            Fox.setBackgroundResource(0)
            Owl.setBackgroundResource(0)
            Wolf.setBackgroundResource(R.drawable.border)
            petType = PetType.WOLF

        }

        finishButton = findViewById(R.id.finish_button)
        finishButton.setOnClickListener {
            onFinishButton()
        }
    }

    private fun onFinishButton() {
        // todo make pet and add to database
        // todo make way to pick which pet you want
        var newUser = User(userName, 10, userStepGoal, FairyBreadType.PASTEL)
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val editor = prefs.edit()
        editor.putInt(GOAL_PREF, userStepGoal)
        editor.apply()
        pet_name = findViewById(R.id.pet_name)
        petName = pet_name.text.toString()
        if (petName == "") {
            petName = "Franky"
        }
        val newPet = Pet(petName, petType)
        Toast.makeText(this, newPet.toString(), Toast.LENGTH_SHORT).show()

        doAsync {
            val userDao: UserDao = AppDatabase.getInstance(this@AppSetupActivity).userDao()
            userDao.insert(newUser)
            val petDao: PetDao = AppDatabase.getInstance(this@AppSetupActivity).petDao()
            petDao.insert(newPet)
        }

        val intent = Intent(this@AppSetupActivity, MainActivity::class.java)
        startActivityForResult(intent, 0)
    }
}
