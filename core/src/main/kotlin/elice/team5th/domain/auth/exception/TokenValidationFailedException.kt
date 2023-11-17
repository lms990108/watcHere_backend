package elice.team5th.domain.auth.exception

import elice.team5th.common.exception.EliceException
import elice.team5th.common.exception.ErrorType

class TokenValidationFailedException(detail: String = "") : EliceException(ErrorType.TOKEN_VALIDATION_FAILED, detail)
