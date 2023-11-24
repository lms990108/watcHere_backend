package elice.team5th.domain.user.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.InvalidRequestException

class DeletedUserException(detail: String = "") : InvalidRequestException(ErrorType.DELETED_USER, detail)
