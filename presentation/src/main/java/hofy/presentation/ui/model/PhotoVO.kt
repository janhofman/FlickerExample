package hofy.presentation.ui.model

import hofy.domain.model.PhotoDO
import hofy.domain.model.PhotoMediaDO
import hofy.domain.model.TagDO

data class PhotoVO(
    val title: String,
    val tags: List<TagItem.TagVO>,
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
                source.title.orEmpty(),
                source.tags?.map { TagItem.TagVO.fromDO(it) } ?: listOf(),
                source.media?.m.orEmpty(),
                if (source.author?.startsWith("nobody@flickr.com") == true) {
                    source.author?.replace(".*\\(\"(.*)\"\\).*".toRegex()) {
                        if (it.groups.size > 1) {
                            it.groups[1]?.value.orEmpty()
                        } else {
                            ""
                        }
                    }.orEmpty()
                } else {
                    source.author.orEmpty()
                },
                source.link,
                source.published.orEmpty()
            )
        }
    }
}

sealed class TagItem {
    data class TagVO(val name: String, val color: Long, var selected: Boolean = false) : TagItem() {
        fun toDO(): TagDO {
            return TagDO(name = name, color = color)
        }

        companion object {
            fun fromDO(source: TagDO): TagVO {
                return TagVO(source.name, source.color)
            }
        }
    }

    object Cancel : TagItem()
}




