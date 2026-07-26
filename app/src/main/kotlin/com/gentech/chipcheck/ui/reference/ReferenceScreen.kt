package com.gentech.chipcheck.ui.reference

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private enum class ReferenceSection(val label: String) {
    CHIP_CODES("Chip Codes"),
    CAPACITY_LIST("Capacity List"),
    FIRMWARE_VERSIONS("Firmware Versions")
}

@Composable
fun ReferenceScreen() {
    var section by remember { mutableStateOf(ReferenceSection.CHIP_CODES) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Reference",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        TabRow(selectedTabIndex = section.ordinal) {
            ReferenceSection.entries.forEach { candidate ->
                Tab(
                    selected = section == candidate,
                    onClick = { section = candidate },
                    text = { Text(candidate.label) }
                )
            }
        }

        when (section) {
            ReferenceSection.CHIP_CODES -> ChipCodesSection(modifier = Modifier.weight(1f))
            ReferenceSection.CAPACITY_LIST -> CapacityListSection(modifier = Modifier.weight(1f))
            ReferenceSection.FIRMWARE_VERSIONS -> FirmwareVersionsSection(modifier = Modifier.weight(1f))
        }
    }
}

private data class ChipCodeRow(val segmentLabel: String, val code: String, val meaning: String)

@Composable
private fun ChipCodesSection(
    modifier: Modifier = Modifier,
    viewModel: ReferenceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.vendors.isEmpty()) return

    Column(modifier = modifier.fillMaxSize()) {
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
            segment.map.entries.map { (code, meaning) -> ChipCodeRow(segment.label, code, meaning) }
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

@Composable
private fun CapacityListSection(
    modifier: Modifier = Modifier,
    viewModel: CapacityListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.categories.isEmpty()) return

    Column(modifier = modifier.fillMaxSize()) {
        val selectedIndex = uiState.categories
            .indexOfFirst { it.label == uiState.selectedLabel }
            .coerceAtLeast(0)

        ScrollableTabRow(selectedTabIndex = selectedIndex) {
            uiState.categories.forEachIndexed { index, category ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { viewModel.selectCategory(category.label) },
                    text = { Text(category.label) }
                )
            }
        }

        val selectedCategory = uiState.categories.getOrNull(selectedIndex) ?: return@Column
        selectedCategory.note?.let { note ->
            Text(
                text = note,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            items(selectedCategory.codes) { code ->
                Text(
                    text = code,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun FirmwareVersionsSection(
    modifier: Modifier = Modifier,
    viewModel: FirmwareVersionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = uiState.query,
            onValueChange = viewModel::onQueryChanged,
            label = { Text("Search model") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        val filtered = uiState.filteredEntries
        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No matching models")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                items(filtered) { entry ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(text = entry.model, style = MaterialTheme.typography.labelMedium)
                        Text(text = entry.buildVersion, style = MaterialTheme.typography.bodyMedium)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
