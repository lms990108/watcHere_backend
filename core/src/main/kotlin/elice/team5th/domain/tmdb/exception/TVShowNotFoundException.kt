package elice.team5th.domain.tmdb.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.NotFoundException

class TVShowNotFoundException(detail: String) : NotFoundException(ErrorType.TV_SHOW_NOT_FOUND, detail)
