package hofy.presentation.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hofy.domain.dataresult.DataResult
import hofy.domain.usecase.DownloadImageUseCase
import hofy.domain.usecase.GetPhotosUseCase
import hofy.domain.usecase.GetTagsUseCase
import hofy.presentation.ui.ext.set
import hofy.presentation.ui.model.PhotoVO
import hofy.presentation.ui.model.TagItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MainState(
    val grid: Boolean = false,
    val loading: Boolean = true,
    val empty: Boolean = false,
    val error: Throwable? = null,
    val photos: List<PhotoVO>? = null,
    val tags: List<TagItem> = listOf(),
    val imageDownloadingState: ImageDownloadingState? = null,
    val currentDetailedPhoto: PhotoVO? = null,
    val query: String? = null
)

data class ImageDownloadingState(
    val imageDownloading: Boolean = false,
    val imageDownloadSuccess: Boolean = false
)

class MainViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val downloadImageUseCase: DownloadImageUseCase
) : ViewModel() {

    fun changeMode() {
        _state.set {
            _state.value.copy(grid = !_state.value.grid)
        }
    }

    private val _state = mutableStateOf(MainState())
    val state = _state as State<MainState>

    private fun loadTags() {
        viewModelScope.launch {
            getTagsUseCase().collect {
                _state.set {
                    val newList = it.map { TagItem.TagVO.fromDO(it) }.toMutableList()
                    newList.removeAll(tags.toSet())
                    val newTags = tags.toMutableList().apply { addAll(newList) }
                    _state.value.copy(tags = addCancelTagsIfNeeded(newTags))
                }
            }
        }
    }

    fun triggerPhotoDetail(photoVO: PhotoVO) {
        _state.set {
            _state.value.copy(currentDetailedPhoto = photoVO)
        }
    }

    fun triggerDownloadImage(photoVO: PhotoVO) {
        viewModelScope.launch {
            downloadImageUseCase(photoVO.toDO()).collect {
                if (it is DataResult.Error || it is DataResult.Success) {
                    scheduleDownloadingStateClear()
                }
                _state.set {
                    _state.value.copy(
                        imageDownloadingState = ImageDownloadingState(
                            imageDownloading = it is DataResult.Loading,
                            imageDownloadSuccess = it is DataResult.Success
                        )
                    )
                }
            }
        }
    }

    private fun scheduleDownloadingStateClear() {
        viewModelScope.launch {
            delay(3000)
            _state.set { _state.value.copy(imageDownloadingState = null) }
        }
    }

    init {
        loadImages()
    }

    private suspend fun executePhotosUseCase(query: String? = null, tags: List<TagItem.TagVO>?) {
        getPhotosUseCase(query, tags?.map { it.toDO() }).collect {
            _state.set {
                when (it) {
                    DataResult.Empty -> {
                        loadTags()
                        _state.value.copy(empty = true, loading = false)
                    }
                    is DataResult.Error -> {
                        loadTags()
                        _state.value.copy(error = it.e, loading = false)
                    }
                    DataResult.Loading -> _state.value.copy(
                        loading = true,
                        empty = false,
                        error = null
                    )
                    is DataResult.Success -> {
                        loadTags()
                        _state.value.copy(
                            photos = it.data.items.map {
                                PhotoVO.fromDO(it)
                            },
                            empty = false,
                            error = null,
                            loading = false
                        )
                    }
                }
            }
        }
    }

    private var searchJob: Job? = null
    fun loadImages(query: String? = null, tags: List<TagItem.TagVO>? = null) {
        if (query != null) {
            _state.set {
                _state.value.copy(query = query)
            }
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query != null) {
                delay(700)
                executePhotosUseCase(
                    query,
                    tags = tags ?: _state.value.tags.filterIsInstance<TagItem.TagVO>()
                        .filter { it.selected })
            } else {
                executePhotosUseCase(
                    _state.value.query,
                    tags = tags ?: _state.value.tags.filterIsInstance<TagItem.TagVO>()
                        .filter { it.selected })
            }
        }
    }

    private fun addCancelTagsIfNeeded(newTags: MutableList<TagItem>): List<TagItem> {
        if (newTags.find { it is TagItem.TagVO && it.selected } != null
            && newTags.find { it is TagItem.Cancel } == null) {
            newTags.add(0, TagItem.Cancel)
        }
        return newTags
    }

    fun triggerTagClicked(tagVO: TagItem.TagVO) {
        _state.set {

            val newTags = tags.toMutableList().apply {
                val index = indexOf(tagVO)
                if (index == -1) {
                    add(index, tagVO.copy(selected = !tagVO.selected))
                } else {
                    set(index, tagVO.copy(selected = !tagVO.selected))
                }
            }
            _state.value.copy(tags = addCancelTagsIfNeeded(newTags))
        }
        loadImages()
    }

    fun triggerFavouriteClicked(photo: PhotoVO) {
        _state.set {
            _state.value.copy(photos = (photos ?: listOf()).toMutableList().apply {
                val index = indexOf(photo)
                if (index == -1) {
                    add(index, photo.copy(isFavourite = !photo.isFavourite))
                } else {
                    set(index, photo.copy(isFavourite = !photo.isFavourite))
                }
            })
        }
    }

    fun triggerCancelTags() {
        _state.set {
            val newTags: MutableList<TagItem> =
                tags.toMutableList().filterIsInstance<TagItem.TagVO>()
                    .map { it.copy(selected = false) }.toMutableList()
            _state.value.copy(
                tags = addCancelTagsIfNeeded(newTags)
            )
        }
        loadImages()
    }

}