package com.mbh.moviebrowser.data.mapper

import com.mbh.moviebrowser.domain.model.Genre
import com.mbh.moviebrowser.domain.model.dto.GenresDto

class GenresDtoToGenre {

    operator fun invoke(genresDto: GenresDto): List<Genre> =
        genresDto.genres.map {
            Genre(
                id = it.id,
                name = it.name
            )
        }
}