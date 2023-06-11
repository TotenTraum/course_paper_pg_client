package com.traum.client.widgets

import AppThemeSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ComboBox(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    selectedText: @Composable () -> Unit,
    dropDownContent: @Composable () -> Unit
) {
    Box(modifier) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAccept, colors = ButtonDefaults.buttonColors(
                containerColor = AppThemeSettings.CurrentColorScheme.surfaceColorAtElevation(3.dp),
                contentColor = AppThemeSettings.CurrentColorScheme.secondary
            ),
            shape = RectangleShape
        ) {
            selectedText()
        }
        DropdownMenuWrap(
            modifier = Modifier.background(AppThemeSettings.CurrentColorScheme.background),
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            dropDownContent()
        }
    }
}

@Composable
fun ComboIconButtonBox(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    selectedIcon: @Composable () -> Unit,
    dropDownContent: @Composable () -> Unit
) {
    Box(modifier) {
        OutlinedIconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAccept, colors = IconButtonDefaults.iconButtonColors(
                containerColor = AppThemeSettings.CurrentColorScheme.surfaceColorAtElevation(3.dp),
                contentColor = AppThemeSettings.CurrentColorScheme.secondary
            ),
            shape = RectangleShape
        ) {
            selectedIcon()
        }
        DropdownMenuWrap(
            modifier = Modifier.background(AppThemeSettings.CurrentColorScheme.background),
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            dropDownContent()
        }
    }
}

@Composable
fun ComboIconBox(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    selectedIcon: @Composable () -> Unit,
    dropDownContent: @Composable () -> Unit
) {
    Box(modifier) {
        selectedIcon()
        DropdownMenuWrap(
            modifier = Modifier.background(AppThemeSettings.CurrentColorScheme.background),
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            dropDownContent()
        }
    }
}