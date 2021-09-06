package com.example.aqiapp.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aqiapp.App
import com.example.aqiapp.ui.base.ViewModelFactory
import com.example.aqiapp.ui.main.viewmodel.AqiViewModel
import com.example.aqiapp.utils.NetworkConnectionLiveData

open class BaseFragment(fragmentId: Int) : Fragment(fragmentId) {
    private val TAG = BaseFragment::class.java.canonicalName
    private lateinit var mAqiViewModel: AqiViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        mAqiViewModel = ViewModelProvider(this, ViewModelFactory()).get(AqiViewModel::class.java)
        setupNetworkConnectionObserver()
    }

    fun getAqiViewModel(): AqiViewModel {
        Log.d(TAG, "getAqiViewModel")
        return mAqiViewModel
    }

    private fun setupNetworkConnectionObserver() {
        NetworkConnectionLiveData(App.getInstance()).observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isConnected ->
                if (!isConnected) {
                    Log.d(
                        TAG,
                        "setupNetworkConnectionObserver internet not connected. Hence not taking any action"
                    )
                    return@Observer
                }
                Log.d(
                    TAG,
                    "setupNetworkConnectionObserver internet is connected. Hence taking any action"
                )
                mAqiViewModel.fetchAqiResponse()
            })
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        mAqiViewModel.fetchAqiResponse()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        mAqiViewModel.closeSocket()
        super.onPause()
    }
}