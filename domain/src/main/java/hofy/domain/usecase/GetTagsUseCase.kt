package hofy.domain.usecase

import hofy.domain.model.TagDO
import hofy.domain.repository.FlickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTagsUseCase(private val flickerRepository: FlickerRepository) {
    operator fun invoke(): Flow<List<TagDO>> {
        return flow {
            emit(flickerRepository.getTags())
        }
    }
}