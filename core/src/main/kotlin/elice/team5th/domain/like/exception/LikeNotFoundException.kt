package elice.team5th.domain.like.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.NotFoundException

class LikeNotFoundException(detail: String = "") : NotFoundException(ErrorType.LIKE_NOT_FOUND, detail)
