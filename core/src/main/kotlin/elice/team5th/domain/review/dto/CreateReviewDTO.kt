package elice.team5th.domain.review.dto

import com.fasterxml.jackson.annotation.JsonProperty
import elice.team5th.domain.tmdb.enumtype.ContentType

data class CreateReviewDTO(
    @JsonProperty("content_id")
    val contentId: Int,
    @JsonProperty("content_type")
    val contentType: ContentType,
    val detail: String,
    val rating: Int
)
