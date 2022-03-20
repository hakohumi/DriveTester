package filu.ffff.drive_tester

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import filu.ffff.drive_tester.ui.theme.DriveTesterTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    // 遅延する必要がある
    //　SensorManagerをgetSystemServiceから生成するには、アクティビティ上で呼ぶ必要がある
    private lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor
    var onGet: (value: Float) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            ?: throw IllegalStateException("センサーが見つかりませんでした。")
        Log.d("main", "sensor")

        setContent {
            DriveTesterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val angle = remember { mutableStateOf(0.0f) }
                    onGet = {
                        angle.value = it
                    }

                    UpdateNeedle(angle.value)

                    Log.d("main", "created")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)

        Log.d("dddds", "onresume")
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)

        Log.e("kdkffffffd", "onpause")
    }

    override fun onSensorChanged(event: SensorEvent?) {
//        Log.d("values", "${event?.values?.map { it.toString() }}")
        event ?: return

        onGet(event.values[0])

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.e("kdkd", "senfor: $p1")
    }
}

@Composable
fun UpdateNeedle(angle: Float) {
    Image(
        painter = painterResource(id = R.drawable.needle1),
        contentDescription = "needle", modifier = Modifier.graphicsLayer(
            rotationZ = angle
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DriveTesterTheme {

    }
}