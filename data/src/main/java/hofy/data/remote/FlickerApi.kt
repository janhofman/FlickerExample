package hofy.data.remote

import hofy.data.remote.model.PublicPhotos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerApi {
    @GET("photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getPhotos(@Query("tags") tags: String): Response<PublicPhotos>
}