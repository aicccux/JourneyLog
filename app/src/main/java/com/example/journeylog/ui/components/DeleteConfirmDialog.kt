package com.example.journeylog.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.journeylog.database.Log
import com.example.journeylog.ui.theme.DeleteColor

@Composable
fun DeleteConfirmDialog(
    onDismiss: () -> Unit,
    log: Log,
    onDelete: (log: Log) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onDelete(log) }, colors = ButtonDefaults.buttonColors(DeleteColor)) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        icon = {
            Icon(Icons.Outlined.Delete, contentDescription = null)
        },
        title = { Text(text = "Delete Confirm") },
        text = { Text(text = "This Log will be deleted and can not be recovered.") }
    )
}