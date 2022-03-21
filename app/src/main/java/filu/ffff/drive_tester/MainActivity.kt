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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import filu.ffff.drive_tester.ui.theme.DriveTesterTheme
import kotlin.math.PI

class MainActivity : ComponentActivity(), SensorEventListener {
    // 遅延する必要がある
    //　SensorManagerをgetSystemServiceから生成するには、アクティビティ上で呼ぶ必要がある
    private lateinit var sensorManager: SensorManager
    private lateinit var accSensor: Sensor
    private lateinit var gyroSensor: Sensor
    var onGetAcc: (value: Float) -> Unit = {}
    var onGetGyro: (value: Float, timestamp: Long) -> Unit = { _, _ -> }

    var timestamp = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            ?: throw IllegalStateException("加速度センサーが見つかりませんでした。")
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            ?: throw IllegalStateException("ジャイロセンサーが見つかりませんでした。")
        Log.d("main", "sensor")

        setContent {
            DriveTesterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {


                    val accAngle = remember { mutableStateOf(0.0f) }
                    onGetAcc = {
                        accAngle.value = it
                    }


                    val gyroTotal = remember { mutableStateOf(0.0) }
                    var preRad = 0.0f

                    val NS2S = 1.0f / 1000000000.0f

                    onGetGyro = { value, _timestamp ->
                        // y軸周りの回転速度
                        // rad/s
                        // 取得間隔で割る必要がある
                        Log.d("timestamp", "$_timestamp")

                        if (timestamp != 0L) {
                            val deltaTime = (_timestamp - timestamp) * NS2S
                            Log.d("deltaTime", "${(_timestamp - timestamp)}")
                            Log.d("timestamp", "${timestamp}")
                            Log.d("NS2S", "$NS2S")
                            val rad = (preRad + -value) * deltaTime / 2
                            Log.d("rad", "$rad")

                            val degree = (rad * (180.0 / PI))
                            Log.d("degree", "$degree")
                            gyroTotal.value += degree

                            preRad = -value
                        }
                        timestamp = _timestamp
                    }

                    val padding = 16.dp
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxHeight()
//                            .fillMaxWidth()
//                            .fillMaxSize()
                    ) {
                        Text(text = "Acc: ${accAngle.value}")
                        LimitedMeter(accAngle.value.toDouble(), modifier = Modifier.weight(1f))

                        Text(text = "Gyro: ${gyroTotal.value}")
                        LimitedMeter(gyroTotal.value, modifier = Modifier.weight(1f))
                        Button({
                            accAngle.value = 0.0f
                            gyroTotal.value = 0.0
                        }) {
                            Text(text = "Reset")
                        }
                    }
//                    Log.d("main", "created")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_UI)

        Log.d("dddds", "onresume")
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)

        Log.e("kdkffffffd", "onpause")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

//        Log.d("sensor", "${event.sensor.type}: ${event.values.map { it.toString() }}")

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                // X軸方向の加速力
                onGetAcc(event.values[0])
            }
            Sensor.TYPE_GYROSCOPE -> {
                onGetGyro(event.values[2], event.timestamp)
            }
            else -> {
                // なにもしない
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.e("kdkd", "senfor: $p1")
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DriveTesterTheme {

    }
}