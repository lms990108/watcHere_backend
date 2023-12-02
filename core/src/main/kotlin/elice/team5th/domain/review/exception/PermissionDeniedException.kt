package elice.team5th.domain.review.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.ForbiddenException

class PermissionDeniedException(detail: String = "") : ForbiddenException(ErrorType.PERMISSION_DENIED, detail)
