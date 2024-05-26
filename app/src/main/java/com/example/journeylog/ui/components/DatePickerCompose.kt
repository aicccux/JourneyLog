package com.example.journeylog.ui.components

import android.app.DatePickerDialog
import android.text.format.DateUtils
import android.widget.DatePicker
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun DatePickerCompose(timeInMillis: Long, onChange: (time: Long) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            onChange(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    TextButton(onClick = { datePickerDialog.show() }) {
        Icon(Icons.Filled.DateRange, null)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(DateUtils.formatDateTime(context, timeInMillis, DateUtils.FORMAT_ABBREV_ALL))
    }
}