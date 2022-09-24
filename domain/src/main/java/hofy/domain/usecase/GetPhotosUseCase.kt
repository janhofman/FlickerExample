package hofy.domain.usecase

import hofy.domain.dataresult.DataResult
import hofy.domain.model.PublicPhotosDO
import hofy.domain.model.TagDO
import hofy.domain.repository.FlickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class GetPhotosUseCase(private val flickerRepository: FlickerRepository) {
    operator fun invoke(
        tags: String?,
        selectedTags: List<TagDO>?
    ): Flow<DataResult<PublicPhotosDO>> {
        return flow {
            val formattedTags =
                tags?.trim()?.split(" ")?.filter { it.isNotBlank() }?.map { it.trim() }
                    ?.joinToString(separator = ",") { it } ?: ""
            val tmpList = selectedTags?.toMutableList()
            tmpList?.removeAll { formattedTags.contains(it.name) }
            val selectedTagsString = (tmpList ?: listOf()).joinToString(
                ",",
                prefix = if (formattedTags.isEmpty() || tmpList.isNullOrEmpty()) "" else ","
            ) { it.name }


            val photos = flickerRepository.getPhotos(
                formattedTags + selectedTagsString
            )
            emit(
                if (photos is DataResult.Success && photos.data.items.isEmpty()) {
                    DataResult.Empty
                } else {
                    photos
                }
            )
        }.onStart { emit(DataResult.Loading) }
    }
}