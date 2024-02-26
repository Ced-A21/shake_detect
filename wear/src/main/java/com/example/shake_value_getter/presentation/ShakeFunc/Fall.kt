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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shake_value_getter.R
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlin.math.sqrt


@Composable
fun Fall(navController: NavController){
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    var AlertMessage by remember { mutableStateOf(" ") }
    var AlertMessage2 by remember { mutableStateOf(" ")}
    var AlertMessage3 by remember { mutableStateOf(" ") }
    var AskPrompt by remember { mutableStateOf(" ") }

    var alert by remember {mutableStateOf(false)}
    val effectKey = remember { mutableStateOf(0) }

    LaunchedEffect(sensorManager, context) {

        // Update UI on the main thread when tiltText changes
        // Use LaunchedEffect to launch a coroutine on the main thread
        // and update the UI safely.

        AlertMessage = "Alert"
        AlertMessage2 = "No Movement detected"
        AlertMessage3 = "Possible fall occurred!!!"
        AskPrompt ="Shake watch to confirm"
        alert = true
        if (alert){
            delay(1000) // Simulating a long-running operation
            effectKey.value++
        }

    }

//    DisposableEffect(Unit) {
//
//        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//
//
//        val listener: SensorEventListener = object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent) {
//                var acceleration = 10f
//                var currentAcceleration = SensorManager.GRAVITY_EARTH
//                var lastAcceleration = SensorManager.GRAVITY_EARTH
//                // Fetching x,y,z values
//                val x = event.values[0]
//                val y = event.values[1]
//                val z = event.values[2]
//                lastAcceleration = currentAcceleration
//
//                // Getting current accelerations
//                // with the help of fetched x,y,z values
//                currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
//                val delta: Float = currentAcceleration - lastAcceleration
//                acceleration = acceleration * 0.9f + delta
//                if (acceleration > 12 ){
//                    // Delay to make the icon reappear after a few seconds
//                    navController.popBackStack()
//                }
//
//
//                // Display a Toast message if
//                // acceleration value is over 12
//            }
//            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
//
//
//        }
//        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(
//            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
//        )
//
//        sensorManager.unregisterListener(listener)
//
//        sensorManager.registerListener(
//            listener,
//            accelerometerSensor,
//            SensorManager.SENSOR_DELAY_NORMAL
//
//        )
//
//        onDispose {
//            sensorManager.unregisterListener(listener)
//        }
//
//    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.alert),
                contentDescription = "null",
                modifier = Modifier
                    .size(40.dp),
                colorFilter = ColorFilter.tint(Color(red = 255, blue = 0, green = 0))
            )
            Text(
                text = AlertMessage,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.background(Color.Black)
                    .align(alignment = Alignment.CenterHorizontally),
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = AlertMessage2,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.background(Color.Black).align(alignment = Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = AskPrompt,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.background(Color.Black).align(alignment = Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                color = Color.White,
            )
//            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}