package elice.team5th.config

import elice.team5th.config.properties.AppProperties
import elice.team5th.config.properties.CorsProperties
import elice.team5th.domain.auth.exception.RestAuthenticationEntryPoint
import elice.team5th.domain.auth.filter.TokenAuthenticationFilter
import elice.team5th.domain.auth.handler.OAuth2AuthenticationFailureHandler
import elice.team5th.domain.auth.handler.OAuth2AuthenticationSuccessHandler
import elice.team5th.domain.auth.handler.TokenAccessDeniedHandler
import elice.team5th.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import elice.team5th.domain.auth.repository.UserRefreshTokenRepository
import elice.team5th.domain.auth.service.CustomOAuth2UserService
import elice.team5th.domain.auth.token.AuthTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val corsProperties: CorsProperties,
    private val appProperties: AppProperties,
    private val tokenProvider: AuthTokenProvider,
    private val oAuth2UserService: CustomOAuth2UserService,
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint,
    private val tokenAccessDeniedHandler: TokenAccessDeniedHandler,
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // CORS 설정
        http.cors {
            it.configurationSource(corsConfigurationSource())
        }

        // 세션 설정
        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        // CSRF 설정
        http.csrf {
            it.disable()
        }

        http.formLogin {
            it.disable()
        }

        http.httpBasic {
            it.disable()
        }

        http.exceptionHandling {
            it.authenticationEntryPoint(restAuthenticationEntryPoint)
            it.accessDeniedHandler(tokenAccessDeniedHandler)
        }

        // 인증 설정
        http.authorizeHttpRequests { auth ->
            auth
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                .requestMatchers("/api/v1/users/**").hasAnyAuthority(RoleType.USER.code)
                .anyRequest().permitAll()
        }

        // OAuth2 설정
        http.oauth2Login {
            it.authorizationEndpoint {
                it.baseUri("/oauth2/authorization")
                it.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
            }
            it.redirectionEndpoint {
                it.baseUri("/*/oauth2/code/*")
            }
            it.userInfoEndpoint {
                it.userService(oAuth2UserService)
            }
            it.successHandler(oAuth2AuthenticationSuccessHandler())
            it.failureHandler(oAuth2AuthenticationFailureHandler())
        }

        return http.build()
    }

    /*
     * auth 매니저 설정
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.getAuthenticationManager()
    }

    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter =
        TokenAuthenticationFilter(tokenProvider)

    /*
     * 쿠키 기반 인가 Request Repository 설정
     * 인가 요청을 쿠키에 저장하고 읽어오는 역할
     */
    @Bean
    fun oAuth2AuthorizationRequestBasedOnCookieRepository(): OAuth2AuthorizationRequestBasedOnCookieRepository {
        return OAuth2AuthorizationRequestBasedOnCookieRepository()
    }

    @Bean
    fun oAuth2AuthenticationSuccessHandler(): OAuth2AuthenticationSuccessHandler {
        return OAuth2AuthenticationSuccessHandler(
            tokenProvider,
            appProperties,
            userRefreshTokenRepository,
            oAuth2AuthorizationRequestBasedOnCookieRepository()
        )
    }

    @Bean
    fun oAuth2AuthenticationFailureHandler(): OAuth2AuthenticationFailureHandler {
        return OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository())
    }

    /*
     * Cors 설정
     */
    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val corsConfigSource = UrlBasedCorsConfigurationSource()

        val corsConfig = CorsConfiguration()
        corsConfig.allowedHeaders = mutableListOf("*")
        corsConfig.allowedMethods = mutableListOf("*")
        corsConfig.allowedOrigins = mutableListOf("*")
        corsConfig.allowCredentials = false
        corsConfig.maxAge = corsProperties.maxAge

        corsConfigSource.registerCorsConfiguration("/**", corsConfig)
        return corsConfigSource
    }
}
