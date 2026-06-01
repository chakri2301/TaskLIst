package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.chakri.tasklist.model.Task

@Composable
fun ViewTaskScreen(
    task: Task,
    onCancelClicked: () -> Unit,
    onSaveClicked: (Int) -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var finalPercent by remember { mutableFloatStateOf(task.percentComplete.toFloat()) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column {
            Text(
                text = "Name:",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = task.name,
                style = MaterialTheme.typography.bodyLarge
            )
            if (task.description != "") {
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (task.deadline != null) {
                Text(
                    text = "Deadline",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = formattedDateFromLong(task.deadline),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = "Percentage Completed:",
                style = MaterialTheme.typography.titleLarge
            )
            PercentComposable(
                percent = finalPercent,
                onPercentChange = {
                    finalPercent = it
                }
            )
            Button(
                onClick = onDeleteClicked
            ) {
                Text("Delete")
            }
        }
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onCancelClicked,
            modifier = Modifier.weight(0.5f)
        ) {
            Text("Cancel")
        }
        Button(
            onClick = { onSaveClicked((finalPercent).toInt()) },
            enabled = task.percentComplete != (finalPercent).toInt().toShort(),
            modifier = Modifier.weight(0.5f)
        ) {
            Text("Save")
        }
    }
}
