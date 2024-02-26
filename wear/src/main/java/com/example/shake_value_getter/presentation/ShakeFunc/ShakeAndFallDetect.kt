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

@Preview
@Composable
fun FallDetection(navController: NavController){
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    var AlertMessage by remember { mutableStateOf(" ") }
    var FallAlertTrigger by remember { mutableStateOf(false) }
    var movementDetected by remember {mutableStateOf(false)}
    val currentTime = System.currentTimeMillis()
    var lastMovementTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val twoMins = 1*60*1000
    var alert by remember {mutableStateOf(false)}

    DisposableEffect(Unit){
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val listener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                // Calculate the magnitude of acceleration
                val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble())
                if (accelerationMagnitude > 9.81){
                    movementDetected = true
                    AlertMessage = "Moving"
                    lastMovementTime = System.currentTimeMillis()
                } else if (System.currentTimeMillis() - lastMovementTime >= twoMins){
                    movementDetected = false
                    alert = true
                }

            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )

        sensorManager.unregisterListener(listener)

        sensorManager.registerListener(
            listener,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL

        )
        onDispose(){
            sensorManager.unregisterListener(listener)
        }
    }
    LaunchedEffect(AlertMessage, movementDetected) {
        // Update UI on the main thread when tiltText changes
        // Use LaunchedEffect to launch a coroutine on the main thread
        // and update the UI safely.
        delay (1000)
        if (alert){
            AlertMessage = "Possible fall detected are you ok?"
        }


    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = AlertMessage,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.background(Color.Black),
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun TiltTimeDisplay(navController: NavController) {
    var tiltText by remember { mutableStateOf(" ") }
    var iconVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    DisposableEffect(Unit) {

        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        val listener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                var acceleration = 10f
                var currentAcceleration = SensorManager.GRAVITY_EARTH
                var lastAcceleration = SensorManager.GRAVITY_EARTH
                // Fetching x,y,z values
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                lastAcceleration = currentAcceleration

                // Getting current accelerations
                // with the help of fetched x,y,z values
                currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta: Float = currentAcceleration - lastAcceleration
                acceleration = acceleration * 0.9f + delta
                if (acceleration > 12 ){
                    val currentDateTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("h:mm a")
                    tiltText = "Current Time: ${currentDateTime.format(formatter)}"

                    iconVisible = true

                    // Delay to make the icon reappear after a few seconds

                }


                // Display a Toast message if
                // acceleration value is over 12
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}


        }
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )

        sensorManager.unregisterListener(listener)

        sensorManager.registerListener(
            listener,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL

        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }

    }
    LaunchedEffect(tiltText) {
        // Update UI on the main thread when tiltText changes
        // Use LaunchedEffect to launch a coroutine on the main thread
        // and update the UI safely.
        delay(3000) // Adjust the delay time in milliseconds (e.g., 3000 for 3 seconds)
        tiltText = " " // Reset the text after the delay
    }
    LaunchedEffect(iconVisible) {
        delay(3000) // Adjust the delay time in milliseconds (e.g., 3000 for 3 seconds)
        iconVisible = false
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = tiltText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.background(Color.Black),
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (iconVisible) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
            }
        }
    }

}