package elice.team5th.domain.auth.filter

import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.util.HeaderUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.math.log

class TokenAuthenticationFilter(
    private val tokenProvider: AuthTokenProvider
): OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val tokenStr = HeaderUtil.getAccessToken(request)
        if (tokenStr != null) {
            val token = tokenProvider.convertAuthToken(tokenStr)
            if (token.validate()) {
                val authentication = tokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } else {
            logger.debug("TokenAuthenticationFilter: token is null")
        }

        filterChain.doFilter(request, response)
    }
}
