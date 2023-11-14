package elice.team5th.domain.content.dto

import java.time.LocalDateTime

data class CreateContentDTO(
    val title: String,
    val type: Int,
    val posterImage: String,
    val starRating: Int?, // nullable이므로 Int?로 선언
    val director: String,
    val genre: String?, // nullable 필드
    val release: LocalDateTime?, // nullable 필드
    val episodeDate: String?, // nullable 필드
    val season: Int? // nullable 필드
)
