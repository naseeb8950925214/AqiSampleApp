package com.example.aqiapp.socket

import android.util.Log
import com.example.aqiapp.data.model.response.AqiModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI


class SocketConnectionManager(val serverUri: String) : SocketConnectionListener {
    private val TAG = SocketConnectionManager::class.java.canonicalName
    private var mSocket: WebSocketClient? = null
    private val mGson: Gson = GsonBuilder().create()
    private var mMessageListener: ReceivedEventListener? = null
    var isConnected = false
        private set

    @Synchronized
    fun removeCallBAck() {
        mMessageListener = null
    }

    fun registerCallback(listener: ReceivedEventListener?) {
        mMessageListener = listener
    }

    override fun connectSocket() {
        Log.d(TAG, "connectSocket mSocket: $mSocket")
        if (mSocket == null || mSocket!!.readyState != WebSocket.READYSTATE.OPEN) {
            Log.d(TAG, "connectSocket Connecting to socket")
            mSocket = object : WebSocketClient(URI(serverUri)) {
                override fun onOpen(serverHandshake: ServerHandshake) {
                    Log.d(TAG, "onOpen")
                    isConnected = true
                    mMessageListener?.onSocketConnected()
                }

                override fun onMessage(message: String) {
                    Log.d(TAG, "onMessage : $message")
                    try {
                        val typeToken = object : TypeToken<ArrayList<AqiModel>>() {}
                        val responseModel: List<AqiModel>? = mGson.fromJson(
                            message, typeToken.type
                        )
                        mMessageListener?.onMessage(responseModel)
                    } catch (exception: Exception) {
                        Log.d(
                            TAG,
                            "onMessage exception: ${exception.message}"
                        )
                    }
                }

                override fun onClose(i: Int, message: String, b: Boolean) {
                    Log.d(TAG, "onClose $message")
                    isConnected = false
                    mMessageListener?.onSocketDisconnected(message)
                }

                override fun onError(e: Exception) {
                    Log.d(TAG, "Error " + e.message)
                    isConnected = false
                    mMessageListener?.onSocketConnectError(e.message)
                }
            }
            mSocket!!.connect()
        }
    }

    override fun onClose() {
        Log.d(
            TAG,
            "Enter onClose(), mSocket: $mSocket"
        )
        isConnected = false
        if (mSocket != null) {
            mSocket!!.close()
            mSocket = null
        }
    }

    companion object {
        private var sConnectionManagerInstance: SocketConnectionManager? = null
        fun getInstance(serverUri: String): SocketConnectionManager? {
            if (sConnectionManagerInstance == null) {
                sConnectionManagerInstance = SocketConnectionManager(serverUri)
            }
            return sConnectionManagerInstance
        }
    }
}