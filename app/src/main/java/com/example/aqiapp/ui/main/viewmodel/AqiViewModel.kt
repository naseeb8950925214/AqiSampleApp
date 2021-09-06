package com.example.aqiapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aqiapp.data.model.response.AqiModel
import com.example.aqiapp.data.repository.AqiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AqiViewModel(private val aqiRepository: AqiRepository) : ViewModel() {
    private val TAG = AqiViewModel::class.java.canonicalName
    private var mAqiResponseLiveData: MutableLiveData<List<AqiModel>?> = MutableLiveData()
    private val mCoroutineJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mCoroutineJob)

    fun getAqiResponseLiveDataLiveData(): LiveData<List<AqiModel>?> {
        return mAqiResponseLiveData
    }

    fun fetchAqiResponse() {
        Log.d(TAG, "fetchAqiResponse")
        mUiScope.launch {
            aqiRepository.getAqiDataAndInitialiseSocket().collect {
                Log.d(TAG, "fetchAqiResponse aqiResponse : $it")
                mAqiResponseLiveData.postValue(it)
            }
        }
    }

    fun closeSocket() {
        Log.d(TAG, "closeSocket")
        aqiRepository.closeSocket()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
        mCoroutineJob.cancel()
    }
}