package elice.team5th.domain.user.dto

import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User

class UserDto {
    data class Response(
        val userId: String?,
        val email: String,
        val profileImage: String,
        val nickname: String,
        val ban: Boolean,
        val provider: ProviderType,
        val role: RoleType
//        val favorites:
//        val reviews
    ) {
        constructor(user: User) : this(
            userId = user.userId,
            email = user.email ?: "",
            profileImage = user.profileImage,
            nickname = user.nickname,
            ban = user.ban,
            provider = user.provider,
            role = user.role
//            favorites = user.favorites
//            reviews = user.reviews
        )
    }

    data class UpdateRequest(
        var profileImage: String?,
        var nickname: String?,
        var ban: Boolean?,
        var role: RoleType?
    )
}
