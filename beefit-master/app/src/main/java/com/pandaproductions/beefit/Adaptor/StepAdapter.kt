package com.pandaproductions.beefit.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandaproductions.beefit.Database.AddStepDataTask
import com.pandaproductions.beefit.Database.AppDatabase
import com.pandaproductions.beefit.Models.StepData
import com.pandaproductions.beefit.R
import com.pandaproductions.beefit.ViewHolder.StepViewHolder


class StepAdapter(val context: Context) : RecyclerView.Adapter<StepViewHolder>() {
    private var recyclerView: RecyclerView? = null
    private var selectedIndex: Int = RecyclerView.NO_POSITION
    var onSelect: (StepData) -> Unit = {}
    var onNothingSelected: () -> Unit = {}

    init {

    }

    var database: AppDatabase? = null
        set(value) {
            field = value
            value?.let {

            }
        }

    var stepItems: MutableList<StepData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {


        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.step_view_holder, parent, false);
        val holder = StepViewHolder(view)
        view.setOnClickListener {
            selectIndex(holder.adapterPosition)
        }
        return holder

    }

    override fun getItemCount(): Int {
        return stepItems.size
    }

    fun update() {
        notifyItemChanged(selectedIndex)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.stepDate = stepItems[position]
        holder.isActive = position == selectedIndex
        holder.date.text = stepItems[position].date
        holder.steps.text = stepItems[position].steps.toString()
    }

    fun insert(stepData: StepData) {
        if (database != null) {
            stepItems.add(stepData)
            selectIndex(stepItems.size - 1)
            notifyItemInserted(stepItems.size - 1)
            AddStepDataTask(database!!, stepData).execute()
        } else {

        }
    }

    private fun selectIndex(i: Int) {
        if (i == selectedIndex) return

        val oldSelectedIndex = selectedIndex
        selectedIndex = i
        notifyItemChanged(oldSelectedIndex)
        notifyItemChanged(selectedIndex, true)


        if (selectedIndex == RecyclerView.NO_POSITION) {
            onNothingSelected()
        } else {
            recyclerView?.scrollToPosition(i)
            onSelect(stepItems[i])
        }
    }
}