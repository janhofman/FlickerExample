package hofy.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import hofy.data.local.util.readable
import hofy.data.repository.PURPLE
import hofy.domain.model.PhotoDO
import hofy.domain.model.PhotoMediaDO
import hofy.domain.model.PublicPhotosDO
import hofy.domain.model.TagDO
import java.util.*


@JsonClass(generateAdapter = true)
data class PublicPhotos(
    @Json(name = "title")
    val title: String?,
    @Json(name = "link")
    val link: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "modified")
    val modified: Date?,
    @Json(name = "generator")
    val generator: String?,
    @Json(name = "items")
    val items: List<Photo?>?
) {
    fun toDO(all: List<TagDO>): PublicPhotosDO {
        return PublicPhotosDO((items?.filterNotNull()?.map {
            PhotoDO(
                it.title,
                it.link,
                PhotoMediaDO(it.media?.m?.replace("m.jpg", "b.jpg").orEmpty()),
                it.dateTaken,
                it.description,
                it.published.readable(),
                it.author,
                it.tags?.split(" ")?.filter { it.isNotEmpty() }?.map {
                    all.find { tag -> tag.name == it } ?: TagDO(it, PURPLE)
                }
            )
        }) ?: listOf())
    }
}

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "title")
    val title: String?,
    @Json(name = "link")
    val link: String?,
    @Json(name = "media")
    val media: PhotoMedia?,
    @Json(name = "date_taken")
    val dateTaken: Date?,
    @Json(name = "desciption")
    val description: String?,
    @Json(name = "published")
    val published: Date?,
    @Json(name = "author")
    val author: String?,
    @Json(name = "author_id")
    val authorId: String?,
    @Json(name = "tags")
    val tags: String?
)

@JsonClass(generateAdapter = true)
data class PhotoMedia(
    @Json(name = "m")
    val m: String?
)
