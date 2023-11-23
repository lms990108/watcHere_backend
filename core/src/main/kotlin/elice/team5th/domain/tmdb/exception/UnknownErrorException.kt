package elice.team5th.domain.tmdb.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.GenericErrorException

class UnknownErrorException(detail: String = "") : GenericErrorException(ErrorType.INTERNAL_SERVER_ERROR, detail)
