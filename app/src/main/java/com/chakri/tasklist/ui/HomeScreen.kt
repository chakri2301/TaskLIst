package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chakri.tasklist.R
import com.chakri.tasklist.model.Task

@Composable
fun HomeScreen(
    tasks: List<Task>,
    searchString: String,
    onSearchStringChanged: (String) -> Unit,
    onTaskOpenedClicked: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        TextField(
            value = searchString,
            onValueChange = { newValue -> onSearchStringChanged(newValue) },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            modifier = modifier
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onTaskOpenedClicked = {onTaskOpenedClicked(task)}
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onTaskOpenedClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = task.name, style = MaterialTheme.typography.displaySmall)
                    //Text(text = timeText, style = MaterialTheme.typography.bodySmall)
                }
                Text(text = task.description, style = MaterialTheme.typography.bodyLarge)

            }
            Box {
                CircularProgressIndicator(
                    progress = { task.percentComplete.toFloat() / 100f },
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.Center)
                )
                Text(
                    text = task.percentComplete.toString() + "%",
                    modifier = Modifier.align(Alignment.Center)
                )

            }
            IconButton(
                onClick = onTaskOpenedClicked
            ) {
                Icon(
                    painter = painterResource(R.drawable.open_arrow),
                    contentDescription = "Expand this task",
                    modifier = Modifier
                        .size(40.dp)
                        .weight(0.2f, fill = false)
                )
            }
        }

    }
}

@Preview
@Composable
fun CardPreview() {
    TaskCard(
        onTaskOpenedClicked = {},
        task = Task("car", "absfvuyb efvsjd", null, 20)
    )
}