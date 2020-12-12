package com.mmarciniak.diseasetest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.symptom_row.view.*

class Test2Adapter(private val symptomsDescriptions: List<String>): RecyclerView.Adapter<Test2ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Test2ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.symptom_row, parent, false)
        return Test2ViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return symptomsDescriptions.size
    }

    override fun onBindViewHolder(holder: Test2ViewHolder, position: Int) {
        try {
            val symptomDescription = symptomsDescriptions[position]
            holder.itemView.symptom_label.text = symptomDescription
            holder.itemView.number_icon.text = position.toString()
        }
        catch (e: ArrayIndexOutOfBoundsException)
        {
            e.printStackTrace()
        }
    }

}

class Test2ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
}