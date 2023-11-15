package elice.team5th.domain.movie.dto

import java.time.LocalDateTime

data class MovieDetailDto(
    val id: Long,
    val title: String,
    val posterImageUrl: String,
    val starRating: Float?,
    val director: String,
    val provider: String,
    val releaseDate: LocalDateTime?, // 개봉일
    val genre: String?, // 장르
    val summary: String? // 줄거리 요약
)
