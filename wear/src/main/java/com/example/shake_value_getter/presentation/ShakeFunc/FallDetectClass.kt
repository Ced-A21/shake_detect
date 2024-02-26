package com.example.shake_value_getter.presentation.ShakeFunc

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlin.math.sqrt

class FallDetectClass{
    var AlertMessage by mutableStateOf(" ")
    var FallAlertTrigger by mutableStateOf(false)
    var movementDetected by mutableStateOf(false)
    val currentTime = System.currentTimeMillis()
    var lastMovementTime by mutableStateOf(System.currentTimeMillis())
    val twoMins = 10000
    var alert by mutableStateOf(false)

    @Composable
    fun StartDetection() {
        val sensorManager = LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val listener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                // Calculate the magnitude of acceleration
                val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble())
                if (accelerationMagnitude > 9.81) {
                    movementDetected = true
                    AlertMessage = "Moving"
                    lastMovementTime = System.currentTimeMillis()
                } else if (System.currentTimeMillis() - lastMovementTime >= twoMins) {
                    movementDetected = false
                    alert = true
                }

            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
        sensorManager.registerListener(
            listener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

}


