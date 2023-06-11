import androidx.compose.material3.DropdownMenu
import androidx.compose.ui.Modifier
import com.traum.client.App
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        BrowserViewportWindow("course_paper_pg_client") {
            App(Modifier)
        }
    }
}
