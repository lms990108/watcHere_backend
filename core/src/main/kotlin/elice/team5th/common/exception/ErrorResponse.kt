package elice.team5th.common.exception

data class ErrorResponse(
    val errorCode: Int,
    val errorMessage: String = ""
)
