package com.gentech.chipcheck.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gentech.chipcheck.domain.repository.ThemeMode
import com.gentech.chipcheck.domain.repository.UnmappedCodeItem

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val unmappedCodes by viewModel.unmappedCodes.collectAsStateWithLifecycle()
    var showClearHistoryConfirm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Theme", style = MaterialTheme.typography.titleMedium)
        ThemeMode.entries.forEach { mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(selected = themeMode == mode, onClick = { viewModel.setThemeMode(mode) })
            ) {
                RadioButton(selected = themeMode == mode, onClick = { viewModel.setThemeMode(mode) })
                Text(
                    text = mode.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { showClearHistoryConfirm = true }) {
            Text("Clear History")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Unmapped Codes", style = MaterialTheme.typography.titleMedium)
        if (unmappedCodes.isEmpty()) {
            Text(text = "No unmapped codes reported yet.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(unmappedCodes, key = { it.id }) { code ->
                    UnmappedCodeRow(
                        code = code,
                        onToggleReviewed = { viewModel.markUnmappedCodeReviewed(code.id, !code.reviewed) },
                        onDelete = { viewModel.deleteUnmappedCode(code.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showClearHistoryConfirm) {
        AlertDialog(
            onDismissRequest = { showClearHistoryConfirm = false },
            title = { Text("Clear history?") },
            text = { Text("This deletes all saved scan history. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearHistory()
                    showClearHistoryConfirm = false
                }) { Text("Clear") }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun UnmappedCodeRow(
    code: UnmappedCodeItem,
    onToggleReviewed: () -> Unit,
    onDelete: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${code.vendorGuess ?: "Unknown vendor"} · ${code.segmentName ?: "whole code"}",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "${code.rawCode} (from ${code.fullPartNumber})",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        TextButton(onClick = onToggleReviewed) {
            Text(if (code.reviewed) "Reviewed" else "Mark reviewed")
        }
        TextButton(onClick = onDelete) {
            Text("Delete")
        }
    }
}
