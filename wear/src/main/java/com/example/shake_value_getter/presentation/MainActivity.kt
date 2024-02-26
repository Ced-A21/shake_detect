/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.shake_value_getter.presentation

import android.content.Context
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
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlin.math.sqrt
import android.content.Context.SENSOR_SERVICE
import androidx.navigation.NavController


import com.example.shake_value_getter.presentation.ShakeFunc.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


private var lastFallTime: Long = 0


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
        FallDetection.initialize(this)
    }
}

@Composable
@Preview
fun WearApp(){
    val navController = rememberSwipeDismissableNavController()

    var movementDetected by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val flow = FallDetection.startDetection()

        // Collect the flow in a coroutine
        scope.launch {
            flow.collect { movementDetected ->
                if (!movementDetected) {
                    navController.navigate("Fall")
                } else if (movementDetected && navController.currentDestination?.route == "Fall") {
                    navController.popBackStack()
                }
            }
        }

        // This will cancel the coroutine when the composable is disposed
        onDispose {
            scope.cancel()
        }
    }



    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "Landing"   // Start screen of the application
    ) {
        composable("Fall_Detect") {
            //LandingScreen(navController)
            FallDetection(navController)
        }
        composable("TimeCheck") {
            TiltTimeDisplay(navController)

        }
        composable("Landing"){
            HelloWorld(navController)
        }
        composable("Fall"){
            Fall(navController)
        }
//        composable("FallDetectService"){
//            FallDetectionServices(navController)
//        }
    }

}
