package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chakri.tasklist.model.Task

@Composable
fun ViewTaskScreen(
    task: Task,
    onCancelClicked: () -> Unit,
    onSaveClicked: (Int) -> Unit,
    onDeleteClicked: () -> Unit,
    onEditClicked:()-> Unit,
    modifier: Modifier = Modifier
) {
    var finalPercent by remember { mutableFloatStateOf(task.percentComplete.toFloat()) }
    var isDeleteDialog by remember { mutableStateOf(false) }
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
                onClick = { isDeleteDialog = true }
            ) {
                Text("Delete")
            }
            Button(
                onClick = onEditClicked
            ){
                Text("Edit")
            }
            if (isDeleteDialog) {
                Dialog(
                    onDismissRequest = { isDeleteDialog = false }
                ) {
                    Card(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Text(
                                text = "Do You want to delete ${task.name}"
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        isDeleteDialog = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                                Button(
                                    onClick = onDeleteClicked
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
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
}
