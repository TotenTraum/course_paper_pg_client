package com.traum.client.widgets

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun DialogWrap(
    onDismissRequest: () -> Unit,
    buttons: @Composable () -> Unit,
    title: @Composable (() -> Unit)?,
    modifier: Modifier,
    text: @Composable (() -> Unit)?,
    backgroundColor: Color,
    shape: Shape
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        buttons = buttons,
        title = title,
        text = text,
        backgroundColor = backgroundColor,
        shape = shape
    )
}