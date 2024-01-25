package com.mbh.moviebrowser.domain.model.dto

data class MovieDto(
    val datesDto: DatesDto,
    val page: Int,
    val resultDtos: List<ResultDto>,
    val total_pages: Int,
    val total_results: Int
)