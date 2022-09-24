package hofy.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hofy.presentation.R
import hofy.presentation.ui.MainState

@Composable
fun BottomInfoComponent(state: MainState) {
    val imageDownloadingState = state.imageDownloadingState
    val text = when {
        imageDownloadingState?.imageDownloading == true -> {
            stringResource(R.string.downloading)
        }
        imageDownloadingState != null && imageDownloadingState.imageDownloadSuccess -> {
            stringResource(R.string.download_success)
        }
        imageDownloadingState != null && !imageDownloadingState.imageDownloadSuccess -> {
            stringResource(R.string.download_error)
        }
        else -> {
            null
        }
    }
    if (text != null) {
        Text(
            text,
            modifier = Modifier.fillMaxWidth()
                .background(
                    Color.Black
                ).padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }

}