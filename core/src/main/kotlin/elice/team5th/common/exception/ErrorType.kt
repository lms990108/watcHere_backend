package elice.team5th.common.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val httpStatus: HttpStatus
) {
    // 400
    INVALID_REQUEST(0, HttpStatus.BAD_REQUEST),

    // 401
    UNAUTHORIZED(2000, HttpStatus.UNAUTHORIZED),
    TOKEN_VALIDATION_FAILED(2001, HttpStatus.UNAUTHORIZED),
    OAUTH_PROVIDER_MISS_MATCH(2002, HttpStatus.UNAUTHORIZED),

    // 404
    NOT_FOUND(3000, HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(3001, HttpStatus.NOT_FOUND),

    // 409
    CONFLICT(4000, HttpStatus.CONFLICT),
}
