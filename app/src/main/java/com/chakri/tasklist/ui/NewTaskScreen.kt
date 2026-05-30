package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.chakri.tasklist.model.Task

private const val MAX_NAME_SIZE = 255
private const val MAX_DESCRIPTION_SIZE = 1000
@Composable
fun NewTaskScreen(
    onCreateClicked: (Task, (String) -> Unit) -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var percent by remember { mutableStateOf(0f) }
    var nameError by remember { mutableStateOf(false) }
    var guideText by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.weight(0.2f)
        ) {
            TextField(
                value = name,
                isError = nameError,
                onValueChange = { if(name.length < MAX_NAME_SIZE){name = it }},
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = { if(description.length < MAX_DESCRIPTION_SIZE) {description = it} },
                label = { Text("description") },
                modifier = Modifier.fillMaxWidth()
            )
            PercentComposable(percent = percent) { percent = it }
        }
        Spacer(
            modifier = Modifier.weight(0.1f)
        )
        Text(
            text = guideText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.weight(0.1f)
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
                    onCreateClicked(
                        Task(name, description, percent.toInt().toShort(), deadline = 100),
                        { guideText = it }
                    )
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Create")
            }
        }
    }

}
