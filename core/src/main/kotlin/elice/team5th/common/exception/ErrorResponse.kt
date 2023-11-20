package elice.team5th.common.exception

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    val errorCode: Int,
    val errorMessage: String = "",
)
