package elice.team5th.domain.auth.entity

import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*

class UserPrincipal(
    private val userId: String,
    val providerType: ProviderType,
    val roleType: RoleType,
    private val authorities: Collection<GrantedAuthority>,
    private var attributes: Map<String, Any>? = null
) : OAuth2User, OidcUser {
    override fun getName(): String = userId

    override fun getAttributes(): Map<String, Any>? {
        return attributes
    }
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
    }

    override fun getClaims(): Map<String, Any>? = null

    override fun getUserInfo(): OidcUserInfo? = null

    override fun getIdToken(): OidcIdToken? = null

    companion object {
        fun create(user: User): UserPrincipal = UserPrincipal(
            user.userId,
            user.providerType,
            RoleType.USER,
            listOf(SimpleGrantedAuthority(RoleType.USER.code))
        )

        fun create(user: User, attributes: Map<String, Any>): UserPrincipal {
            val userPrincipal = create(user)
            userPrincipal.attributes = attributes
            return userPrincipal
        }
    }
}
