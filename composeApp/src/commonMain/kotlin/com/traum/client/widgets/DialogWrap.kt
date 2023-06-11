package com.traum.client.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

expect fun DialogWrap(
    onDismissRequest: () -> Unit,
    buttons: @Composable () -> Unit,
    title: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier,
    text: @Composable (() -> Unit)?,
    backgroundColor: Color = AppThemeSettings.CurrentColorScheme.surface,
    shape: Shape = RoundedCornerShape(6.dp)
)