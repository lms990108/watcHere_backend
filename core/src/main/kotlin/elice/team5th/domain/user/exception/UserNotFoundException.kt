package elice.team5th.domain.user.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.NotFoundException

class UserNotFoundException(detail: String = "") : NotFoundException(ErrorType.USER_NOT_FOUND, detail)
