package com.example.aqiapp.data.repository

import android.util.Log
import com.example.aqiapp.data.model.response.AqiModel
import com.example.aqiapp.socket.ReceivedEventListener
import com.example.aqiapp.socket.SocketConnectionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AqiRepository : ReceivedEventListener {
    private val TAG = AqiRepository::class.java.canonicalName
    private var mSocketManager: SocketConnectionManager? = null
    private val mAqiResponseFlow: MutableStateFlow<List<AqiModel>?> =
        MutableStateFlow(ArrayList())

    fun getAqiDataAndInitialiseSocket(): Flow<List<AqiModel>?> {
        Log.d(TAG, "getAqiDataAndInitialiseSocket")
        initialiseSocket()
        return mAqiResponseFlow
    }

    fun closeSocket() {
        Log.d(TAG, "closeSocket")
        mSocketManager?.onClose()
        mSocketManager?.removeCallBAck()
        mSocketManager = null
    }

    private fun initialiseSocket() {
        closeSocket()
        Log.d(TAG, "initialiseSocket")
        if (mSocketManager == null) {
            val socketServerUri = "ws://city-ws.herokuapp.com"
            Log.d(
                TAG,
                "initialiseSocket socketServerUri : $socketServerUri as mSocketManager == null"
            )
            mSocketManager = SocketConnectionManager.getInstance(socketServerUri)
            mSocketManager!!.registerCallback(this)
            mSocketManager!!.connectSocket()
        } else {
            Log.d(
                TAG,
                "initialiseSocket no need as mSocketManager != null"
            )
        }
    }

    override fun onSocketConnected() {
        Log.d(TAG, "onSocketConnected")
    }

    override fun onSocketConnectError(errorMsg: String?) {
        Log.d(TAG, "onSocketConnectError errorMsg : $errorMsg")
    }

    override fun onSocketDisconnected(errorMsg: String?) {
        Log.d(TAG, "onSocketDisconnected errorMsg : $errorMsg")
    }

    override fun onMessage(aqiModelList: List<AqiModel>?) {
        Log.d(TAG, "onMessage aqiModelList : $aqiModelList")
        aqiModelList?.let {
            mAqiResponseFlow.value = it
        }
    }
}