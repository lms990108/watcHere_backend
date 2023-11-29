package elice.team5th.domain.like.exception

import elice.team5th.common.exception.ConflictException
import elice.team5th.common.exception.ErrorType

class AlreadyLikedContentException(detail: String) : ConflictException(ErrorType.ALREADY_LIKED_CONTENT, detail)
