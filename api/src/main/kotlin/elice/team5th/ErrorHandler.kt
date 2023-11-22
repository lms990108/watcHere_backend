package elice.team5th

import elice.team5th.common.exception.EliceException
import elice.team5th.common.exception.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    @ExceptionHandler(EliceException::class)
    fun handleEliceException(e: EliceException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(e.errorType.code, e.detail),
            e.errorType.httpStatus
        )
    }
}
