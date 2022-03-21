package filu.ffff.drive_tester

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import kotlin.math.abs


@Composable
fun LimitedMeter(value: Double, modifier: Modifier = Modifier) {
    val dynamicRange = remember { mutableStateOf(0.0) }

    val tempValue = value

    if (dynamicRange.value < abs(value)) {
        dynamicRange.value = abs(value)
    }

    // 入力されたvalueをleftMaxとrightMaxの間を-90 ~ 90に正規化したい。
    val leftMax = -dynamicRange.value
    val rightMax = dynamicRange.value



    Log.d("LimitedMeter: value", "$value")
    Log.d("LimitedMeter: tempValue", "$tempValue")

    val normalizedValue: Double =
        (((tempValue - leftMax) / (rightMax - leftMax)) * (90 - (-90))) + (-90)

    Log.d("normalizedValue", "${(tempValue - leftMax) / (rightMax - leftMax)}")

    Image(
        painter = painterResource(id = R.drawable.needle),
        contentDescription = "needle",
        modifier = modifier
            .graphicsLayer(
                rotationZ = normalizedValue.toFloat()
            )
            .fillMaxSize()
    )
}

@Composable
fun NormalMeter(value: Double, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.needle),
        contentDescription = "needle",
        modifier = modifier
            .graphicsLayer(
                rotationZ = value.toFloat()
            )
            .fillMaxSize()
    )
}