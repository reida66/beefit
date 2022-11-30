package com.pandaproductions.beefit.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pandaproductions.beefit.Adaptor.StepAdapter
import com.pandaproductions.beefit.Database.GetAllStepDataTask
import com.pandaproductions.beefit.Database.LoadDatabaseTask
import com.pandaproductions.beefit.Models.StepData
import com.pandaproductions.beefit.R
import java.util.function.Function


class HomeFragment() : Fragment() {
    lateinit var listt: RecyclerView;
    private lateinit var adapter: StepAdapter
    var list = mutableListOf<StepData>()
    private lateinit var chart: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val stepItems: MutableList<StepData>? = null
        listt = view.findViewById(R.id.stepRecyclerView)
        adapter = StepAdapter(view.context)
        listt.adapter = adapter
        listt.layoutManager = LinearLayoutManager(context)


        LoadDatabaseTask(container!!.context, Function { database ->
            GetAllStepDataTask(database!!, adapter, Function {
                setUpGraph()


            }).execute()

            Toast.makeText(context, "HI", Toast.LENGTH_SHORT).show()


        }).execute()



        chart = view.findViewById(R.id.Chartt)


        //TODO get all the data from the database about past entries.

        //TODO set the adapter once made

        //TODO use the step service class to start the fun

        //TODO start that service
        return view
    }

    fun setUpGraph() {
        val entries = ArrayList<Entry>()
        var x_step_counter: Float = 0f
        adapter.stepItems.forEach {
            entries.add(Entry(x_step_counter, it.steps.toFloat()))
            x_step_counter += 1f

        }
        val dataSet = LineDataSet(entries, "BarDataSet") // add entries to dataset
        val data = LineData(dataSet)
        chart.setData(data)
        chart.setBackgroundColor(2)
        chart.invalidate()
    }
}