package com.chakri.tasklist.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset


//Use
//https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
//https://developer.android.com/reference/java/time/format/DateTimeFormatter
//https://developer.android.com/reference/kotlin/androidx/compose/material3/DatePickerDialog.composable
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DateEditComposable(
    updateDayEpoch: (Long?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var datePickerState = rememberDatePickerState()
    var dayEpoch by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }
    Row {
        Button(
            onClick = { expanded = true }
        ) {
            if (dayEpoch == null) {
                Text("Add deadline")
            } else {
                Text(formattedDateFromLong(dayEpoch!!))
            }
        }
    }
    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false }
        ) {
            Column {
                DatePicker(
                    state = datePickerState,
                    dateFormatter = DatePickerDefaults
                        .dateFormatter(),
                    modifier = modifier
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            dayEpoch = null
                            expanded = false
                        }
                    ) {
                        Text("Remove Deadline")
                    }
                    Button(
                        onClick = {
                            expanded = false
                            val date = datePickerState.getSelectedDate()
                            dayEpoch = date?.toEpochDay()
                            updateDayEpoch(dayEpoch)

                        }
                    ) {
                        Text("Add Deadline")
                    }
                }
            }
        }
    }
}

fun formattedDateFromLong(dayEpoch: Long): String {
    val datetime = LocalDate.ofEpochDay(dayEpoch)
    return "${datetime.dayOfWeek} ${datetime.dayOfMonth} ${datetime.month} ${datetime.year}"
}