package elice.team5th.domain.movie.dto

import java.time.LocalDateTime

data class MovieToListDto(
    val id: Long,
    val title: String,
    val posterImageUrl: String,
    val releaseDate: LocalDateTime?
)
