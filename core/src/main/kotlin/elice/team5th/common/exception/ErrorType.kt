package elice.team5th.common.exception

enum class ErrorType(
    val code: Int
) {
    INVALID_REQUEST(0),

    NOT_ALLOWED(1000),

    UNAUTHORIZED(2000),
    TOKEN_VALIDATION_FAILED(2001),
    OAUTH_PROVIDER_MISS_MATCH(2002),

    NOT_FOUND(3000),
    USER_NOT_FOUND(3001),

    CONFLICT(4000),

    SERVER_ERROR(10000)
}
