package com.example.aqiapp.ui.main.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.aqiapp.R
import com.example.aqiapp.data.model.response.AqiModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_graph.*

class GraphFragment : BaseFragment(R.layout.fragment_graph) {
    private val TAG = GraphFragment::class.java.canonicalName
    private var mSelectedCityName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        setupAqiDataObserver()
        initView()
    }

    private fun initView() {
        Log.d(TAG, "initView")
        val args: GraphFragmentArgs by navArgs()
        mSelectedCityName = args.selectedCityName
        Log.d(TAG, "initView mSelectedCityName : $mSelectedCityName")
        //chart.setOnChartValueSelectedListener(this)
        chart.description.isEnabled = true
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(true)
        chart.setBackgroundColor(Color.LTGRAY)
        val data = LineData()
        data.setValueTextColor(Color.BLACK)
        chart.data = data

        val tfLight = Typeface.createFromAsset(resources.assets, "OpenSans-Bold.ttf")
        val l: Legend = chart.legend
        l.form = LegendForm.LINE
        l.typeface = tfLight
        l.textColor = Color.BLACK

        val xl: XAxis = chart.xAxis
        xl.typeface = tfLight
        xl.textColor = Color.BLACK
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true

        val leftAxis: YAxis = chart.axisLeft
        leftAxis.typeface = tfLight
        leftAxis.textColor = Color.BLACK
        leftAxis.axisMaximum = 600f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)

        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false
    }

    private fun setupAqiDataObserver() {
        Log.d(TAG, "setupAqiDataObserver")
        getAqiViewModel().getAqiResponseLiveDataLiveData()
            .observe(viewLifecycleOwner, { aqiResponse ->
                Log.d(TAG, "setupAqiDataObserver aqiResponse : $aqiResponse")
                if (!aqiResponse.isNullOrEmpty()) {
                    for (response in aqiResponse) {
                        if (!response.city.isNullOrEmpty() && response.city.equals(
                                mSelectedCityName,
                                true
                            )
                        ) {
                            addEntryToGraph(response)
                            break
                        }
                    }
                }
            })
    }

    private fun addEntryToGraph(aqiModel: AqiModel) {
        Log.d(TAG, "addEntryToGraph aqiModel : $aqiModel")
        val data = chart.data

        if (data != null && aqiModel.aqi != null) {
            var set = data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(
                Entry(
                    set.entryCount.toFloat(), /*(Math.random() * 40).toFloat() + 30f)*/
                    aqiModel.aqi.toFloat(),
                ),
                0
            )
            data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.setVisibleXRangeMaximum(120f)
            chart.moveViewToX(data.entryCount.toFloat())
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "$mSelectedCityName Aqi")
        set.axisDependency = AxisDependency.LEFT
        set.color = ColorTemplate.getHoloBlue()
        set.setCircleColor(Color.WHITE)
        set.lineWidth = 2f
        set.circleRadius = 4f
        set.fillAlpha = 65
        set.fillColor = ColorTemplate.getHoloBlue()
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = Color.WHITE
        set.valueTextSize = 9f
        set.setDrawValues(false)
        return set
    }
}