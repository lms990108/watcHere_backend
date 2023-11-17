package elice.team5th.common.exception

import elice.team5th.common.exception.ErrorType

abstract class EliceException(val errorType: ErrorType, val detail: String = "") : RuntimeException(errorType.name)

abstract class InvalidRequestException(errorType: ErrorType, detail: String = "") : EliceException(errorType, detail)
abstract class NotAllowedException(errorType: ErrorType, detail: String = "") : EliceException(errorType, detail)
abstract class UnauthorizedException(errorType: ErrorType, detail: String = "") : EliceException(errorType, detail)
abstract class NotFoundException(errorType: ErrorType, detail: String = "") : EliceException(errorType, detail)
abstract class ConflictException(errorType: ErrorType, detail: String = "") : EliceException(errorType, detail)
