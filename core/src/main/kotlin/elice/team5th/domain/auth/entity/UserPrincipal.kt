package elice.team5th.domain.auth.entity

import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import io.jsonwebtoken.Claims
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*

class UserPrincipal(
    val userId: String,
    var nickname: String,
    var profileImage: String,
    val providerType: ProviderType,
    val roleType: RoleType,
    private val authorities: Collection<GrantedAuthority>,
    private var attributes: Map<String, Any>? = null
) : OAuth2User, OidcUser {
    override fun getName(): String = userId

    override fun getAttributes(): Map<String, Any>? = attributes

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities.toMutableList()

    override fun getClaims(): Map<String, Any>? = null

    override fun getUserInfo(): OidcUserInfo? = null

    override fun getIdToken(): OidcIdToken? = null

    companion object {
        private const val AUTHORITIES_KEY = "role"

        fun create(user: User): UserPrincipal = UserPrincipal(
            user.userId,
            user.nickname,
            user.profileImage,
            user.provider,
            user.role,
            listOf(SimpleGrantedAuthority(user.role.code))
        )

        fun create(user: User, claims: Claims): UserPrincipal = UserPrincipal(
            user.userId,
            user.nickname,
            user.profileImage,
            user.provider,
            RoleType.of(claims[AUTHORITIES_KEY].toString()),
            claims[AUTHORITIES_KEY].toString()
                .split(",")
                .map { SimpleGrantedAuthority(it.trim()) }
        )

        fun create(user: User, attributes: Map<String, Any>): UserPrincipal {
            val userPrincipal = create(user)
            userPrincipal.attributes = attributes
            return userPrincipal
        }
    }
}
