package hofy.data.di

import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hofy.data.BuildConfig
import hofy.data.local.AppDatabase
import hofy.data.remote.FlickerApi
import hofy.data.repository.FlickerRepositoryImp
import hofy.domain.repository.FlickerRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*


val dataModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "tag-db"
        ).build()
    }
    single<FlickerApi> {

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.FLICKER_BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .addLast(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()

        retrofit.create(FlickerApi::class.java)
    }
    single(named("io")) {
        Dispatchers.IO
    }
    single<FlickerRepository> {
        FlickerRepositoryImp(get(), get(), get(named("io")))
    }
}