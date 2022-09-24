package hofy.domain.dataresult

sealed class DataResult<out T> {
    data class Error<T>(val e: Throwable) : DataResult<T>()
    object Loading : DataResult<Nothing>()
    object Empty : DataResult<Nothing>()
    data class Success<T>(val data: T) : DataResult<T>()

}
