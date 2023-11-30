package elice.team5th.domain.tmdb.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.NotFoundException

class ContentNotFoundException(detail: String) : NotFoundException(ErrorType.CONTENT_NOT_FOUND, detail)
