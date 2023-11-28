package elice.team5th.domain.review.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateReviewDTO(
    @JsonProperty("content_id")
    val contentId: Int,
    @JsonProperty("content_type")
    val contentType: String, // Enum 타입으로 변경
    val detail: String,
    val rating: Int
)
