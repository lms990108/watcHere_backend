package elice.team5th.domain.review.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.UnauthorizedException

class UnauthorizedReviewAccessException(detail: String = "") : UnauthorizedException(
    ErrorType.UNAUTHORIZED_REVIEW_ACCESS,
    detail
)
