package com.example.aqiapp.ui.main.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aqiapp.App
import com.example.aqiapp.R
import com.example.aqiapp.data.model.response.AqiModel
import com.example.aqiapp.ui.base.ViewModelFactory
import com.example.aqiapp.ui.main.adapter.AqiListAdapter
import com.example.aqiapp.ui.main.viewmodel.AqiViewModel
import com.example.aqiapp.utils.NetworkConnectionLiveData
import kotlinx.android.synthetic.main.fragment_aqi.*

class AqiFragment : BaseFragment(R.layout.fragment_aqi), AqiListAdapter.RecyclerViewClickListener {
    private val TAG = AqiFragment::class.java.canonicalName
    private lateinit var mAqiListAdapter: AqiListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        setupAqiDataObserver()
        initView()
    }

    private fun initView() {
        Log.d(TAG, "initView")
        progressBar.visibility = View.VISIBLE
        mAqiListAdapter = AqiListAdapter(arrayListOf(), this)
        val dividerItemDecoration = DividerItemDecoration(
            aqiListRV.context,
            (aqiListRV.layoutManager as LinearLayoutManager).orientation
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.recycler_view_divider
            )!!
        )
        aqiListRV.addItemDecoration(dividerItemDecoration)
        aqiListRV.adapter = mAqiListAdapter
    }

    private fun setupAqiDataObserver() {
        Log.d(TAG, "setupAqiDataObserver")
        getAqiViewModel().getAqiResponseLiveDataLiveData()
            .observe(viewLifecycleOwner, { aqiResponse ->
                Log.d(TAG, "setupAqiDataObserver aqiResponse : $aqiResponse")
                progressBar.visibility = View.GONE
                if (!aqiResponse.isNullOrEmpty()) {
                    mAqiListAdapter.addData(aqiResponse)
                    mAqiListAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onItemClickListener(aqiModel: AqiModel) {
        Log.d(TAG, "setupAqiDataObserver aqiModel : $aqiModel")
        if (!aqiModel.city.isNullOrEmpty()) {
            findNavController().navigate(
                AqiFragmentDirections.actionAqiFragmentToGraphFragment(
                    aqiModel.city
                )
            )
        }
    }
}