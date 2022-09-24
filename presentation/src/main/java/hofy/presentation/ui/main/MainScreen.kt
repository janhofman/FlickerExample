package hofy.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import hofy.presentation.ui.MainState
import hofy.presentation.ui.component.TagsComponent
import hofy.presentation.ui.component.screen.EmptyScreen
import hofy.presentation.ui.component.screen.ErrorScreen
import hofy.presentation.ui.component.screen.LoadingScreen
import hofy.presentation.ui.model.PhotoVO
import hofy.presentation.ui.model.TagVO

@Composable
fun MainScreen(
    state: MainState,
    onImageClick: (PhotoVO) -> Unit,
    onShareClick: (PhotoVO) -> Unit,
    onDownloadClick: (PhotoVO) -> Unit,
    onRefreshRequested: () -> Unit,
    onTagClick: (TagVO) -> Unit,
    onFavouriteClick: (PhotoVO) -> Unit,
) {
    SwipeRefresh(
        rememberSwipeRefreshState(state.loading),
        onRefresh = { onRefreshRequested.invoke() }, indicator = { swipeState, trigger ->
            if (swipeState.isSwipeInProgress) {
                SwipeRefreshIndicator(swipeState, trigger)
            }
        }) {
        Column(Modifier.fillMaxSize()) {
            when {
                state.loading -> {
                    LoadingScreen()
                }
                state.empty -> {
                    TagsComponent(state.tags, onTagClick)
                    EmptyScreen()
                }
                state.error != null -> {
                    ErrorScreen()
                }
                state.photos != null -> {
                    TagsComponent(state.tags, onTagClick)
                    PhotoGrid(
                        items = state.photos,
                        onImageClick,
                        state.grid,
                        onShareClick,
                        onDownloadClick,
                        onFavouriteClick
                    )
                }
            }
        }
    }
}