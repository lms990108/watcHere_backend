package elice.team5th.domain.auth.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.UnauthorizedException

class BanUserException(detail: String) : UnauthorizedException(ErrorType.BAN_USER, detail)
