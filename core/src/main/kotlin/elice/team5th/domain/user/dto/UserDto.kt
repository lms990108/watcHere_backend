package elice.team5th.domain.user.dto

import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import jakarta.validation.constraints.NotBlank

class UserDto {
    data class Response(
        val userId: String?,
        val profileImage: String?,
        val nickname: String,
        val providerType: ProviderType,
        val role: RoleType
//        val favorites:
//        val reviews
    ) {
        constructor(user: User) : this(
            userId = user.userId,
            profileImage = user.profileImage,
            nickname = user.nickname,
            providerType = user.providerType,
            role = user.role
//            favorites = user.favorites
//            reviews = user.reviews
        )
    }

    data class SignupRequest(
        var profileImage: String? = null,

        @NotBlank
        var nickname: String,

        val providerType: ProviderType
    )

    data class UpdateProfileRequest(
        var profileImage: String? = null,
        var nickname: String? = null
    )
}
