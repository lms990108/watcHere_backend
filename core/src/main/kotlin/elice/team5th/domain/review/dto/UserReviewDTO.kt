package elice.team5th.domain.review.dto

import elice.team5th.domain.user.dto.UserDto

class UserReviewDTO (
    val id: Long? = null,
    val contentId: Int,
    val detail: String,
    val rating: Int,
    val likes: Int,
    val reports: Int
)
