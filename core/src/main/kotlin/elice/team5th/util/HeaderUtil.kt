package elice.team5th.util

import jakarta.servlet.http.HttpServletRequest

object HeaderUtil {
    private const val HEADER_AUTHORIZATION = "Authorization"
    private const val TOKEN_PREFIX = "Bearer "

    fun getAccessToken(request: HttpServletRequest): String? {
        val headerValue = request.getHeader(HEADER_AUTHORIZATION) ?: return null

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length)
        }

        return null
    }
}
