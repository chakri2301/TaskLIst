package com.chakri.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chakri.tasklist.R
import com.chakri.tasklist.model.SortBy
import com.chakri.tasklist.model.Task

@Composable
fun HomeScreen(
    tasks: List<Task>,
    searchString: String,
    onSearchStringChanged: (String) -> Unit,
    onTaskOpenedClicked: (Int) -> Unit,
    onCreateClicked: () -> Unit,
    sortBy: SortBy,
    isAscOrder: Boolean,
    setIsAscOrder: (Boolean) -> Unit,
    setSortBy: (SortBy) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column {
            TextField(
                value = searchString,
                onValueChange = { newValue -> onSearchStringChanged(newValue) },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = { onSearchStringChanged("") }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = "clear search"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
            ) {
                if (sortBy == SortBy.none) {
                    SortBy.entries.map {
                        if (it == SortBy.none) return@map
                        FilterChip(
                            selected = false,
                            label = {
                                Text(it.name)
                            },
                            onClick = {
                                setSortBy(it)
                            }
                        )
                    }
                } else {
                    SortBy.entries.map {
                        if (sortBy == it) {
                            FilterChip(
                                selected = true,
                                label = { Text(it.name) },
                                onClick = {
                                    setSortBy(SortBy.none)
                                }
                            )
                        }
                    }
                    VerticalDivider()
                    FilterChip(
                        selected = isAscOrder,
                        onClick = {
                            if (!isAscOrder) {
                                setIsAscOrder(true)
                            }
                        },
                        label = { Text("Ascending") }
                    )
                    FilterChip(
                        selected = !isAscOrder,
                        onClick = {
                            if (isAscOrder) {
                                setIsAscOrder(false)
                            }
                        },
                        label = { Text("Descending") }
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(tasks.size) { i ->
                    TaskCard(
                        task = tasks[i],
                        onTaskOpenedClicked = { onTaskOpenedClicked(i) }
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier.size(128.dp)
                    )
                }
            }

        }
        FloatingActionButton(
            onClick = onCreateClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding(),
                    end = 4.dp
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = "Add Task"
            )
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
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.displaySmall,
                        textDecoration = if (task.percentComplete != 100.toShort()) TextDecoration.None else TextDecoration.LineThrough
                    )
                    if (task.deadline != null) {
                        Text(
                            text = formattedDateFromLong(task.deadline),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
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
        task = Task("car", "absfvuyb efvsjd", 20, null)
    )
}