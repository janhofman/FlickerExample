package hofy.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import hofy.presentation.ui.component.base.AppImage
import hofy.presentation.ui.model.PhotoVO

@Composable
fun GridPhotoItem(photoVO: PhotoVO, onImageClick: (PhotoVO) -> Unit) {
    AppImage(
        photoVO.url, modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable {
            onImageClick.invoke(photoVO)
        },
        contentScale = ContentScale.Crop
    )
}