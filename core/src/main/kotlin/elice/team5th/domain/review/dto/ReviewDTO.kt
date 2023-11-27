package elice.team5th.domain.review.dto

import com.fasterxml.jackson.annotation.JsonProperty
import elice.team5th.domain.user.model.User

data class ReviewDTO(
    val id: Long? = null,
    val userId: String,
    @JsonProperty("content_id")
    val contentId: Int,
    val detail: String,
    val rating: Int,
    val likes: Int,
    val reports: Int
)
