package com.gentech.chipcheck.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gentech.chipcheck.domain.decoder.Confidence
import com.gentech.chipcheck.ui.theme.AmberUnknown
import com.gentech.chipcheck.ui.theme.AmberUnknownContainer

@Composable
fun ConfidenceBadge(confidence: Confidence, modifier: Modifier = Modifier) {
    if (confidence != Confidence.UNKNOWN) return

    Surface(
        modifier = modifier,
        color = AmberUnknownContainer,
        contentColor = AmberUnknown,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = "Unknown code",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
