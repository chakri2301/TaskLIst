package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.chakri.tasklist.model.Task
import kotlinx.coroutines.launch

//@Preview
@Composable
fun EditTaskScreen(
    task: Task,
    onCancelClicked:()->Unit,
    onChangeClicked:suspend (Task)->Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ){
        var name by remember { mutableStateOf(task.name) }
        var description by remember { mutableStateOf(task.description) }
        var percent by remember { mutableStateOf(task.percentComplete.toFloat()) }
        var deadlineDayEpoch by remember { mutableStateOf(task.deadline) }
        val scope = rememberCoroutineScope()
        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            label = {Text("Edit Name")}
        )
        OutlinedTextField(
            value = description,
            onValueChange = {description= it},
            label = {Text("Edit Description")}
        )
        PercentComposable(
            percent = percent,
            onPercentChange = {percent = it}
        )
        DateEditComposable(
            updateDayEpoch = {deadlineDayEpoch= it}
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onCancelClicked,
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    scope.launch {
                        onChangeClicked(
                            Task(
                                name,
                                description,
                                percent.toInt().toShort(),
                                deadline = deadlineDayEpoch
                            )
                        )
                    }
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Change")
            }
        }
    }
}