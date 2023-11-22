package elice.team5th.domain.tmdb.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.NotFoundException

class ResponseParsingException(detail: String = "") : NotFoundException(ErrorType.INTERNAL_SERVER_ERROR, detail)
