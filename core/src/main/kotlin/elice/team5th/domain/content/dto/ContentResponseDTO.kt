package elice.team5th.domain.content.dto

import elice.team5th.domain.content.model.Content
import java.time.LocalDateTime

data class ContentResponseDTO(
    val id: Long,
    val title: String,
    val type: Int,
    val posterImage: String,
    val starRating: Int?,
    val director: String,
    val genre: String?,
    val releaseDate: LocalDateTime?,
    val episodeDate: String?,
    val season: Int?
) {
    companion object {
        fun fromEntity(content: Content): ContentResponseDTO {
            return ContentResponseDTO(
                id = content.id,
                title = content.title,
                type = content.type,
                posterImage = content.posterImage,
                starRating = content.starRating,
                director = content.director,
                genre = content.genre,
                releaseDate = content.releaseDate,
                episodeDate = content.episodeDate,
                season = content.season
            )
        }
    }
}
