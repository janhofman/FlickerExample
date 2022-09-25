package hofy.presentation.ui.main

import hofy.presentation.ui.model.PhotoVO
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val ARG_PHOTO_URL = "photoUrl"

sealed class NavigationItem(val path: String) {
    object Main : NavigationItem("main")
    object Favourites : NavigationItem("favourites")
    object Detail : NavigationItem("detail/{$ARG_PHOTO_URL}") {
        fun build(photoVO: PhotoVO): String {
            return path.replace(
                "{$ARG_PHOTO_URL}", URLEncoder.encode(
                    photoVO.url,
                    StandardCharsets.UTF_8.toString()
                )
            )
        }
    }
}