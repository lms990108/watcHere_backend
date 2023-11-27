package elice.team5th.domain.review.dto

import com.fasterxml.jackson.annotation.JsonProperty
import elice.team5th.domain.review.model.Review
import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.model.User

data class ReviewDTO(
    val id: Long? = null,
    val author: UserDto.Response,
    @JsonProperty("content_id")
    val contentId: Int,
    val detail: String,
    val rating: Int,
    val likes: Int,
    val reports: Int
) {
    constructor(review: Review) : this(
        id = review.id,
        author = UserDto.Response(review.user),
        contentId = review.contentId,
        detail = review.detail,
        rating = review.rating,
        likes = review.likes,
        reports = review.reports
    )
}
