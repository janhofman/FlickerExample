package hofy.presentation.ui.component.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import hofy.presentation.ui.MainState
import hofy.presentation.ui.component.TagsComponent
import hofy.presentation.ui.main.PhotoDashboard
import hofy.presentation.ui.model.PhotoVO
import hofy.presentation.ui.model.TagItem

@Composable
fun DashboardScreen(
    state: MainState,
    onImageClick: (PhotoVO) -> Unit,
    onShareClick: (PhotoVO) -> Unit,
    onDownloadClick: (PhotoVO) -> Unit,
    onRefreshRequested: () -> Unit,
    onTagClick: (TagItem.TagVO) -> Unit,
    onFavouriteClick: (PhotoVO) -> Unit,
    onCancelClick: () -> Unit
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
                    TagsComponent(state.tags, onTagClick, onCancelClick)
                    EmptyScreen()
                }
                state.error != null -> {
                    ErrorScreen()
                }
                state.photos != null -> {
                    TagsComponent(state.tags, onTagClick, onCancelClick)
                    PhotoDashboard(
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