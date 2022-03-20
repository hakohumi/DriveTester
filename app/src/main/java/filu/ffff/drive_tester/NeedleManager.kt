package filu.ffff.drive_tester

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class NeedleManager(private val sensorManager: SensorManager) : SensorEventListener {
    val sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)
    val sensor: Sensor = sensorList[0]
    var onGet: (value: Float) -> Unit = {}

    init {
        Log.d("NeedleManager", "init")

        sensorManager.registerListener(
            this, sensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    fun registerListener() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("values", "${event?.values?.map { it.toString() }}")
        if (event is SensorEvent) {
            onGet(event.values[0])
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("kdkd", "senfor: $p1")
    }
}