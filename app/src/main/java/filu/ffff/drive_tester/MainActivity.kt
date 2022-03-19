package filu.ffff.drive_tester

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.*
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import filu.ffff.drive_tester.ui.theme.DriveTesterTheme

class MainActivity : ComponentActivity(), SensorEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DriveTesterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")

                    val sensorManager = LocalContext.current.getSystemService(SENSOR_SERVICE) as SensorManager
                    val b = sensorManager.getSensorList(TYPE_ACCELEROMETER)
                    Log.d("tetst", "$b")
                    val s: Sensor = b[0]
                    Log.d("tedds", "$s")
                    sensorManager.registerListener(this, s, SENSOR_DELAY_FASTEST)
                }
            }
        }
    }

    @Composable
    fun myFunction(){

    }

    override fun onSensorChanged(p0: SensorEvent?) {
//        Log.d("acc", "${p0?.accuracy} ")
//        Log.d("sensor",        "${p0?.sensor} " )
        Log.d("values","${p0?.values?.map{it.toString()}}")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        Log.d("kdkd", "senfor: $Int")

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DriveTesterTheme {
        Greeting("Android")
    }
}