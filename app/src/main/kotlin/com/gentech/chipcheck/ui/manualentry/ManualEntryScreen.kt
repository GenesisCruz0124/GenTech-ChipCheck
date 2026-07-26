package com.gentech.chipcheck.ui.manualentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gentech.chipcheck.ui.components.SegmentCard

@Composable
fun ManualEntryScreen(
    onViewResult: (String) -> Unit,
    viewModel: ManualEntryViewModel = hiltViewModel()
) {
    val input by viewModel.input.collectAsStateWithLifecycle()
    val decodedChip by viewModel.decodedChip.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Manual Entry", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = input,
            onValueChange = viewModel::onInputChanged,
            label = { Text("Part number") },
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val chip = decodedChip
        if (chip != null) {
            Text(
                text = "Vendor: ${chip.vendor ?: "Unrecognized"}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(chip.segments) { segment ->
                    SegmentCard(segment = segment, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onViewResult(input) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Full Result")
            }
        }
    }
}
