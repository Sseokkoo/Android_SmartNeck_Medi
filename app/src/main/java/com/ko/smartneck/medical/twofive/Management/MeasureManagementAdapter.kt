package com.ko.smartneck.medical.twofive.Management

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ko.smartneck.medical.twofive.R
import java.util.*

class MeasureManagementAdapter(var measureMents: MutableList<MeasurementFull>, var inflater: LayoutInflater) : BaseAdapter() {
    private var holder: Holder? = null

    override fun getCount(): Int {
        return measureMents.size

    }

    override fun getItem(position: Int): Any {
        return measureMents[position]

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.item_measure_management, null)
            holder = Holder()

        }
        holder!!.tv_idx = view!!.findViewById(R.id.tv_idx)
        holder!!.tv_height = view.findViewById(R.id.tv_height)
        holder!!.tv_name = view.findViewById(R.id.tv_name)
        holder!!.tv_weight = view.findViewById(R.id.tv_weight)
        holder!!.tv_date = view.findViewById(R.id.tv_date)

        holder!!.tv_idx!!.text = (position+1).toString()
        holder!!.tv_name!!.text = measureMents[position].name
        holder!!.tv_weight!!.text = "${(measureMents[position].weight.toString().toFloat() / 10)}kg"
        holder!!.tv_height!!.text = measureMents[position].height.toString() + "mm"
        holder!!.tv_date!!.text = measureMents[position].date
        return view
    }

    internal inner class Holder {
        var tv_date: TextView? = null
        var tv_idx: TextView? = null
        var tv_weight: TextView? = null
        var tv_name: TextView? = null
        var tv_height: TextView? = null
    }

}