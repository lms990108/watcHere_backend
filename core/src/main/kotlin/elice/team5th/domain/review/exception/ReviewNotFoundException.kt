package elice.team5th.domain.review.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.NotFoundException

class ReviewNotFoundException(detail: String = "") : NotFoundException(ErrorType.REVIEW_NOT_FOUND, detail)
