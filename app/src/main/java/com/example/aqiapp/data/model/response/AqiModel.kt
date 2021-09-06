package com.example.aqiapp.data.model.response

data class AqiModel(
    val city: String? = null,
    val aqi: Double? = null,
    val lastUpdate: Long = System.currentTimeMillis()
)
