package hofy.presentation.ui.navigation

import androidx.navigation.NavBackStackEntry
import hofy.presentation.ui.main.NavigationItem


fun NavBackStackEntry?.isDetailNavigation(): Boolean {
    return this?.destination?.route == NavigationItem.Detail.path
}

fun NavBackStackEntry?.isFavouritesNavigation(): Boolean {
    return this?.destination?.route == NavigationItem.Favourites.path
}

fun NavBackStackEntry?.isMainNavigation(): Boolean {
    return this?.destination?.route == NavigationItem.Main.path
}