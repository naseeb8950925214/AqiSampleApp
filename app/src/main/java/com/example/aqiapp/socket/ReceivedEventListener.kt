package com.example.aqiapp.socket

import com.example.aqiapp.data.model.response.AqiModel

interface ReceivedEventListener {
    fun onSocketConnected()
    fun onSocketConnectError(errorMsg: String?)
    fun onSocketDisconnected(errorMsg: String?)
    fun onMessage(aqiModelList: List<AqiModel>?)
}