package hofy.domain.usecase

import hofy.domain.dataresult.DataResult
import hofy.domain.model.PhotoDO
import hofy.domain.repository.FlickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class DownloadImageUseCase(private val flickerRepository: FlickerRepository) {
    operator fun invoke(photoDO: PhotoDO): Flow<DataResult<Boolean>> {
        return flow {
            emit(flickerRepository.downloadImage(photoDO))
        }.onStart { emit(DataResult.Loading) }
    }
}