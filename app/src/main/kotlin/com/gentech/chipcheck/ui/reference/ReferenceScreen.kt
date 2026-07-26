package com.gentech.chipcheck.ui.reference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private data class ReferenceRow(val segmentLabel: String, val code: String, val meaning: String)

@Composable
fun ReferenceScreen(viewModel: ReferenceViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Reference",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        if (uiState.vendors.isEmpty()) return@Column

        val selectedIndex = uiState.vendors
            .indexOfFirst { it.vendor == uiState.selectedVendor }
            .coerceAtLeast(0)

        ScrollableTabRow(selectedTabIndex = selectedIndex) {
            uiState.vendors.forEachIndexed { index, rule ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { viewModel.selectVendor(rule.vendor) },
                    text = { Text(rule.vendor) }
                )
            }
        }

        val selectedRule = uiState.vendors.getOrNull(selectedIndex) ?: return@Column
        val rows = selectedRule.segments.flatMap { segment ->
            segment.map.entries.map { (code, meaning) -> ReferenceRow(segment.label, code, meaning) }
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            items(rows) { row ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(text = row.segmentLabel, style = MaterialTheme.typography.labelMedium)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = row.code,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Text(text = row.meaning, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                HorizontalDivider()
            }
        }
    }
}
