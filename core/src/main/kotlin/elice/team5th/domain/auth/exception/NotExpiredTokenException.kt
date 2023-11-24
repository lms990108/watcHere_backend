package elice.team5th.domain.auth.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.InvalidRequestException

class NotExpiredTokenException(detail: String = "") : InvalidRequestException(ErrorType.NOT_EXPIRED_TOKEN, detail)
