package elice.team5th.domain.tmdb.util

import com.fasterxml.jackson.core.JsonProcessingException
import elice.team5th.domain.tmdb.exception.ResponseParsingException
import elice.team5th.domain.tmdb.exception.UnknownErrorException
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException

object ErrorUtil {
    fun handleCommonErrors(throwable: Throwable): Throwable {
        return when (throwable) {
            is WebClientResponseException -> {
                ResponseStatusException(throwable.statusCode, "Error from TMDB API: ${throwable.message}", throwable)
            }
            is JsonProcessingException -> {
                ResponseParsingException("Error parsing TMDB API response")
            }
            else -> {
                UnknownErrorException("An unknown error occurred")
            }
        }
    }
}
