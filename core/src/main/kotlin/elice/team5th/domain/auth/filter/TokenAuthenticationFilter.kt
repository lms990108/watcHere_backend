package elice.team5th.domain.auth.filter

import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.user.model.RoleType
import elice.team5th.util.HeaderUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenAuthenticationFilter(
    private val tokenProvider: AuthTokenProvider
) : OncePerRequestFilter() {
    val logger = LoggerFactory.getLogger(this::class.java)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val tokenStr = HeaderUtil.getAccessToken(request)

        if (!tokenStr.isNullOrEmpty()) {
            val token = tokenProvider.convertAuthToken(tokenStr)
            if (token.validate()) {
                val authentication = tokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } else {
            val authentication = AnonymousAuthenticationToken(
                "anonymous",
                "anonymous",
                listOf(SimpleGrantedAuthority(RoleType.GUEST.code))
            )
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }
}
