package elice.team5th.domain.auth.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class RestAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        authException.printStackTrace()
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.localizedMessage)
    }
}
