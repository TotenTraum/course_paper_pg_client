import androidx.compose.ui.window.ComposeUIViewController
import com.traum.client.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}
