package com.mbh.moviebrowser.di

import com.mbh.moviebrowser.data.data_source.MovieDBApiSource
import com.mbh.moviebrowser.data.repository.MovieDBRepositoryImpl
import com.mbh.moviebrowser.domain.repository.MovieDBRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

const val BASE_URL = "https://api.themoviedb.org/3/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): MovieDBApiSource{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api: MovieDBApiSource): MovieDBRepository =
        MovieDBRepositoryImpl(api = api)
}