package hofy.domain.repository

import hofy.domain.dataresult.DataResult
import hofy.domain.model.PhotoDO
import hofy.domain.model.PublicPhotosDO
import hofy.domain.model.TagDO

interface FlickerRepository {
    suspend fun getPhotos(tags: String = ""): DataResult<PublicPhotosDO>
    suspend fun getTags(): List<TagDO>
    suspend fun downloadImage(photoDO: PhotoDO): DataResult<Boolean>
}