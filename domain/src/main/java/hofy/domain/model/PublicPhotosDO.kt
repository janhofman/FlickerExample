package hofy.domain.model

import java.util.*

data class PublicPhotosDO(val items: List<PhotoDO>)

data class PhotoDO(
    val title: String? = null,
    val link: String? = null,
    val media: PhotoMediaDO? = null,
    val dateTaken: Date? = null,
    val description: String? = null,
    val published: String? = null,
    val author: String? = null,
    val tags: List<TagDO>? = null
)

data class PhotoMediaDO(
    val m: String?
)

