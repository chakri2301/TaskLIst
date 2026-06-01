package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.chakri.tasklist.model.Task
import kotlinx.coroutines.launch

private const val MAX_NAME_SIZE = 255
private const val MAX_DESCRIPTION_SIZE = 1000

@Preview(showBackground = true)
@Composable
fun NewTaskPreview() {
    NewTaskScreen(
        onCreateClicked = { taks, update -> },
        onCancelClicked = {},
    )
}


@Composable
fun NewTaskScreen(
    onCreateClicked: suspend (Task, (String) -> Unit) -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var percent by remember { mutableStateOf(0f) }
    var nameError by remember { mutableStateOf(false) }
    var guideText by remember { mutableStateOf("") }
    var deadlineDayEpoch by remember { mutableStateOf<Long?>(null) }
    var scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.weight(0.2f)
        ) {
            TextField(
                value = name,
                isError = nameError,
                onValueChange = {
                    if (name.length < MAX_NAME_SIZE) {
                        name = it
                    }
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = {
                    if (description.length < MAX_DESCRIPTION_SIZE) {
                        description = it
                    }
                },
                label = { Text("description") },
                modifier = Modifier.fillMaxWidth()
            )
            PercentComposable(percent = percent) { percent = it }
            DateEditComposable(
                updateDayEpoch = {
                    deadlineDayEpoch = it
                }
            )
        }

        Text(
            text = guideText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
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
                        onCreateClicked(
                            Task(
                                name,
                                description,
                                percent.toInt().toShort(),
                                deadline = deadlineDayEpoch
                            ),
                            { guideText = it }
                        )
                    }
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Create")
            }
        }
    }

}
