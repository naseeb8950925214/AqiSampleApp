package com.example.aqiapp.socket

interface SocketConnectionListener {
    fun connectSocket()
    fun onClose()
}