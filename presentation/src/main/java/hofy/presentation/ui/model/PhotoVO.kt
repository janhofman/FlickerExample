package hofy.presentation.ui.model

import hofy.domain.model.PhotoDO
import hofy.domain.model.PhotoMediaDO
import hofy.domain.model.TagDO

data class PhotoVO(
    val tags: List<TagVO>,
    val url: String,
    val author: String,
    val link: String?,
    val published: String,
    var isFavourite: Boolean = false
) {
    fun toDO(): PhotoDO {
        return PhotoDO(media = PhotoMediaDO(url))
    }

    companion object {
        fun fromDO(source: PhotoDO): PhotoVO {
            return PhotoVO(
                source.tags?.map { TagVO.fromDO(it) } ?: listOf(),
                source.media?.m.orEmpty(),
                source.author.orEmpty(),
                source.link,
                source.published.orEmpty()
            )
        }
    }
}

data class TagVO(val name: String, val color: Long, var selected: Boolean = false) {
    fun toDO(): TagDO {
        return TagDO(name = name, color = color)
    }

    companion object {
        fun fromDO(source: TagDO): TagVO {
            return TagVO(source.name, source.color)
        }
    }
}


