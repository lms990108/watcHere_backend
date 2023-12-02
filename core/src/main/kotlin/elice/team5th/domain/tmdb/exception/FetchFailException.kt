package elice.team5th.domain.tmdb.exception

import elice.team5th.common.exception.EliceException
import elice.team5th.common.exception.ErrorType

class FetchFailException(detail: String) : EliceException(ErrorType.FETCH_FAIL, detail)
