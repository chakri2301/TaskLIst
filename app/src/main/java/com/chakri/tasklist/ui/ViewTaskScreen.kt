package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chakri.tasklist.model.Task

@Composable
fun ViewTaskScreen(
    task: Task,
    onCancelClicked:()->Unit,
    onSaveClicked:(Int)->Unit
) {
    var finalPercent by remember { mutableStateOf(task.percentComplete.toFloat()) }
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
        ){
        Column {
            Text(
                text = "Name:",
                style= MaterialTheme.typography.titleLarge
            )
            Text(
                text = task.name,
                style= MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Description:",
                style= MaterialTheme.typography.titleLarge
            )
            Text(
                text = task.description,
                style= MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Percentage Completed:",
                style= MaterialTheme.typography.titleLarge
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    steps = 9,
                    valueRange = 0f..100f,
                    value = finalPercent,
                    onValueChange = {
                        finalPercent = it
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = "${(finalPercent).toInt()}%",
                    modifier = Modifier.weight(0.2f, fill = false)
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()){
            OutlinedButton(
                onClick = onCancelClicked,
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {onSaveClicked((finalPercent).toInt())},
                enabled = task.percentComplete != (finalPercent).toInt(),
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Save")
            }
        }
    }
}
