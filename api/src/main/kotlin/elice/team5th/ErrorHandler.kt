package elice.team5th

import elice.team5th.common.exception.EliceException
import elice.team5th.common.exception.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
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

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            errorCode = HttpStatus.BAD_REQUEST.value(), // 'errorCode' 파라미터를 제공해야 함
            errorMessage = "The required parameter '${e.parameterName}' is missing."
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
