package elice.team5th.controller

import elice.team5th.config.properties.AppProperties
import elice.team5th.domain.auth.exception.NotExpiredTokenException
import elice.team5th.domain.auth.exception.TokenValidationFailedException
import elice.team5th.domain.auth.repository.UserRefreshTokenRepository
import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.user.model.RoleType
import elice.team5th.util.CookieUtil
import elice.team5th.util.HeaderUtil
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Date

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val appProperties: AppProperties,
    private val tokenProvider: AuthTokenProvider,
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {
    companion object {
        private const val REFRESH_TOKEN = "refresh_token"
        private const val THREE_DAYS_MS = 259200000L
    }

    @Operation(summary = "토큰 갱신", description = "리프레쉬 토큰을 갱신합니다.")
    @PostMapping("/refresh")
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        // 액세스 토큰 확인
        val accessToken = HeaderUtil.getAccessToken(request)
        val authToken = tokenProvider.convertAuthToken(accessToken)
        if (!authToken.validate()) {
            throw TokenValidationFailedException("액세스 토큰이 유효하지 않습니다.")
        }

        // 만료된 액세스 토큰인지 확인
        val claims = authToken.getExpiredTokenClaims() ?: throw NotExpiredTokenException("액세스 토큰이 만료되지 않았습니다.")

        val userId = claims.subject
        val roleType = RoleType.of(claims["role"].toString())

        // 리프레쉬 토큰 확인
        val refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
            .map { it.value }
            .orElse(null)
        var authRefreshToken = tokenProvider.convertAuthToken(refreshToken)

        if (!authRefreshToken.validate()) throw TokenValidationFailedException("리프레쉬 토큰이 유효하지 않습니다.")

        // 리프레쉬 토큰이 유효한지 확인
        val userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken)
            ?: throw TokenValidationFailedException("유저와 리프레쉬 토큰이 일치하지 않습니다.")

        val now = Date()
        val newAccessToken = tokenProvider.createAuthToken(
            userId,
            roleType.code,
            Date(now.time + appProperties.auth.tokenExpiry)
        )

        val validTime = authRefreshToken.getTokenClaims()?.expiration?.time?.minus(now.time) ?: 0

        // 리프레쉬 토큰 기간이 3일 이하로 남은 경우, 갱신
        if (validTime < THREE_DAYS_MS) {
            val refreshTokenExpiry = appProperties.auth.refreshTokenExpiry
            authRefreshToken = tokenProvider.createAuthToken(
                appProperties.auth.tokenSecret!!,
                Date(now.time + refreshTokenExpiry)
            )

            userRefreshToken.refreshToken = authRefreshToken.token!!
            userRefreshTokenRepository.saveAndFlush(userRefreshToken)

            val cookieMaxAge = (refreshTokenExpiry / 60).toInt()
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN)
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.token!!, cookieMaxAge)
        }

        return ResponseEntity.ok()
            .body("token" to newAccessToken.token)
    }
}
