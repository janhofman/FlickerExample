package hofy.presentation.ui.navigation

import androidx.navigation.NavBackStackEntry


fun NavBackStackEntry?.isDetailNavigation(): Boolean {
    return this?.destination?.route == "detail/{photoUrl}"
}

fun NavBackStackEntry?.isMainNavigation(): Boolean {
    return this?.destination?.route == "main"
}