package com.gentech.chipcheck.ui.result

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gentech.chipcheck.domain.decoder.Confidence
import com.gentech.chipcheck.ui.components.SegmentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    onBack: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var lastIsSaved by remember { mutableStateOf(uiState.isSaved) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved && !lastIsSaved) {
            snackbarHostState.showSnackbar("Saved to history")
        }
        lastIsSaved = uiState.isSaved
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Result") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        val chip = uiState.decodedChip
        if (chip == null) {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
                Text("Decoding...")
            }
            return@Scaffold
        }

        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Text(text = "Part No: ${chip.normalizedPartNumber}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Vendor: ${chip.vendor ?: "Unrecognized"}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(chip.segments) { segment ->
                    Column {
                        SegmentCard(segment = segment)
                        if (segment.confidence == Confidence.UNKNOWN) {
                            val alreadyReported = segment.name in uiState.reportedSegmentNames
                            TextButton(
                                onClick = { viewModel.reportUnknownSegment(segment) },
                                enabled = !alreadyReported
                            ) {
                                Text(if (alreadyReported) "Reported" else "Report unknown code")
                            }
                        }
                    }
                }
                chip.dateCodeHint?.let { hint ->
                    item {
                        Text(
                            text = "Possible date code: ${hint.yywwReading} or ${hint.wwyyReading}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.saveToHistory() },
                    enabled = !uiState.isSaved,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (uiState.isSaved) "Saved" else "Save")
                }
                OutlinedButton(
                    onClick = {
                        viewModel.shareText()?.let { clipboardManager.setText(AnnotatedString(it)) }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Copy")
                }
                OutlinedButton(
                    onClick = {
                        val text = viewModel.shareText() ?: return@OutlinedButton
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, text)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share chip check result"))
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Share")
                }
            }
        }
    }
}
