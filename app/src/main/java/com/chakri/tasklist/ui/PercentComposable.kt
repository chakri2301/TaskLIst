package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PercentComposable(
    percent: Float,
    onPercentChange:(Float)-> Unit
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            steps = 9,
            valueRange = 0f..100f,
            value = percent,
            onValueChange = {onPercentChange(it)},
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Text(
            text = "${percent.toInt()}%",
            modifier = Modifier.weight(0.2f, fill = false)
        )
    }
}