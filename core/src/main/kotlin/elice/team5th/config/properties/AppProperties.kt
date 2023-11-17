package elice.team5th.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
class AppProperties {
    val auth: Auth = Auth()
    val oauth2: OAuth2 = OAuth2()

    data class Auth(
        var tokenSecret: String = "",
        var tokenExpiry: Long = 0,
        var refreshTokenExpiry: Long = 0
    )

    class OAuth2 {
        var authorizedRedirectUris: List<String> = listOf()

        fun authorizedRedirectUris(authorizedRedirectUris: List<String>): OAuth2 {
            this.authorizedRedirectUris = authorizedRedirectUris
            return this
        }
    }
}
