package com.mbh.moviebrowser.data.mapper

import com.mbh.moviebrowser.domain.model.ResultMovie
import com.mbh.moviebrowser.domain.model.dto.MovieDto

class MovieDtoToMovie {

    operator fun invoke(movieDto: MovieDto): List<ResultMovie> =
        movieDto.resultDtos.map { result ->
            ResultMovie(
                id = result.id,
                title = result.title,
                genre_ids = result.genre_ids,
                overview = result.overview,
                poster_path = result.poster_path,
                vote_average = result.vote_average,
            )
        }
}