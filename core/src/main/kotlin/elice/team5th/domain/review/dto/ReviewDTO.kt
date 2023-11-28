package elice.team5th.domain.review.dto

import com.fasterxml.jackson.annotation.JsonProperty
import elice.team5th.domain.review.model.Review
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.user.dto.UserDto

data class ReviewDTO(
    val id: Long? = null,
    val author: UserDto.Response,
    @JsonProperty("content_id")
    val contentId: Int,
    val contentType: ContentType, // 필드 추가
    val detail: String,
    val rating: Int,
    val likes: Int,
    val reports: Int
) {
    constructor(review: Review) : this(
        id = review.id,
        author = UserDto.Response(review.user),
        contentId = review.contentId,
        contentType = review.contentType, // 여기서 설정
        detail = review.detail,
        rating = review.rating,
        likes = review.likes,
        reports = review.reports
    )
}
