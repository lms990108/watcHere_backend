package elice.team5th.domain.auth.exception

import elice.team5th.common.exception.ErrorType
import elice.team5th.common.exception.UnauthorizedException

class OAuthProviderMissMatchException(detail: String = "") : UnauthorizedException(
    ErrorType.OAUTH_PROVIDER_MISS_MATCH,
    detail
)
