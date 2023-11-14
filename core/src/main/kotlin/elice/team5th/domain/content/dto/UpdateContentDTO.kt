package elice.team5th.domain.content.dto

import java.time.LocalDateTime

data class UpdateContentDTO(
    val title: String?, // 모든 필드가 업데이트 시 변경될 수 있으므로 nullable로 선언
    val type: Int?,
    val posterImage: String?,
    val starRating: Int?,
    val director: String?,
    val genre: String?,
    val releaseDate: LocalDateTime?,
    val episodeDate: String?,
    val season: Int?
)
