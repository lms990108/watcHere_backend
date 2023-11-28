package elice.team5th.domain.review.dto

import com.fasterxml.jackson.annotation.JsonProperty
import elice.team5th.domain.tmdb.enumtype.ContentType

data class CreateReviewDTO(
    @JsonProperty("content_id")
    val contentId: Int,
    val contentType: ContentType, // Enum 타입으로 변경
    val detail: String,
    val rating: Int
)
