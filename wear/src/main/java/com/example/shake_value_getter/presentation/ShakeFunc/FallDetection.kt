package com.example.shake_value_getter.presentation.ShakeFunc

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.sqrt

object FallDetection {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private val movementDetectedFlow = MutableStateFlow(true)
    private var lastMovementTime: Long = System.currentTimeMillis()
    private val timeToDeclareFall: Long = 10000 // Two minutes in milliseconds

    fun initialize(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }

    fun startDetection(): Flow<Boolean> {
        val listener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                // Calculate the magnitude of acceleration
                val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble())

                if (accelerationMagnitude > 9.81) {
                    movementDetectedFlow.value = true
                    lastMovementTime = System.currentTimeMillis()
                } else if (System.currentTimeMillis() - lastMovementTime >= timeToDeclareFall) {
                    movementDetectedFlow.value = false
                    println("Fall Detected")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)

        return movementDetectedFlow
    }
}