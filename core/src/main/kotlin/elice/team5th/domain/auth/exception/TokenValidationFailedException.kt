package elice.team5th.domain.auth.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.UnauthorizedException

class TokenValidationFailedException(detail: String = "") : UnauthorizedException(
    ErrorType.TOKEN_VALIDATION_FAILED,
    detail
)
