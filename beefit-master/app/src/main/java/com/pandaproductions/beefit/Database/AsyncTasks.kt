package com.pandaproductions.beefit.Database

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.room.Room
import com.pandaproductions.beefit.Adaptor.StepAdapter
import com.pandaproductions.beefit.Models.Pet
import com.pandaproductions.beefit.Models.StepData
import com.pandaproductions.beefit.Models.User
import java.lang.ref.WeakReference
import java.util.function.Function

class LoadDatabaseTask(private val context: Context, private val callback: Function<AppDatabase?, Unit>) :
    AsyncTask<Unit, Unit, AppDatabase>() {
    private val weakContext = WeakReference(context)

    override fun doInBackground(vararg params: Unit?): AppDatabase? {
        var database: AppDatabase? = null
        database =
            Room.databaseBuilder(weakContext.get()!!.applicationContext, AppDatabase::class.java, "database").build()
        return database
    }

    override fun onPostExecute(database: AppDatabase?) {
        callback.apply(database)
    }
}

class GetUserTask(
    private val database: AppDatabase, private val callback: Function<User, Unit>
) : AsyncTask<Unit, Unit, User>() {
    override fun doInBackground(vararg p0: Unit?): User {
        return database.userDao().getUser(0)
    }

    override fun onPostExecute(user: User) {
        callback.apply(user)
    }
}

class AddStepDataTask(
    private val database: AppDatabase,
    private val stepData: StepData
) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg p0: Unit?) {
        stepData.id = database.stepDataDao().insert(stepData)
    }

    override fun onPostExecute(result: Unit?) {
        Log.d("DATABASE", stepData.toString())
    }
}

class GetAllStepDataTask(
    private val database: AppDatabase,
    private val adapter: StepAdapter, private val callback: Function<Unit, Unit>
) : AsyncTask<Unit, Unit, MutableList<StepData>>() {
    override fun doInBackground(vararg p0: Unit?): MutableList<StepData> {
        val stepDataDao = database.stepDataDao()
        return stepDataDao.getStepData()
    }

    override fun onPostExecute(stepData: MutableList<StepData>) {
        adapter.stepItems = stepData
        adapter.update()
        callback.apply(Unit)
    }
}

class GetPetTask(
    private val database: AppDatabase, private val callback: Function<Pet, Unit>
) : AsyncTask<Unit, Unit, Pet>() {
    override fun doInBackground(vararg p0: Unit?): Pet {
        return database.petDao().getPet(0)
    }

    override fun onPostExecute(pet: Pet) {
        callback.apply(pet)
    }
}

class UpdatePetTask(private val database: AppDatabase, val pet: Pet) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        return database.petDao().updatePet(pet)
    }
}