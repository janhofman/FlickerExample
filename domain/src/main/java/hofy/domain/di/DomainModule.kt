package hofy.domain.di

import hofy.domain.usecase.DownloadImageUseCase
import hofy.domain.usecase.GetPhotosUseCase
import hofy.domain.usecase.GetTagsUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        GetPhotosUseCase(get())
    }
    single {
        DownloadImageUseCase(get())
    }
    single {
        GetTagsUseCase(get())
    }
}