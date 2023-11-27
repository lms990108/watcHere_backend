package elice.team5th.domain.tmdb.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.NotFoundException

class MovieNotFoundException(detail: String) : NotFoundException(ErrorType.MOVIE_NOT_FOUND, detail)
