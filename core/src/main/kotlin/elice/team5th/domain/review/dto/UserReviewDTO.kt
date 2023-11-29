package elice.team5th.domain.review.dto

class UserReviewDTO(
    val id: Long,
    val contentId: Int,
    val contentType: String, // 필드 추가
    val detail: String,
    val rating: Int,
    val likes: Int,
    val reports: Int
)
