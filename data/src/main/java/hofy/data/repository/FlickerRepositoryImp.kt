package hofy.data.repository

import android.os.Environment
import hofy.data.local.AppDatabase
import hofy.data.local.model.TagDB
import hofy.data.remote.FlickerApi
import hofy.data.remote.model.PublicPhotos
import hofy.domain.dataresult.DataResult
import hofy.domain.model.PhotoDO
import hofy.domain.model.PublicPhotosDO
import hofy.domain.model.TagDO
import hofy.domain.repository.FlickerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import kotlin.random.Random


class FlickerRepositoryImp(
    private val api: FlickerApi,
    private val db: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) : FlickerRepository {

    private suspend fun retrieveAndSaveTagsFromPhotos(publicPhotosDO: PublicPhotos): List<TagDO> {
        return withContext(dispatcher) {
            val photos = publicPhotosDO.items?.filterNotNull()?.filter { !it.tags.isNullOrBlank() }
            val tags = photos?.map { it.tags!!.split(" ") }?.flatten()
            if (!tags.isNullOrEmpty()) {
                val existingColors = db.tagDao().getAllColors()
                val tagColors: MutableList<Long> = TAG_COLORS.toMutableList()
                tagColors.removeAll(existingColors)
                var possibleColors = tagColors
                if (possibleColors.isEmpty()) {
                    possibleColors = TAG_COLORS.toMutableList()
                }
                val finalTags = tags.map {
                    val color =
                        possibleColors.removeAt(Random.nextInt(possibleColors.size))
                    if (possibleColors.isEmpty()) {
                        possibleColors = TAG_COLORS.toMutableList()
                    }
                    TagDB(
                        it,
                        color
                    )
                }.toTypedArray()
                db.tagDao().insertAll(*finalTags)
            }
            getTags()
        }
    }

    override suspend fun getPhotos(tags: String): DataResult<PublicPhotosDO> {
        return withContext(dispatcher) {
            try {
                val response = api.getPhotos(tags)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        DataResult.Success(data.toDO(retrieveAndSaveTagsFromPhotos(data)))
                    } else {
                        DataResult.Empty
                    }
                } else {
                    DataResult.Error(HttpException(response))
                }
            } catch (e: Throwable) {
                DataResult.Error(e)
            }
        }
    }

    override suspend fun getTags(): List<TagDO> {
        return withContext(dispatcher) {
            db.tagDao().getAll().map { TagDO(it.name, it.color ?: PURPLE) }
        }
    }

    private fun getNewImageFile(
        defaultName: String,
        extension: String,
        iteration: Int? = null
    ): File {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "$defaultName${(iteration ?: "")}.$extension"
        )
        return if (file.exists()) {
            getNewImageFile(defaultName, extension, (iteration ?: 0) + 1)
        } else {
            file
        }
    }

    override suspend fun downloadImage(photoDO: PhotoDO): DataResult<Boolean> {
        return withContext(dispatcher) {
            try {
                val imageUrl = photoDO.media?.m
                val url = URL(imageUrl)
                val inputStream: InputStream = url.openStream()
                val targetFile =
                    getNewImageFile(File(url.file).nameWithoutExtension, File(url.file).extension)
                Files.copy(inputStream, targetFile.toPath())
                DataResult.Success(true)
            } catch (e: Throwable) {
                DataResult.Error(e)
            }
        }
    }
}