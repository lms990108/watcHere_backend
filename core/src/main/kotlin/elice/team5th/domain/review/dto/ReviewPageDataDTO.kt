package elice.team5th.domain.review.dto

import elice.team5th.domain.review.model.Review
import org.springframework.data.domain.Page

data class ReviewPageDataDTO(
    val reviews: Page<ReviewDTO>,
    val averageRating: Double,
    val totalElements: Long
)
