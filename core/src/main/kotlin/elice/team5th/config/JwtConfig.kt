package elice.team5th.config

import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.user.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
    @Value("\${jwt.secret}")
    private val secret: String,
    val userService: UserService
) {
    @Bean
    fun jwtProvider(): AuthTokenProvider {
        return AuthTokenProvider(userService, secret)
    }
}
