import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

fun swapAppColor(value: Boolean? = null) {
    if (value == null)
        AppThemeSettings.isDark.value = !AppThemeSettings.isDark.value
    else
        AppThemeSettings.isDark.value = value

//    val conf = Config.getConfig<AppInfo>("configApp.json")
//    conf.isDark = AppThemeSettings.isDark.value
//    Config.setConfig("configApp.json", conf)
}

object AppThemeSettings {
    val darkColorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = darkColorScheme()

    val lightColorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = lightColorScheme()

    val isDark: MutableState<Boolean> = mutableStateOf(true)
    val _currentColorScheme: MutableState<ColorScheme?> = mutableStateOf(null)

    var CurrentColorScheme: ColorScheme
        get() = _currentColorScheme.value!!
        set(value) {
            _currentColorScheme.value = value
        }

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

@Composable
fun AppTheme(
    colorScheme: ColorScheme? = null,
    shapes: Shapes = AppThemeSettings.shapes,
    typography: Typography = AppThemeSettings.typography,
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable () -> Unit
) {
    val isDark = remember { AppThemeSettings.isDark }
    val current = remember { AppThemeSettings._currentColorScheme }
    current.value = colorScheme
        ?: if (isDark.value)
            AppThemeSettings.darkColorScheme
        else
            AppThemeSettings.lightColorScheme
    MaterialTheme(current.value!!, shapes, typography) {
        Surface(modifier) {
            content()
        }
    }
}