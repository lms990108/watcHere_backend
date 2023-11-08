package elice.team5th.elice.team5h.common.exception

enum class ErrorType(
    val code: Int
) {
    INVALID_REQUEST(0),

    NOT_ALLOWED(1000),

    UNAUTHORIZED(2000),

    DATA_NOT_FOUND(3000),

    CONFLICT(4000),

    SERVER_ERROR(10000)
}
