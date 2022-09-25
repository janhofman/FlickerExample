package hofy.presentation.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hofy.presentation.ui.component.GridPhotoItem
import hofy.presentation.ui.component.LargePhotoItem
import hofy.presentation.ui.model.PhotoVO

@Composable
fun PhotoDashboard(
    items: List<PhotoVO>,
    onImageClick: (PhotoVO) -> Unit,
    grid: Boolean,
    onShareClick: (PhotoVO) -> Unit,
    onDownloadClick: (PhotoVO) -> Unit,
    onFavouriteClick: (PhotoVO) -> Unit
) {
    if (grid) {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(items) {
                GridPhotoItem(photoVO = it, onImageClick)
            }
        }
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            items.forEach {
                item {
                    LargePhotoItem(
                        it,
                        onImageClick,
                        onShareClick,
                        onDownloadClick,
                        onFavouriteClick
                    )
                }
            }
        }
    }

}