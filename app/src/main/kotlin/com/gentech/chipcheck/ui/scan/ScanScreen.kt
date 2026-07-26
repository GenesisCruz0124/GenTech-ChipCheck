package com.gentech.chipcheck.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gentech.chipcheck.ui.components.GuideOverlay

@Composable
fun ScanScreen(
    onPartNumberChosen: (String) -> Unit,
    onManualEntryRequested: () -> Unit,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    val detectedLines by viewModel.detectedLines.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            Box(modifier = Modifier.weight(1f)) {
                CameraPreview(
                    onLinesDetected = viewModel::onLinesDetected,
                    modifier = Modifier.fillMaxSize()
                )
                GuideOverlay(modifier = Modifier.fillMaxSize())
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Camera access is needed to scan chip markings.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Grant Camera Permission")
                }
            }
        }

        Surface(tonalElevation = 2.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (detectedLines.isEmpty()) {
                        "Point the camera at the chip marking"
                    } else {
                        "Tap the line that is the part number"
                    },
                    style = MaterialTheme.typography.titleSmall
                )
                if (detectedLines.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    detectedLines.forEach { line ->
                        AssistChip(
                            onClick = { onPartNumberChosen(line) },
                            label = { Text(line) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onManualEntryRequested, modifier = Modifier.fillMaxWidth()) {
                    Text("Type manually instead")
                }
            }
        }
    }
}
