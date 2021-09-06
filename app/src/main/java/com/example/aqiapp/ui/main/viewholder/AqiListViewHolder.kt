package com.example.aqiapp.ui.main.viewholder

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aqiapp.App
import com.example.aqiapp.R
import com.example.aqiapp.data.model.response.AqiModel
import kotlinx.android.synthetic.main.view_holder_aqi_list.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AqiListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val TAG = AqiListViewHolder::class.java.canonicalName

    fun bind(aqiModel: AqiModel) {
        Log.d(TAG, "bind")
        itemView.cityValueTV.text = aqiModel.city
        itemView.currentAQIValueTV.text = getRoundedOffAqiValue(aqiModel.aqi)
        itemView.lastUpdatedTV.text = getLastUpdatedString(aqiModel.lastUpdate)
        itemView.top_layout.setBackgroundColor(getBackgroundColor(aqiModel.aqi))
    }

    private fun getRoundedOffAqiValue(aqiValue: Double?): String {
        Log.d(TAG, "getRoundedOffAqiValue aqiValue : $aqiValue")
        return if (aqiValue != null) roundOffDecimal(aqiValue).toString() else ""
    }

    private fun roundOffDecimal(aqiValue: Double): Double {
        Log.d(TAG, "roundOffDecimal aqiValue : $aqiValue")
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(aqiValue).toDouble()
    }

    private fun getLastUpdatedString(lastUpdatedValue: Long): String {
        val currentTime = System.currentTimeMillis()
        val diffInCurrentTimeAndLastUpdated = currentTime - lastUpdatedValue
        var lastUpdatedString = ""
        Log.d(
            TAG,
            "getLastUpdatedString lastUpdatedValue : $lastUpdatedValue currentTime : $currentTime " +
                    "diffInCurrentTimeAndLastUpdated : $diffInCurrentTimeAndLastUpdated"
        )
        when {
            diffInCurrentTimeAndLastUpdated <= 60000 -> lastUpdatedString = "Few seconds ago"
            diffInCurrentTimeAndLastUpdated > 60000 || diffInCurrentTimeAndLastUpdated < 60000 * 60 * 60 -> lastUpdatedString =
                "A minute ago"
            else -> getTimeStringFromMillis(lastUpdatedValue)
        }
        return lastUpdatedString
    }

    private fun getBackgroundColor(aqiValue: Double?): Int {
        Log.d(TAG, "getBackgroundColor aqiValue : $aqiValue")
        var backgroundColor = R.color.white
        if (aqiValue != null) {
            when (aqiValue) {
                in 0.0..50.0 -> backgroundColor = R.color.light_green
                in 51.0..100.0 -> backgroundColor = R.color.green
                in 101.0..200.0 -> backgroundColor = R.color.yellow
                in 201.0..300.0 -> backgroundColor = R.color.light_brown
                in 301.0..400.0 -> backgroundColor = R.color.red
                in 401.0..500.0 -> backgroundColor = R.color.dark_red
            }
        }
        return ContextCompat.getColor(App.getInstance(), backgroundColor)
    }

    private fun getTimeStringFromMillis(millisInLong: Long): String {
        Log.d(TAG, "getTimeStringFromMillis millisInLong : $millisInLong")
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeString = sdf.format(millisInLong)
        Log.d(TAG, "getTimeStringFromMillis timeString : $timeString")
        return timeString
    }
}