package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NewTaskScreen(
    modifier:Modifier = Modifier
){
    var percent by remember{ mutableStateOf(0f) }
    Column {
        TextField(
            value = "Hi",
            onValueChange = {},
            label = { Text("Name") }
        )
        TextField(
            value = "Hi",
            onValueChange = {},
            label = { Text("description") }
        )
        Row {
            Slider(
                value = percent,
                onValueChange = { percent = it }
            )
            Text(
                text = "${percent}%",
                modifier = Modifier.weight(0.2f)
            )
        }
    }
}
@Preview
@Composable
fun NewTaskPreview(){
    NewTaskScreen()
}