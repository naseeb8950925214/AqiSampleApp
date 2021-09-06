package com.example.aqiapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aqiapp.R
import com.example.aqiapp.data.model.response.AqiModel
import com.example.aqiapp.ui.main.viewholder.AqiListViewHolder

class AqiListAdapter(
    private val aqiList: ArrayList<AqiModel>,
    private val recyclerViewClickListener: RecyclerViewClickListener
) : RecyclerView.Adapter<AqiListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AqiListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_holder_aqi_list, parent,
                false
            )
        )

    override fun getItemCount(): Int = aqiList.size

    override fun onBindViewHolder(holder: AqiListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            recyclerViewClickListener.onItemClickListener(aqiList.get(position))
        }
        holder.bind(aqiList[position])
    }

    fun addData(aqiList: List<AqiModel>) {
        this.aqiList.clear()
        this.aqiList.addAll(aqiList)
    }

    interface RecyclerViewClickListener {
        fun onItemClickListener(aqiModel: AqiModel)
    }
}