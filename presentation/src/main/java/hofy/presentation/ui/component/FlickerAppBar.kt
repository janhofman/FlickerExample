package hofy.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import hofy.presentation.R
import hofy.presentation.ui.MainState
import hofy.presentation.ui.model.PhotoVO
import hofy.presentation.ui.navigation.isDetailNavigation
import hofy.presentation.ui.navigation.isMainNavigation

@OptIn(ExperimentalUnitApi::class)
@Composable
fun FlickerAppBar(
    state: MainState,
    currentBackStackEntry: NavBackStackEntry?,
    onNavigationIconClick: () -> Unit,
    onPhotoDownloadClick: (PhotoVO) -> Unit,
    onModeChangeClick: () -> Unit,
    onShareIconClick: (PhotoVO) -> Unit,
    onSearchChanged: (String) -> Unit
) {
    SmallTopAppBar(
        title = {
            if (currentBackStackEntry.isMainNavigation()) {
                TextField(
                    value = state.query.orEmpty(),
                    onValueChange = {
                        onSearchChanged.invoke(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_search), null)
                    },
                    trailingIcon = {
                        if (!state.query.isNullOrEmpty()) {
                            Icon(
                                painterResource(R.drawable.ic_close),
                                null,
                                modifier = Modifier.clickable {
                                    onSearchChanged.invoke("")
                                })
                        }
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.search_hint),
                            fontSize = TextUnit(12f, TextUnitType.Sp),
                            lineHeight = TextUnit(12f, TextUnitType.Sp),
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.White
                    )
                )

            }
        },
        navigationIcon = {
            if (currentBackStackEntry.isDetailNavigation()) {
                Icon(
                    painterResource(R.drawable.ic_close),
                    null,
                    modifier = Modifier.padding(horizontal = 8.dp).clickable {
                        onNavigationIconClick.invoke()
                    })
            }

        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            actionIconContentColor = if (currentBackStackEntry.isDetailNavigation()
            ) {
                Color.White
            } else {
                Color.Black
            },
            navigationIconContentColor = if (currentBackStackEntry.isDetailNavigation()
            ) {
                Color.White
            } else {
                Color.Black
            }, containerColor = if (currentBackStackEntry.isDetailNavigation()
            ) {
                Color.Black
            } else {
                Color.White
            }
        ),
        actions = {
            if (currentBackStackEntry.isDetailNavigation()) {
                Icon(
                    modifier = Modifier.padding(horizontal = 8.dp).clickable {
                        val photo = state.currentDetailedPhoto
                        if (photo != null) {
                            onPhotoDownloadClick.invoke(photo)
                        }
                    },
                    painter = painterResource(
                        R.drawable.ic_download
                    ), contentDescription = null
                )
                Icon(
                    modifier = Modifier.padding(horizontal = 8.dp).clickable {
                        val photo = state.currentDetailedPhoto
                        if (photo != null) {
                            onShareIconClick.invoke(photo)
                        }
                    },
                    painter = painterResource(
                        R.drawable.ic_share
                    ), contentDescription = null
                )
            }
            if (currentBackStackEntry.isMainNavigation()) {
                Icon(
                    modifier = Modifier.padding(horizontal = 8.dp).clickable {
                        onModeChangeClick.invoke()
                    },
                    painter = painterResource(
                        if (state.grid) {
                            R.drawable.ic_continuous
                        } else {
                            R.drawable.ic_grid
                        }
                    ), contentDescription = null
                )
            }
        },
    )
}