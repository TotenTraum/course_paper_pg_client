import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.traum.client.App
import com.traum.client.Config
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.WindowClose
import compose.icons.fontawesomeicons.regular.WindowMaximize
import compose.icons.fontawesomeicons.regular.WindowMinimize
import compose.icons.fontawesomeicons.regular.WindowRestore

fun main() = application {
    Config

    val windowState = rememberWindowState(
        width = Dp.Unspecified,
        height = Dp.Unspecified,
        placement = WindowPlacement.Maximized
    )
    var isVisible by remember { mutableStateOf(true) }

    Window(
        title = "course_paper_pg_client",
        state = windowState,
        visible = isVisible,
        onCloseRequest = ::exitApplication,
        undecorated = true
    ) {
        App(Modifier.padding(top = 36.dp)) {
            AppWindowTitleBar(
                name = "Система для кафе",
                onClose = this@application::exitApplication,
                state = windowState,
                onMaximize = {
                    if (windowState.placement == WindowPlacement.Floating)
                        windowState.placement = WindowPlacement.Maximized
                    else
                        windowState.placement = WindowPlacement.Floating
                },
                onHide = { isVisible = false })
        }
    }
}

@Composable
private fun WindowScope.AppWindowTitleBar(
    name: String,
    onClose: () -> Unit,
    onHide: () -> Unit,
    state: WindowState,
    onMaximize: () -> Unit
) = WindowDraggableArea {
    Row(
        Modifier.fillMaxWidth().height(36.dp).padding(start = 12.dp, end = 12.dp, top = 6.dp),
    ) {
        Row(Modifier.fillMaxWidth(0.5f)) {
            Text(name)
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = onHide) {
                Icon(
                    imageVector = FontAwesomeIcons.Regular.WindowMinimize,
                    contentDescription = "",
                    tint = if (AppThemeSettings.isDark.value) Color.White else Color.Black
                )
            }
            IconButton(onClick = onMaximize) {
                Icon(
                    imageVector = if (state.placement == WindowPlacement.Floating)
                        FontAwesomeIcons.Regular.WindowMaximize
                    else
                        FontAwesomeIcons.Regular.WindowRestore,
                    contentDescription = "",
                    tint = if (AppThemeSettings.isDark.value) Color.White else Color.Black
                )
            }
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = FontAwesomeIcons.Regular.WindowClose,
                    contentDescription = "",
                    tint = if (AppThemeSettings.isDark.value) Color.White else Color.Black
                )
            }
        }
    }
}