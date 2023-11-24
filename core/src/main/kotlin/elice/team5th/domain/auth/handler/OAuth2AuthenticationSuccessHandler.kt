package elice.team5th.domain.auth.handler

import elice.team5th.config.properties.AppProperties
import elice.team5th.domain.auth.info.OAuth2UserInfoFactory
import elice.team5th.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import elice.team5th.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import elice.team5th.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.Companion.REFRESH_TOKEN
import elice.team5th.domain.auth.repository.UserRefreshTokenRepository
import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.auth.token.UserRefreshToken
import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.util.CookieUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.Date
import java.util.Locale

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenProvider: AuthTokenProvider,
    private val appProperties: AppProperties,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
    private val authorizationRequestRepository: OAuth2AuthorizationRequestBasedOnCookieRepository
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map { it.value }

        if (redirectUri.isPresent && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw IllegalArgumentException("잘못된 리다이렉트 URI입니다.")
        }

        val targetUrl = redirectUri.orElse(defaultTargetUrl)

        val authToken = authentication as OAuth2AuthenticationToken
        val providerType = ProviderType.valueOf(authToken.authorizedClientRegistrationId.uppercase(Locale.getDefault()))

        val user = authentication.principal as OidcUser
        val userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.attributes)
        val authorities = user.authorities

        val roleType = if (hasAuthority(authorities, RoleType.ADMIN.code)) RoleType.ADMIN else RoleType.USER

        val now = Date()
        val accessToken = tokenProvider.createAuthToken(
            userInfo.getId(),
            roleType.code,
            Date(now.time + appProperties.auth.tokenExpiry)
        )

        // Refresh token setup
        val refreshTokenExpiry = appProperties.auth.refreshTokenExpiry
        val refreshToken = tokenProvider.createAuthToken(
            appProperties.auth.tokenSecret!!,
            Date(now.time + refreshTokenExpiry)
        )

        // Save refresh token to database
        // 이미 있는 유저라면 refresh token을 업데이트 아니면 새로 생성
        var userRefreshToken = userRefreshTokenRepository.findByUserId(userInfo.getId())
        if (userRefreshToken != null) {
            userRefreshToken.refreshToken = refreshToken.token!!
            userRefreshTokenRepository.saveAndFlush(userRefreshToken)
        } else {
            userRefreshToken = UserRefreshToken(userInfo.getId(), refreshToken.token!!)
            userRefreshTokenRepository.saveAndFlush(userRefreshToken)
        }

        val cookieMaxAge = (refreshTokenExpiry / 60).toInt()

        // 쿠키에 있던 리프레쉬 토큰 제거 후 새로운 리프레쉬 토큰 쿠키 추가
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN)
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.token, cookieMaxAge)

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", accessToken.token)
            .build().toUriString()
    }

    private fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun hasAuthority(authorities: Collection<GrantedAuthority>, authority: String): Boolean {
        return authorities.any { it.authority == authority }
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)
        return appProperties.oauth2.authorizedRedirectUris
            .any { authorizedRedirectUri ->
                val authorizedURI = URI.create(authorizedRedirectUri)
                authorizedURI.host.equals(
                    clientRedirectUri.host,
                    ignoreCase = true
                ) && authorizedURI.port == clientRedirectUri.port
            }
    }
}
