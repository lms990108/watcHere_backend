package elice.team5th.common.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val httpStatus: HttpStatus
) {
    // 400
    INVALID_REQUEST(1000, HttpStatus.BAD_REQUEST),
    DELETED_USER(1001, HttpStatus.BAD_REQUEST),
    NOT_EXPIRED_TOKEN(1002, HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED(1003, HttpStatus.BAD_REQUEST),

    // 401
    UNAUTHORIZED(2000, HttpStatus.UNAUTHORIZED),
    TOKEN_VALIDATION_FAILED(2001, HttpStatus.UNAUTHORIZED),
    OAUTH_PROVIDER_MISS_MATCH(2002, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_REVIEW_ACCESS(2003, HttpStatus.UNAUTHORIZED),
    BAN_USER(2004, HttpStatus.UNAUTHORIZED),

    // 403
    PERMISSION_DENIED(3001, HttpStatus.FORBIDDEN),

    // 404
    NOT_FOUND(4000, HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(4001, HttpStatus.NOT_FOUND),
    PATH_NOT_FOUND(4002, HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND(4003, HttpStatus.NOT_FOUND),

    // 409
    CONFLICT(5000, HttpStatus.CONFLICT),

    // 429
    TOO_MANY_REQUEST(6001, HttpStatus.TOO_MANY_REQUESTS),

    // 500
    INTERNAL_SERVER_ERROR(7000, HttpStatus.INTERNAL_SERVER_ERROR),

    // 503
    SERVICE_UNAVAILABLE(8000, HttpStatus.SERVICE_UNAVAILABLE)
}
