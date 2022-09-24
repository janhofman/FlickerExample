package hofy.presentation.ui.component.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import hofy.presentation.ui.component.base.ZoomableImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoDetailScreen(url: String) {
    val painter = rememberAsyncImagePainter(url)
    ZoomableImage(
        painter,
        isRotation = false,
        modifier = Modifier.fillMaxSize().background(Color.Black)
    )
}