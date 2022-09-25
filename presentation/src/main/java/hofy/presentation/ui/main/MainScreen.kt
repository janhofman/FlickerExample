package hofy.presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hofy.presentation.ui.MainState
import hofy.presentation.ui.component.BottomInfoComponent
import hofy.presentation.ui.component.screen.DashboardScreen
import hofy.presentation.ui.component.screen.FavouritesScreen
import hofy.presentation.ui.component.screen.PhotoDetailScreen
import hofy.presentation.ui.model.PhotoVO
import hofy.presentation.ui.model.TagItem
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun MainScreen(
    navController: NavHostController,
    state: MainState,
    onImageClick: (PhotoVO) -> Unit,
    onShareClick: (PhotoVO) -> Unit,
    onDownloadClick: (PhotoVO) -> Unit,
    onRefreshRequested: () -> Unit,
    onTagClick: (TagItem.TagVO) -> Unit,
    onFavouriteClick: (PhotoVO) -> Unit,
    onCancelClick: () -> Unit
) {
    var padding by remember {
        mutableStateOf(56.dp)
    }
    Box(
        Modifier.fillMaxSize().padding(bottom = padding),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = NavigationItem.Main.path
            ) {
                composable(NavigationItem.Main.path) {
                    padding = 56.dp
                    DashboardScreen(
                        state,
                        onImageClick,
                        onShareClick,
                        onDownloadClick,
                        onRefreshRequested,
                        onTagClick,
                        onFavouriteClick,
                        onCancelClick
                    )
                }
                composable(NavigationItem.Detail.path) { navBackStackEntry ->
                    padding = 0.dp
                    PhotoDetailScreen(
                        URLDecoder.decode(
                            navBackStackEntry.arguments?.getString(
                                ARG_PHOTO_URL
                            ).orEmpty(), StandardCharsets.UTF_8.toString()
                        )
                    )
                }
                composable(NavigationItem.Favourites.path) {
                    padding = 56.dp
                    FavouritesScreen()
                }
            }
        }
        BottomInfoComponent(state)
    }
}