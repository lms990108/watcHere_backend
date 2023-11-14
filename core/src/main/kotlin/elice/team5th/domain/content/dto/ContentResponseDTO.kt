package elice.team5th.domain.content.dto

import elice.team5th.domain.content.model.Content

// Content 응답을 위한 DTO
data class ContentResponseDTO(
    val id: Long,
    val title: String,
    val type: String,
    val posterImage: String?,
    val starRating: Float,
    val director: String?
) {
    companion object {
        fun fromEntity(content: Content): ContentResponseDTO {
            return ContentResponseDTO(
                id = content.id ?: throw IllegalArgumentException("Content ID cannot be null"),
                title = content.title,
                type = content.type,
                posterImage = content.posterImage,
                starRating = content.starRating,
                director = content.director
            )
        }
    }
}

