package com.pandaproductions.beefit.ViewHolder

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandaproductions.beefit.Models.StepData
import com.pandaproductions.beefit.R

class StepViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    var steps: TextView = view.findViewById(R.id.stepCount)
    var date: TextView = view.findViewById(R.id.date)

    var isActive: Boolean = false
        set(value) {
            field = value
            view.setBackgroundColor(if (value) Color.LTGRAY else Color.TRANSPARENT)
        }

    var stepDate: StepData = StepData("", 1)

}