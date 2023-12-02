package elice.team5th.domain.auth.token

import elice.team5th.domain.auth.entity.UserPrincipal
import elice.team5th.domain.auth.exception.TokenValidationFailedException
import elice.team5th.domain.user.service.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.security.Key
import java.util.Date

class AuthTokenProvider(
    private val userService: UserService,
    secret: String
) {
    @Value("\${app.auth.token-expiry}")
    private val expiry: Long = 0
    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())
    private val AUTHORITIES_KEY = "role"

    fun createAuthToken(authentication: Authentication): AuthToken {
        val userPrincipal = authentication.principal as UserPrincipal
        val now = Date()
        return AuthToken.createAuthToken(
            userPrincipal.userId,
            userPrincipal.roleType.code,
            Date(now.time + expiry),
            key
        )
    }

    fun createAuthToken(id: String, expiry: Date): AuthToken {
        return AuthToken.createAuthToken(id, expiry, key)
    }

    fun createAuthToken(id: String, role: String, expiry: Date): AuthToken {
        return AuthToken.createAuthToken(id, role, expiry, key)
    }

    fun convertAuthToken(token: String?): AuthToken {
        return AuthToken(token, key)
    }

    fun getAuthentication(authToken: AuthToken): Authentication {
        if (authToken.validate()) {
            val claims: Claims = authToken.getTokenClaims()
                ?: throw TokenValidationFailedException("The Claims of authToken cannot validate.")
            val authorities: Collection<GrantedAuthority> = claims[AUTHORITIES_KEY].toString()
                .split(",")
                .map { SimpleGrantedAuthority(it.trim()) }
            val user = userService.findUserById(claims.subject)
            val principal = UserPrincipal.create(user, claims)

            return UsernamePasswordAuthenticationToken(principal, authToken, authorities)
        } else {
            throw TokenValidationFailedException("The authToken $authToken is not valid.")
        }
    }
}
